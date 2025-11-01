package com.samit.securitybasic.config;


import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth->auth.requestMatchers("/login/*","/logout/*").permitAll()
                .anyRequest().authenticated())
                .csrf(csrfConfig-> csrfConfig.disable())
                //.sessionManagement(sessionConfig ->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return httpSecurity.build();
   }

   @Bean
   UserDetailsService userDetailsService(){
       UserDetails user =User.withUsername("deepak")
               .password(passwordEncoder().encode("deepak@123"))
               .roles("USER")
               .build();
       UserDetails admin =User.withUsername("verma")
               .password(passwordEncoder().encode("verma@123"))
               .roles("ADMIN")
               .build();
        return new InMemoryUserDetailsManager(user,admin);
   }

   @Bean
   PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }


}
