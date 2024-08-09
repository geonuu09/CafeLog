package com.project.cafelogproject.config;

import com.project.cafelogproject.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserDetailService userService;
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/**").permitAll()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .headers((headers) -> headers  // h2 콘솔 화면 보이도록 하기위해서 header 지정
            .addHeaderWriter(new XFrameOptionsHeaderWriter(
                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

        // 서버가 클라이언트의 상태를 유지하지 않고, 모든 요청을 독릭접으로 처리하도록 설정
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http,
    BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
    throws Exception {
  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
  authProvider.setPasswordEncoder(bCryptPasswordEncoder);
  authProvider.setUserDetailsService(userService);
  return new ProviderManager(authProvider);

}
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
