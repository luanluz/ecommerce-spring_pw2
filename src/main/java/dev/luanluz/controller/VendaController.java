package dev.luanluz.controller;

import dev.luanluz.model.entity.*;
import dev.luanluz.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Scope("request")
@Transactional
@Controller
@SessionAttributes("vendas")
@RequestMapping("vendas")
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

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    @PostMapping("/checkout")
    @ResponseBody
    public ModelAndView finalizarCompra(
            @RequestParam(name = "pessoa") Long pessoaId,
            HttpSession session
    ) {
        if (venda.getItensVenda().size() == 0)
            return new ModelAndView("redirect:/vendas/cart");

        Pessoa pessoa = pessoaRepository.pessoa(pessoaId);

        venda.setData(LocalDate.now());
        venda.setPessoa(pessoa);

        for (ItemVenda item : venda.getItensVenda())
            item.setVenda(venda);

        repository.save(venda);

        session.invalidate();

        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/cart")
    public ModelAndView carrinho(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        model.addAttribute("pessoasFisicas", pessoaFisicaRepository.pessoasFisicas());
        model.addAttribute("pessoasJuridicas", pessoaJuridicaRepository.pessoasJuridicas());

        return new ModelAndView("/vendas/cart", model);
    }

    @PostMapping("/add-item")
    public ModelAndView addItem(ItemVenda itemVenda) {
        Long produtoId = (long) itemVenda.getProduto().getId();
        Produto produto = produtoRepository.produto(produtoId);
        itemVenda.setProduto(produto);

        venda.getItensVenda().add(itemVenda);
        return new ModelAndView("redirect:/produtos/select");
    }

    @GetMapping("/remove-item/{id}")
    public ModelAndView removerItem(@PathVariable("id") int id) {
        venda.getItensVenda().remove(id);
        return new ModelAndView("redirect:/vendas/cart");
    }

    @GetMapping("/remove-all")
    public ModelAndView removerTodosItens() {
        venda.getItensVenda().clear();
        return new ModelAndView("redirect:/vendas/cart");
    }

    @GetMapping("/show/{id}")
    public ModelAndView exibirDetalhes(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("venda", repository.venda(id));

        return new ModelAndView("/vendas/show", model);
    }
}
