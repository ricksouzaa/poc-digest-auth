package poc.digest.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  DigestAuthenticationEntryPoint digestAuthenticationEntryPoint() {
    DigestAuthenticationEntryPoint result = new DigestAuthenticationEntryPoint();
    result.setRealmName("REALM");
    result.setKey("12345");
    result.setNonceValiditySeconds(3600);
    return result;
  }

  DigestAuthenticationFilter digestAuthenticationFilter() {
    DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
    filter.setUserDetailsService(userDetailsService);
    filter.setCreateAuthenticatedToken(true);
    filter.setPasswordAlreadyEncoded(true);
    filter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint());
    return filter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(withDefaults())
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .exceptionHandling(e -> e.authenticationEntryPoint(digestAuthenticationEntryPoint()))
        .addFilterBefore(digestAuthenticationFilter(), BasicAuthenticationFilter.class)
        .headers(
            headers -> headers
                .xssProtection(withDefaults())
                .contentSecurityPolicy(
                    config -> config.policyDirectives("default-src 'self'"))
        )
        .build();
  }

}
