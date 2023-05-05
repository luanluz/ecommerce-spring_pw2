package dev.luanluz.controller;

import dev.luanluz.model.entity.Endereco;
import dev.luanluz.model.entity.PessoaJuridica;
import dev.luanluz.repository.PessoaJuridicaRepository;
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
@RequestMapping("pessoas-juridicas")
public class PessoaJuridicaController {
    @Autowired
    PessoaJuridicaRepository repository;

    /**
     * @param pessoaJuridica
     * @return String
     */
    @GetMapping("/form")
    public ModelAndView form(PessoaJuridica pessoaJuridica) {
        return new ModelAndView("/pessoas/juridicas/form");
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid PessoaJuridica pessoa, BindingResult result) {
        if(result.hasErrors())
            return form(pessoa);

        for (Endereco endereco : pessoa.getEnderecos())
            endereco.setPessoa(pessoa);

        repository.save(pessoa);
        return new ModelAndView("redirect:/pessoas-juridicas/form");
    }
}
