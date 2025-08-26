package com.example.demo.config;
import com.example.demo.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .requestMatchers( "/api/user").authenticated()
                        .requestMatchers( "/api/user/myreecipes").authenticated()
                        .requestMatchers( "/api/user/favoriterecipes").authenticated()
                        .requestMatchers( "/api/recipes/*/is-rated").authenticated()
                        .requestMatchers( "/api/recipes/*/rate/*").authenticated()
                        .requestMatchers( "/api/recipe/*/planner/*/*").authenticated()
                        .requestMatchers( "/api/planner/*").authenticated()
                        .requestMatchers( "/homephotos").authenticated()
                        .requestMatchers( "/api/planner/delete/*").authenticated()
                        .requestMatchers( "/api/recipes/favorite_recipes").authenticated()
                        .requestMatchers( "/api/recipes/*/is-favorited").authenticated()
                        .requestMatchers( "/api/recipes/*/favorite").authenticated()
                        .requestMatchers( "/api/recipes/*/unfavorite").authenticated()
                        .requestMatchers( "/api/recipes/*/comment").authenticated()
                        .requestMatchers( "/api/recipes/*/deletecomment/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers( "/api/shoppinglist").authenticated()
                        .requestMatchers("/api/recipes/approve").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/recipes/approve/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers( "/uploaded-images/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

