package dev.luanluz.controller;

import dev.luanluz.model.entity.ItemVenda;
import dev.luanluz.model.entity.Produto;
import dev.luanluz.model.entity.Venda;
import dev.luanluz.repository.ProdutoRepository;
import dev.luanluz.repository.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Scope("request")
@Transactional
@Controller
@RequestMapping("vendas")
public class VendaController {
    @Autowired
    VendaRepository repository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    Venda venda;

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    @GetMapping("/cart")
    public ModelAndView carrinho(ModelMap model) {
        model.addAttribute("vendas", repository.vendas());
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
}
