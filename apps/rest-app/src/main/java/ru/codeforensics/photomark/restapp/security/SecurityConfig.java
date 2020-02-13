package ru.codeforensics.photomark.restapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String KEY_HEADER_NAME = "X-API-Key";

  @Autowired
  private UserSessionDetailsService userSessionDetailsService;

  @Bean
  public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
    RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
    filter.setPrincipalRequestHeader(KEY_HEADER_NAME);
    filter.setAuthenticationManager(authenticationManager());
    filter.setExceptionIfHeaderMissing(false);
    return filter;
  }

  @Bean
  public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
    PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
    provider.setPreAuthenticatedUserDetailsService(
        new UserDetailsByNameServiceWrapper<>(userSessionDetailsService));
    return provider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity security) throws Exception {
    security
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(requestHeaderAuthenticationFilter())
        .authorizeRequests()
        .antMatchers("/api-docs/**", "/swagger*/**").permitAll()
        .anyRequest().authenticated();
  }
}
