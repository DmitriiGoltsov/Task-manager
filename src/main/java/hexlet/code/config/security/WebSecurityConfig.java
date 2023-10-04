package hexlet.code.config.security;

import hexlet.code.filters.JwtAuthFilter;
import hexlet.code.services.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import java.util.List;

import static hexlet.code.controllers.UsersController.USER_CONTROLLER_PATH;
import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_URL;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    public static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));
    public static final String LOGIN = "/login";

    private static final String baseUrl = "/api";

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        
        http
                .authorizeHttpRequests(requests ->
                                requests
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + LOGIN, POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, GET.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + LABEL_CONTROLLER_URL, POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + LABEL_CONTROLLER_URL, GET.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/h2console/**", GET.toString())).permitAll()
                                        .requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))).permitAll()
                                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
