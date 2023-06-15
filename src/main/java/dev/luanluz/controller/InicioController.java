package dev.luanluz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Collection;

@Controller
public class InicioController {
    @GetMapping("/pagina-inicial")
    public void loginPageRedirect(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authResult
    ) throws IOException, ServletException {

        if (authResult != null && authResult.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

            if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/vendas"));

            else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")))
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/produtos-disponiveis"));

        }
    }
}
