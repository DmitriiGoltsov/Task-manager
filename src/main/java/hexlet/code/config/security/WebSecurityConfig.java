package hexlet.code.config.security;

import hexlet.code.filters.JwtAuthFilter;
import hexlet.code.services.CustomUserDetailsService;
import hexlet.code.utils.NamePaths;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    public static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));
    private static final String baseUrl = "/api";

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                                requests
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + NamePaths.getLoginPath(), POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + NamePaths.getUsersPath(), POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + NamePaths.getUsersPath(), GET.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + NamePaths.getLabelsPath(), POST.toString())).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher(baseUrl + NamePaths.getLabelsPath(), GET.toString())).permitAll()
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
