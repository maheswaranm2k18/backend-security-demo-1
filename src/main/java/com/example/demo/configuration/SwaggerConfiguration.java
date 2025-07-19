////package com.example.demo.configuration;
//
////import org.springdoc.core.models.GroupedOpenApi;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
//
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import springfox.documentation.builders.ApiInfoBuilder;
////import springfox.documentation.builders.PathSelectors;
////import springfox.documentation.builders.RequestHandlerSelectors;
////import springfox.documentation.service.ApiInfo;
////import springfox.documentation.spi.DocumentationType;
////import springfox.documentation.spring.web.plugins.Docket;
////import springfox.documentation.swagger2.annotations.EnableSwagger2;
////
////@Configuration
////@EnableSwagger2
////public class SwaggerConfiguration {
////
////    @Bean
////    public Docket api() {
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.yourpackage")) // Replace with your package name
////                .paths(PathSelectors.ant("/register")) // Replace with your endpoint path
////                .build()
////                .apiInfo(apiInfo());
////    }
////
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder()
////                .title("API Documentation")
////                .description("API documentation for your application")
////                .version("1.0.0")
////                .build();
////    }
//	
////	 @Bean
////	    public GroupedOpenApi apiGroup() {
////	        return GroupedOpenApi.builder()
////	                .group("My API") // Replace with your preferred API group name
////	                .packagesToScan("com.example.demo") // Specify your base package here
////	                .build();
////	    }
////}
//
//package com.example.demo.configuration;
//
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContext;
//
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfiguration {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//            .select()
//            .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
//            .paths(PathSelectors.any())
//            .build()
//            .securitySchemes(Arrays.asList(apiKey()))
//            .securityContexts(Arrays.asList(securityContext()))
//            .apiInfo(apiInfo());
//    }
//
//    private SecurityScheme apiKey() {
//        return new SecurityScheme()
//            .type(SecurityScheme.Type.APIKEY)
//            .name("Authorization")
//            .in(SecurityScheme.In.HEADER);
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//            .securityReferences(defaultAuth())
//            .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
//        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
//    }
//
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder()
////            .title("Your API Documentation")
////            .description("API documentation for your application")
////            .version("1.0")
////            .build();
////    }
//    
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
//            .info(new Info()
//                .title("Your API Documentation")
//                .version("1.0")
//                .description("API documentation for your application"));
//    }
//}
//
