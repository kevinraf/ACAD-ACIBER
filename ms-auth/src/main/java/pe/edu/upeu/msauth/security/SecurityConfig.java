package pe.edu.upeu.msauth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // ENDPOINTS PÚBLICOS
                .antMatchers(
                        "/auth/login",
                        "/auth/create",
                        "/auth/validate",   // 👈 AÑADIR ESTO
                        "/v3/api-docs/**",
                        "/doc/**",
                        "/h2-console/**",
                        "/actuator/**"
                ).permitAll()
                // TODO lo demás, si luego agregas algo, protegido
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable(); // para h2-console
    }
}
