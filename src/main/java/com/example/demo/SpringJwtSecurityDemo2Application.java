package com.example.demo;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.constant.FileConstant;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
public class SpringJwtSecurityDemo2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtSecurityDemo2Application.class, args);
		new File(FileConstant.USER_FOLDER);
		System.out.println("Application started successfully !....");
	}

	@Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	  
	  @Bean
		 public WebMvcConfigurer corsConfigurer() {
		        return new WebMvcConfigurer() {
		            @Override
		            public void addCorsMappings(CorsRegistry registry) {
		                registry.addMapping("/user/**").allowedOrigins("http://localhost:4200");
		            }
		        };
		    }
	  
//	  @Bean
//		public CorsFilter corsFilter() {
//			CorsConfiguration corsConfiguration = new CorsConfiguration();
//			corsConfiguration.setAllowCredentials(true);
//			corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//			corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//					"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
//					"Access-Control-Request-Method", "Access-Control-Request-Headers"));
//			corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
//					"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//			corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//			urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//			return new CorsFilter(urlBasedCorsConfigurationSource);
//		}
	  
	  @Bean
		public CorsFilter corsFilter() {
		  UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.setAllowCredentials(true);
			corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
			corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
					"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
					"Access-Control-Request-Method", "Access-Control-Request-Headers", "Jwt-Token"));
			corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
					"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Jwt-Token"));
			corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
			urlBasedCorsConfigurationSource.registerCorsConfiguration("/user/**", corsConfiguration);
			return new CorsFilter(urlBasedCorsConfigurationSource);
		}
	  
//	  @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.demo")) // Replace with your package name
//                .paths(PathSelectors.ant("/login")) // Replace with your endpoint path
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("API Documentation")
//                .description("API documentation for your application")
//                .version("1.0.0")
//                .build();
//    }
}
