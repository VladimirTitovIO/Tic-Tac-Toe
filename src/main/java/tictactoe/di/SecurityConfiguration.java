package tictactoe.di;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tictactoe.web.model.AuthFilter;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {
        http.csrf(csrf -> csrf.disable()).
                authorizeHttpRequests(auth -> auth.
                requestMatchers("/auth/**").permitAll().anyRequest().authenticated()).
                addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
