package poc.digest.auth.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Map;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties("app")
public class UserStore {

  private Map<String, String> users;

  @Bean
  UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
        users.entrySet()
            .stream()
            .map(this::createUserDetails)
            .toList()
    );
  }

  private UserDetails createUserDetails(Map.Entry<String, String> user) {
    log.info("Load user: " + user);
    return User.withUsername(user.getKey())
        .password(user.getValue())
        .roles("USER")
        .build();
  }
}
