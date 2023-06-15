package dev.luanluz.controller;

import dev.luanluz.model.entity.*;
import dev.luanluz.repository.PapelRepository;
import dev.luanluz.repository.PessoaFisicaRepository;
import dev.luanluz.repository.PessoaJuridicaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Transactional
public class AutenticacaoController {
    @Autowired
    PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    PessoaJuridicaRepository pessoaJuridicaRepository;
    @Autowired
    PapelRepository papelRepository;

    @GetMapping("/entrar")
    public ModelAndView login() {
        return new ModelAndView("/autenticacao/entrar");
    }

    @GetMapping("/criar-pessoa/fisica")
    public String criarPessoaFisica(Model model) {
        model.addAttribute("pessoa", new PessoaFisica());
        return "/autenticacao/criar-conta/pessoa-fisica/form";
    }

    @GetMapping("/criar-pessoa/juridica")
    public String criarPessoaJuridica(Model model) {
        model.addAttribute("pessoa", new PessoaJuridica());
        return "/autenticacao/criar-conta/pessoa-juridica/form";
    }

    @PostMapping("/salvar-pessoa/fisica")
    public String salvarPessoaFisica(@ModelAttribute("pessoa") @Valid PessoaFisica pessoa, BindingResult result) {
        return salvarPessoa(pessoa, result);
    }

    @PostMapping("/salvar-pessoa/juridica")
    public String salvarPessoaJuridica(@ModelAttribute("pessoa") @Valid PessoaJuridica pessoa, BindingResult result) {
        return salvarPessoa(pessoa, result);
    }

    private String salvarPessoa(Pessoa pessoa, BindingResult result) {
        if (result.hasErrors()) {
            if (pessoa instanceof PessoaFisica) {
                return "/autenticacao/criar-conta/pessoa-fisica/form";
            } else if (pessoa instanceof PessoaJuridica) {
                return "/autenticacao/criar-conta/pessoa-juridica/form";
            }
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Papel papel = papelRepository.papel("ROLE_USER");
        Usuario usuario = pessoa.getUsuario();
        String senhaCodificada = encoder.encode(usuario.getSenha());

        usuario.setSenha(senhaCodificada);
        usuario.setPessoa(pessoa);
        usuario.getPapeis().add(papel);

        for (Endereco endereco : pessoa.getEnderecos())
            endereco.setPessoa(pessoa);

        if (pessoa instanceof PessoaFisica) {
            pessoaFisicaRepository.save((PessoaFisica) pessoa);
        } else if (pessoa instanceof PessoaJuridica) {
            pessoaJuridicaRepository.save((PessoaJuridica) pessoa);
        }

        return "redirect:/entrar";
    }
}
