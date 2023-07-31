package com.example.demo.configuration;

import static com.example.demo.constant.SecurityConstant.PUBLIC_URLS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtAccessDeniedHandler;
import com.example.demo.filter.JwtAuthenticationEntryPoint;
import com.example.demo.filter.JwtAuthorizationFilter;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	@Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
	@Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//	@Autowired
//    private UserDetailsService userDetailsService;
//	@Autowired
//    private PasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//    private AuthenticationProvider authenticationProvider;

//    @Autowired
//    public SecurityConfiguration(JwtAuthorizationFilter jwtAuthorizationFilter,
//                                 JwtAccessDeniedHandler jwtAccessDeniedHandler,
//                                 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//                                 @Qualifier("userDetailsService")UserDetailsService userDetailsService,
//                                 BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
//        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.userDetailsService = userDetailsService;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().and()
//                .sessionManagement().sessionCreationPolicy(STATELESS)
//                .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        
        http
		.csrf()
		.disable().cors().and()
		.authorizeHttpRequests()
		.requestMatchers(PUBLIC_URLS)
		.permitAll()
//		.requestMatchers("/user/login")
//		.permitAll()
//		.requestMatchers()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
//		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
}
