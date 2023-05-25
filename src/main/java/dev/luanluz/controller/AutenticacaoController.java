package dev.luanluz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AutenticacaoController {

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/autenticacao/login");
    }
}
