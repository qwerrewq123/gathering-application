package spring.myproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import spring.myproject.common.filter.JwtAuthFilter;
import spring.myproject.common.security.FailedAuthenticationEntryPoint;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FailedAuthenticationEntryPoint failedAuthenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;
    private String[] getWhiteList = {"/connect/**","/gatherings","/gathering/*","/gathering","/gathering/*/meetings",
            "/gathering/*/meeting/*","/image/*","/gathering/*/image","/gathering/*/boards","/auth/user/*","/gathering/participated/*","/health"};
    private String[] postWhiteList = {"/auth/id-check","/auth/generateToken","auth/nickname-check",
            "/auth/email-certification","/auth/check-certification","/auth/sign-in","/auth/sign-up"};
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(POST,postWhiteList).permitAll()
                        .requestMatchers(GET,getWhiteList).permitAll()
                        .requestMatchers("/connect").permitAll()
                        .requestMatchers("/fcm/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandle -> exceptionHandle.authenticationEntryPoint(failedAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
