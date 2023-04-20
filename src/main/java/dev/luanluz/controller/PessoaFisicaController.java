package dev.luanluz.controller;

import dev.luanluz.model.entity.Endereco;
import dev.luanluz.model.entity.PessoaFisica;
import dev.luanluz.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String form(PessoaFisica pessoaFisica) {
        return "/pessoas-fisicas/form";
    }

    @PostMapping("/save")
    public ModelAndView save(PessoaFisica pessoaFisica) {
        for (Endereco endereco : pessoaFisica.getEnderecos())
            endereco.setPessoa(pessoaFisica);

        repository.save(pessoaFisica);
        return new ModelAndView("redirect:/pessoas-fisicas/form");
    }
}
