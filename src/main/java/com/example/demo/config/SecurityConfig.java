package com.example.demo.config;

import com.example.demo.security.JwtAuthentication;
import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthentication jwtAuthentication;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthentication jwtAuthentication, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthentication = jwtAuthentication;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize.antMatchers(HttpMethod.GET,"/api/**").permitAll()
                                .antMatchers("/api/auth/**").permitAll()
                                .antMatchers(HttpMethod.GET,"/api/categories/**").permitAll()
                                .antMatchers("/v3/api-docs/**").permitAll()
                                .antMatchers("/swagger-ui/**").permitAll()
                                .antMatchers("/swagger-resources/**").permitAll()
                                .antMatchers("/swagger-ui.html/**").permitAll()
                                .antMatchers("/webjars/**").permitAll()
                        .anyRequest().authenticated()

                ).exceptionHandling((exception) ->exception.authenticationEntryPoint(jwtAuthentication)
        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService(){
        UserDetails mustafa = User.builder()
                .username("mustafa")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();
        UserDetails musa = User.builder()
                .username("musa")
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(mustafa,musa);
    } */
}



















