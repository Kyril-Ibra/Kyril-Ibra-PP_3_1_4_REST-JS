package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServiceImpl userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").usernameParameter("email").successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and().exceptionHandling().accessDeniedPage("/403");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {  //проверяет по логину и паролю существует ли такой пользователь
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);  //Предоставляет пользователей из userService (по имени пользователя)
        return provider;
    }
}