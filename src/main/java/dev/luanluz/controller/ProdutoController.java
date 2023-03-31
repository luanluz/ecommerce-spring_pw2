package dev.luanluz.controller;

import dev.luanluz.model.entity.ItemVenda;
import dev.luanluz.model.entity.Produto;
import dev.luanluz.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Transactional
@Controller
@RequestMapping("produtos")
public class ProdutoController {
    @Autowired
    ProdutoRepository repository;

    /**
     * @param produto
     * @return
     */
    @GetMapping("/form")
    public String form(Produto produto) {
        return "/produtos/form";
    }

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("produtos", repository.produtos());
        return new ModelAndView("/produtos/list", model);
    }

    @GetMapping("/select")
    public ModelAndView listarProdutosParaVenda(ModelMap model) {
        model.addAttribute("produtos", repository.produtos());
        model.addAttribute("itemVenda", new ItemVenda());

        return new ModelAndView("/produtos/select", model);
    }

    @PostMapping("/save")
    public ModelAndView save(Produto produto) {
        repository.save(produto);
        return new ModelAndView("redirect:/produtos/list");
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/produtos/list");
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("produto", repository.produto(id));
        return new ModelAndView("/produtos/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(Produto produto) {
        repository.update(produto);
        return new ModelAndView("redirect:/produtos/list");
    }
}
