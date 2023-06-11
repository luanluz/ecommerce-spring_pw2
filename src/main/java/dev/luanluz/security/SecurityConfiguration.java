package dev.luanluz.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private ConfigDetalhesUsuario configDetalhesUsuario;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> {
            try {
                auth.requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/pessoa/form").permitAll()
                    .requestMatchers(HttpMethod.POST, "/pessoa/save").permitAll()
                    .requestMatchers("/painel/**").hasRole("ADMIN")
                    .requestMatchers("/vendas/list").hasRole("ADMIN")
                    .requestMatchers("/produtos").hasRole("ADMIN")
                    .anyRequest() //define que a configuração é válida para qualquer requisição.
                    .authenticated() //define que o usuário precisa estar autenticado.
                    .and()
                    .formLogin() //define que a autenticação pode ser feita via formulário de login.
                    .loginPage("/login").defaultSuccessUrl("/pagina-inicial", true) // passamos como parâmetro a URL para acesso à página de login que criamos
                    .permitAll() //define que essa página pode ser acessada por todos, independentemente do usuário estar autenticado ou não.
                    .and()
                    .logout() //para logout
                    .permitAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).httpBasic(withDefaults());

        return http.build();
    }

    @Autowired
    public void configureUserDetails(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(configDetalhesUsuario)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
