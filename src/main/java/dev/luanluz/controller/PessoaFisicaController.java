package dev.luanluz.controller;

import dev.luanluz.model.entity.Endereco;
import dev.luanluz.model.entity.PessoaFisica;
import dev.luanluz.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Transactional
@Controller
@RequestMapping("pessoas-fisicas")
public class PessoaFisicaController {
    @Autowired
    PessoaFisicaRepository repository;

    /**
     * @param pessoaFisica
     * @return String
     */
    @GetMapping("/form")
    public ModelAndView form(PessoaFisica pessoaFisica) {
        return new ModelAndView("/pessoas/fisicas/form");
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid PessoaFisica pessoa, BindingResult result) {
        if(result.hasErrors())
            return form(pessoa);

        for (Endereco endereco : pessoa.getEnderecos())
            endereco.setPessoa(pessoa);

        repository.save(pessoa);

        return new ModelAndView("redirect:/pessoas-fisicas/form");
    }
}
