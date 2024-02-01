package tech.sbk2k1.tasktracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tech.sbk2k1.tasktracker.JWT.JWTAuthenticationEntryPoint;
import tech.sbk2k1.tasktracker.JWT.JWTAuthenticationFilter;

import tech.sbk2k1.tasktracker.services.AuthProjectService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  private JWTAuthenticationEntryPoint point;
  @Autowired
  private JWTAuthenticationFilter filter;

  @Autowired
  private AuthProjectService CustomProjectService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf().disable()
        .authorizeRequests(authorize -> authorize
            .requestMatchers("/project/**").permitAll() // Allow all "/project/..." routes without authentication
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated() // Require authentication for any other routes
        )
        .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider doDaoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(CustomProjectService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }
}