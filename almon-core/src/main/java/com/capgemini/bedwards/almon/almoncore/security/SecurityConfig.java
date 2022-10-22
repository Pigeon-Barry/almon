package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.services.APIKeyService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    @Autowired
    DataSource dataSource;
    @Autowired
    private AlmonAuthenticationProvider authProvider;

    private final static CsrfTokenRepository CSRF_TOKEN_REPOSITORY = CookieCsrfTokenRepository.withHttpOnlyFalse();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


    @Configuration
    @Order(1)
    public static class WebSecurityConfig {
        public AccessDeniedHandler accessDeniedHandler() {
            return (request, response, accessDeniedException) -> {
                User user = SecurityUtil.getAuthenticatedUser();
                if (user != null) {
                    if (user.getApprovedBy() == null) {
                        response.sendRedirect("/web/auth/pendingApproval");
                        return;
                    }
                    if (!user.isEnabled()) {
                        response.sendRedirect("/web/auth/accountDisabled");
                        return;
                    }
                }
                response.sendRedirect("/web/error");

            };
        }

        @Bean
        public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/web/**")
                    .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler()))
                    .csrf(configurer -> configurer.csrfTokenRepository(CSRF_TOKEN_REPOSITORY))
                    .cors()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .and()
                    .formLogin(form -> form
                            .defaultSuccessUrl("/web/home", true)
                            .loginPage("/web/auth/login")
                            .loginProcessingUrl("/web/auth/login")
                            .usernameParameter("email")
                            .failureUrl("/web/auth/login?invalid=true")
                            .permitAll()
                    )
                    .logout(logoutConfigurer -> logoutConfigurer.logoutUrl("/web/auth/logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .deleteCookies("remember-me")
                            .clearAuthentication(true)
                            .permitAll())
                    .authorizeRequests(requests -> requests
                            .antMatchers("/actuator/**").permitAll()
                            .antMatchers("/web/css/**", "/web/js/**", "/web/img/**", "/web/favicon.ico", "/web/auth/**").permitAll()
                            .antMatchers("/web/auth/pendingApproval").authenticated()
                            .antMatchers("/web/auth/accountDisabled").authenticated()
                            .antMatchers("/web/admin/**").hasAnyAuthority("VIEW_ADMIN_PAGES")
                            .antMatchers("/web/error").permitAll()
                            .antMatchers("/web/**").hasAnyAuthority("ACCESS_CORE_PAGES")
                    )
            ;
            return http.build();
        }
    }

    @Configuration
    @Order(2)
    public static class ApiSecurityConfig {
        @Autowired
        private APIKeyService apiKeyService;

        @Bean
        public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .cors()
                    .and()
                    .csrf(configurer -> configurer.csrfTokenRepository(CSRF_TOKEN_REPOSITORY))
                    .addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyService), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
            return http.build();
        }
    }


}
