package com.hiringapp.config;

import com.hiringapp.model.authEntity.User;
import com.hiringapp.jwt.JwtAuthFilter;
import com.hiringapp.repository.UserDetailsRepository;
import com.hiringapp.service.CustomUserDetailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {
    private final CustomUserDetailService customUserDetailsService;
    private final UserDetailsRepository userDetailsRepository;

    public JwtSecurityConfig(final CustomUserDetailService customUserDetailsService,
                             final UserDetailsRepository userDetailsRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userDetailsRepository = userDetailsRepository;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authReq -> authReq
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    // to create default admin
    @Bean
    public CommandLineRunner createAdmin(PasswordEncoder passwordEncoder) {
        return args -> {
            if (userDetailsRepository.findByEmail("harry.ek22@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("harry.ek22@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userDetailsRepository.save(admin);
            }
        };
    }
}