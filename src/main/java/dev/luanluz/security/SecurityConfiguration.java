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
                auth.requestMatchers("/css/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/img/**").permitAll()
                    .requestMatchers( "/criar-pessoa/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/salvar-pessoa/**").permitAll()
                    .requestMatchers("/vendas").hasRole("ADMIN")
                    .requestMatchers("/produtos").hasRole("ADMIN")
                    .requestMatchers("/pessoas/**").hasRole("ADMIN")
                    .requestMatchers("/vendas/cart").hasRole("USER")
                    .requestMatchers("/vendas/checkout").hasRole("USER")
                    .requestMatchers("/vendas/select-delivery-address").hasRole("USER")
                    .requestMatchers("/vendas/add-delivery-address").hasRole("USER")
                    .requestMatchers("/vendas/add-item").hasRole("USER")
                    .requestMatchers("/vendas/remove-item/**").hasRole("USER")
                    .requestMatchers("/vendas/remove-all").hasRole("USER")
                    .anyRequest() //define que a configuração é válida para qualquer requisição.
                    .authenticated() //define que o usuário precisa estar autenticado.
                    .and()
                    .formLogin() //define que a autenticação pode ser feita via formulário de login.
                    .loginPage("/entrar").defaultSuccessUrl("/pagina-inicial", true) // passamos como parâmetro a URL para acesso à página de login que criamos
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
