package dev.luanluz.controller;

import dev.luanluz.model.entity.*;
import dev.luanluz.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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

    @GetMapping("vendas/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    @PostMapping("vendas/checkout")
    @ResponseBody
    public ModelAndView finalizarCompra(
            @RequestParam(name = "pessoa_id") Long pessoaId,
            @RequestParam(name = "enderecoId") Long enderecoId,
            SessionStatus sessionStatus
    ) {
        if (venda.getItensVenda().size() == 0)
            return new ModelAndView("redirect:/vendas/cart");

        Pessoa pessoa = pessoaRepository.pessoa(pessoaId);
        Endereco endereco = enderecoRepository.endereco(enderecoId);

        venda.setData(LocalDate.now());
        venda.setPessoa(pessoa);
        venda.setEndereco(endereco);

        for (ItemVenda item : venda.getItensVenda())
            item.setVenda(venda);

        repository.save(venda);

        sessionStatus.setComplete();

        return new ModelAndView("redirect:/pagina-inicial");
    }

    @GetMapping("vendas/select-delivery-address")
    public ModelAndView selecionarEndereco(
        ModelMap model,
        @RequestParam(name = "pessoa_id", required = false) Long pessoaId,
        Endereco endereco,
        RedirectAttributes redirAttrs
    ) {
        if (pessoaId == null) {
            redirAttrs.addFlashAttribute("messageError", "Você precisa selecionar uma pessoa para efeturar compra.");
            return new ModelAndView("redirect:/vendas/cart");
        }

        if (venda.getItensVenda().size() == 0) {
            redirAttrs.addFlashAttribute("messageError", "Você precisa adicionar itens no carrinho para efetuar compra.");
            return new ModelAndView("redirect:/vendas/cart");
        }

        Pessoa pessoa = pessoaRepository.pessoa(pessoaId);
        endereco.setPessoa(pessoa);

        model.addAttribute("pessoaSelecionada", pessoa);
        model.addAttribute("endereco", endereco);

        return new ModelAndView("/vendas/select-delivery-address", model);
    }

    @PostMapping("vendas/add-delivery-address")
    public ModelAndView adicionarEnderecoEntrega(
        @RequestParam(name = "pessoa_id") Long pessoaId,
        ModelMap model,
        RedirectAttributes redirAttrs,
        @Valid @ModelAttribute("endereco") Endereco endereco,
        BindingResult result
    ) {
        if(result.hasErrors())
            return selecionarEndereco(model, pessoaId, endereco, redirAttrs);

        Pessoa pessoa = pessoaRepository.pessoa(pessoaId);

        endereco.setPessoa(pessoa);
        enderecoRepository.save(endereco);

        return new ModelAndView("redirect:/vendas/select-delivery-address?pessoa_id=" + pessoaId);
    }

    @GetMapping("vendas/cart")
    public ModelAndView carrinho(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        model.addAttribute("pessoasFisicas", pessoaFisicaRepository.pessoasFisicas());
        model.addAttribute("pessoasJuridicas", pessoaJuridicaRepository.pessoasJuridicas());

        return new ModelAndView("/vendas/cart", model);
    }

    @GetMapping("/produtos-disponiveis")
    public ModelAndView listarProdutosParaVenda(ItemVenda itemVenda, ModelMap model) {
        model.addAttribute("produtos", produtoRepository.produtos());

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
    public ModelAndView exibirDetalhes(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("venda", repository.venda(id));

        return new ModelAndView("/vendas/show", model);
    }
}
