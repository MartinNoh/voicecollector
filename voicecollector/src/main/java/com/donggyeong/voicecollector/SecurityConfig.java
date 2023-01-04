package com.donggyeong.voicecollector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


import lombok.RequiredArgsConstructor;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.donggyeong.voicecollector.user.UserSecurityService;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	private final UserSecurityService userSecurityService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/registration/**").hasRole("ADMIN")
			.antMatchers("/**").permitAll()
	        .and()  // @PreAuthorize 미사용 시 csrf ignore 할 것
	        .csrf().ignoringAntMatchers("/collection/upload/**")
	        .and()
	        .csrf().ignoringAntMatchers("/collection/modify/**")
	        .and()
	        .csrf().ignoringAntMatchers("/email/confirm/**")
	        .and()
	        .headers()
	        .addHeaderWriter (new XFrameOptionsHeaderWriter(
	                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            .and()
            .formLogin()
            .loginPage("/user/login")
            .usernameParameter("email")
//            .defaultSuccessUrl("/")
            .defaultSuccessUrl("/collection/record")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
			;
		return http.build();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}