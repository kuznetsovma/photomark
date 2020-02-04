package ru.codeforensics.photomark.uploadapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private APIKeyManager apiKeyManager;

  @Override
  protected void configure(HttpSecurity security) throws Exception {
    APIKeyFilter filter = new APIKeyFilter();
    filter.setAuthenticationManager(apiKeyManager);
    security
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(filter)
        .authorizeRequests()
        .anyRequest().authenticated();
  }

}
