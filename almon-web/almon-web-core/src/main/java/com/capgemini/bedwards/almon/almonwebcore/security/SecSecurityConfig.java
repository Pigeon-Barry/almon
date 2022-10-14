package com.capgemini.bedwards.almon.almonwebcore.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecSecurityConfig {

    @Autowired
    DataSource dataSource;
    @Autowired
    private AlmonAuthenticationProvider authProvider;


    private String API_KEY = "SomeKey1234567890";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .csrf().disable()
                .addFilterBefore(new ApiKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "auth/*", "/favicon.ico", "/auth/**").permitAll()
                .antMatchers("/web/auth/pendingApproval").authenticated()
                .antMatchers("/web/admin/**").hasAnyAuthority("VIEW_ADMIN_PAGES")
                .antMatchers("/error").permitAll()
                .antMatchers("/**").hasAnyAuthority("ACCESS_CORE_PAGES")
                .and()
                .csrf().disable()
                .cors()
                .and()
                .formLogin()
                .defaultSuccessUrl("/web/home")
                .loginPage("/web/auth/login")
                .loginProcessingUrl("/web/auth/login")
                .usernameParameter("email")
                .failureUrl("/web/auth/login?invalid=true")
                .and()
//                .disable()
                .logout()
                .logoutUrl("/web/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ;

        return http.build();
    }
}
