package de.workshops.bookshelf.config;

import static org.springframework.security.config.Customizer.withDefaults;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // see https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html,
    // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework, and
    // https://github.com/spring-projects/spring-security/issues/12915#issuecomment-1482931700
    XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
    delegate.setCsrfRequestAttributeName("_csrf");
    CsrfTokenRequestHandler requestHandler = new CsrfTokenRequestHandler() {
      @Override
      public void handle(
          HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        delegate.handle(request, response, csrfToken);
      }

      @Override
      public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String tokenValue = CsrfTokenRequestHandler.super.resolveCsrfTokenValue(request, csrfToken);
        if (tokenValue.length() == 36) {
          return tokenValue;
        }
        return delegate.resolveCsrfTokenValue(request, csrfToken);
      }
    };

    return http
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .anyRequest().authenticated()
        )
        .csrf(
            csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(requestHandler)
        )
        .httpBasic(withDefaults())
        .formLogin(withDefaults())
        .build();
  }
  @Bean
  UserDetailsService userDetailsService() {
    return username -> {
      String sql = "SELECT * FROM bookshelf_user WHERE username = ?";

      return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
          rs.getString("username"),
          rs.getString("password"),
          Collections.singletonList(
              new SimpleGrantedAuthority(rs.getString("role"))
          )
      ), username);
    };
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
