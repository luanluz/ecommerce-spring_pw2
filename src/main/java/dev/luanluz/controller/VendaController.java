package dev.luanluz.controller;

import dev.luanluz.model.entity.*;
import dev.luanluz.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Scope("request")
@Transactional
@Controller
@SessionAttributes("venda")
public class VendaController {
    @Autowired
    Venda venda;
    @Autowired
    VendaRepository repository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    PessoaJuridicaRepository pessoaJuridicaRepository;
    @Autowired
    EnderecoRepository enderecoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("vendas")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    @GetMapping("vendas/filtrar-por-data")
    public ModelAndView filtrarVendas(
        ModelMap model,
        @RequestParam(name = "data", required = false) String data,
        Authentication authentication
    ) {
        List<Venda> vendas;

        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            if (data == null)
                vendas = repository.vendas();
            else
                vendas = repository.vendasPorData(LocalDate.parse(data));
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Usuario usuario = usuarioRepository.usuario(username);

            if (data == null)
                vendas = repository.vendasPorPessoa(usuario.getId());
            else
                vendas = repository.vendasDePessoaPorData(usuario.getId(), LocalDate.parse(data));
        }

        model.addAttribute("vendas", vendas);
        return new ModelAndView("/vendas/list", model);
    }

    @GetMapping("compras")
    public ModelAndView listarVendasPorCliente(ModelMap model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.usuario(username);

        model.addAttribute("vendas", repository.vendasPorPessoa(usuario.getId()));
        return new ModelAndView("/vendas/list", model);
    }

    @PostMapping("vendas/checkout")
    @ResponseBody
    public ModelAndView finalizarCompra(
            @RequestParam(name = "enderecoId") Long enderecoId,
            SessionStatus sessionStatus,
            Authentication authentication
    ) {
        if (venda.getItensVenda().size() == 0)
            return new ModelAndView("redirect:/vendas/cart");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.usuario(username);
        Pessoa pessoa = pessoaRepository.pessoa(usuario.getPessoa().getId());
        Endereco endereco = enderecoRepository.endereco(enderecoId);

        venda.setData(LocalDate.now());
        venda.setPessoa(pessoa);
        venda.setEndereco(endereco);

        for (ItemVenda item : venda.getItensVenda())
            item.setVenda(venda);

        repository.save(venda);

        sessionStatus.setComplete();
        venda.getItensVenda().clear();

        return new ModelAndView("redirect:/compras");
    }

    @GetMapping("vendas/select-delivery-address")
    public ModelAndView selecionarEndereco(
        ModelMap model,
        Endereco endereco,
        RedirectAttributes redirAttrs,
        Authentication authentication
    ) {
        if (venda.getItensVenda().size() == 0) {
            redirAttrs.addFlashAttribute("messageError", "Você precisa adicionar itens no carrinho para efetuar compra.");
            return new ModelAndView("redirect:/vendas/cart");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.usuario(username);
        Pessoa pessoa = pessoaRepository.pessoa(usuario.getPessoa().getId());
        endereco.setPessoa(pessoa);

        model.addAttribute("pessoaSelecionada", pessoa);
        model.addAttribute("endereco", endereco);

        return new ModelAndView("/vendas/select-delivery-address", model);
    }

    @PostMapping("vendas/add-delivery-address")
    public ModelAndView adicionarEnderecoEntrega(
        ModelMap model,
        RedirectAttributes redirAttrs,
        @Valid @ModelAttribute("endereco") Endereco endereco,
        BindingResult result,
        Authentication authentication
    ) {
        if(result.hasErrors())
            return selecionarEndereco(model, endereco, redirAttrs, authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.usuario(username);
        Pessoa pessoa = pessoaRepository.pessoa(usuario.getPessoa().getId());

        endereco.setPessoa(pessoa);
        enderecoRepository.save(endereco);

        return new ModelAndView("redirect:/vendas/select-delivery-address");
    }

    @GetMapping("vendas/cart")
    public ModelAndView carrinho(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());

        return new ModelAndView("/vendas/cart", model);
    }

    @GetMapping("/produtos-disponiveis")
    public ModelAndView listarProdutosParaVenda(ItemVenda itemVenda, ModelMap model) {
        model.addAttribute("produtos", produtoRepository.produtos());

        return new ModelAndView("/vendas/select", model);
    }

    @GetMapping("/produtos-disponiveis/filtrar-por")
    public ModelAndView filtrarProdutosPor(
            ItemVenda itemVenda,
            ModelMap model,
            @RequestParam(name = "produto_descricao", required = false) String produtoDescricao
    ) {
        List<Produto> produtos;

        if (produtoDescricao == null || produtoDescricao.equals(""))
            produtos = produtoRepository.produtos();
        else
            produtos = produtoRepository.produtosPorNome(produtoDescricao);

        model.addAttribute("produtos", produtos);

        return new ModelAndView("/vendas/select", model);
    }

    @PostMapping("vendas/add-item")
    public ModelAndView addItem(
        @Valid ItemVenda itemVenda,
        BindingResult result,
        ModelMap model
    ) {
        if(result.hasErrors()) {
            return listarProdutosParaVenda(itemVenda, model);
        }

        Long produtoId = (long) itemVenda.getProduto().getId();
        Produto produto = produtoRepository.produto(produtoId);
        itemVenda.setProduto(produto);

        venda.getItensVenda().add(itemVenda);
        return new ModelAndView("redirect:/produtos-disponiveis");
    }

    @GetMapping("vendas/remove-item/{id}")
    public ModelAndView removerItem(@PathVariable("id") int id) {
        venda.getItensVenda().remove(id);
        return new ModelAndView("redirect:/vendas/cart");
    }

    @GetMapping("vendas/remove-all")
    public ModelAndView removerTodosItens() {
        venda.getItensVenda().clear();
        return new ModelAndView("redirect:/vendas/cart");
    }

    @GetMapping("vendas/show/{id}")
    public ModelAndView exibirDetalhes(@PathVariable("id") Long id, ModelMap model, Authentication authentication) {
        Venda venda = repository.venda(id);

        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
                || venda.getPessoa().getUsuario().getUsername().equals(authentication.getName())) {
            model.addAttribute("venda", venda);
            return new ModelAndView("/vendas/show", model);
        }

        return new ModelAndView("redirect:/pagina-inicial");
    }
}
