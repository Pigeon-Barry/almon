package com.capgemini.bedwards.almon.almonwebcore.security;

import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almonwebcore.exception.handling.WebExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecSecurityConfig {

    @Autowired
    DataSource dataSource;
    @Autowired
    private AlmonAuthenticationProvider authProvider;
    @Autowired
    private APIKeyService apiKeyService;
    @Autowired
    private WebExceptionHandler webExceptionHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository()
    {
        return new InMemoryHttpTraceRepository();
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
                .exceptionHandling()

                .accessDeniedHandler(webExceptionHandler);
        http
                .antMatcher("/api/**")
                .csrf().disable()
                .addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .antMatcher("/web/**")
                .exceptionHandling()
                .accessDeniedHandler(webExceptionHandler)
                .and()
                .csrf()
                .and()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http
                .authorizeRequests()
                .antMatchers("/web/css/**", "/web/js/**", "/web/img/**", "/web/favicon.ico", "/web/auth/**").permitAll()
                .antMatchers("/web/auth/pendingApproval").authenticated()
                .antMatchers("/web/admin/**").hasAnyAuthority("VIEW_ADMIN_PAGES")
                .antMatchers("/error").permitAll()
                .antMatchers("/web/**").hasAnyAuthority("ACCESS_CORE_PAGES")
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
