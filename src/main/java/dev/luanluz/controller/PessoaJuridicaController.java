package dev.luanluz.controller;

import dev.luanluz.model.entity.Endereco;
import dev.luanluz.model.entity.PessoaJuridica;
import dev.luanluz.repository.PessoaJuridicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String form(PessoaJuridica pessoaJuridica) {
        return "/pessoas/juridicas/form";
    }

    @PostMapping("/save")
    public ModelAndView save(PessoaJuridica pessoaJuridica) {
        for (Endereco endereco : pessoaJuridica.getEnderecos())
            endereco.setPessoa(pessoaJuridica);

        repository.save(pessoaJuridica);
        return new ModelAndView("redirect:/pessoas-juridicas/form");
    }
}
