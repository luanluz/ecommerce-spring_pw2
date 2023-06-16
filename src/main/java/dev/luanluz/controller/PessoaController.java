package dev.luanluz.controller;

import dev.luanluz.model.entity.Pessoa;
import dev.luanluz.repository.PessoaFisicaRepository;
import dev.luanluz.repository.PessoaJuridicaRepository;
import dev.luanluz.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
@RequestMapping("pessoas")
public class PessoaController {
    @Autowired
    PessoaRepository repository;
    @Autowired
    PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    PessoaJuridicaRepository pessoaJuridicaRepository;

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("pessoas", repository.pessoas());
        return new ModelAndView("/pessoas/list", model);
    }

    @GetMapping("/show/{id}")
    public ModelAndView exibirDetalhes(@PathVariable("id") Long id, ModelMap model) {
        Pessoa pessoa = repository.pessoa(id);

        model.addAttribute("pessoa", pessoa);
        return new ModelAndView("/pessoas/show", model);
    }

    @GetMapping("/filtrar-por")
    public ModelAndView filtrarProdutosPor(
        ModelMap model,
        @RequestParam(name = "nome", required = false) String nome
    ) {
        List<Pessoa> pessoas;

        if (nome == null || nome.equals("")) {
            pessoas = repository.pessoas();
        } else {
            List<? extends Pessoa> pessoasFisicas = pessoaFisicaRepository.pessoaFisicaPorNome(nome);
            List<? extends Pessoa> pessoasJuridicas = pessoaJuridicaRepository.pessoaJuridicaPorNome(nome);

            pessoas = new ArrayList<>();
            pessoas.addAll(pessoasFisicas);
            pessoas.addAll(pessoasJuridicas);
        }

        model.addAttribute("pessoas", pessoas);

        return new ModelAndView("/pessoas/list", model);
    }
}
