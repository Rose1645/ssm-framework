package com.rose.lzk.config;

import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger接口文档配置：http://localhost:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2 //代表开启swagger2
@ConditionalOnProperty(prefix = "Swagger-config", name = "swagger-ui-open", havingValue = "true")  //@ConditionalOnProperty(prefix = "mconfig", name = "swagger-ui-open", havingValue = "true")是控制当前的config是否生效,其中的参数对应着我的application.yml文件,如果我的参数为非true,那么swagger则关闭
public class SwaggerConfig {

  /**
   * 通过 createRestApi函数来构建一个DocketBean
   * 函数名,可以随意命名,喜欢什么命名就什么命名
   */
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        //控制暴露出去的路径下的实例
        //如果某个接口不想暴露,可以使用以下注解
        //@ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
        //.apis(RequestHandlerSelectors.basePackage("com.example.zwd.springbootswagger2.controller"))
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any())
        .build()
        //添加登录认证
        .securitySchemes(securitySchemes())
        .securityContexts(securityContexts());
  }

  //构建 api文档的详细信息函数
  public ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        //页面标题
        .title("Swagger-Ui开放接口文档")
        //描述
        .description("Rest API接口")
        //条款地址
        .termsOfServiceUrl("https://blog.csdn.net/Rose1645")
        .version("1.0")
        .build();
  }

  private List<ApiKey> securitySchemes() {
    //设置请求头信息
    java.util.List<springfox.documentation.service.ApiKey> result = new ArrayList<>();
    springfox.documentation.service.ApiKey apiKey = new springfox.documentation.service.ApiKey("Authorization", "Authorization", "header");
    result.add(apiKey);
    return result;
  }

  private List<SecurityContext> securityContexts() {
    //设置需要登录认证的路径
    List<SecurityContext> result = new ArrayList<>();
    /*result.add(getContextByPath("/brand/.*"));
    result.add(getContextByPath("/product/.*"));
    result.add(getContextByPath("/productCategory/.*"))*/;
    result.add(getContextByPath(".*"));
    return result;
  }

  private SecurityContext getContextByPath(String pathRegex){
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex(pathRegex))
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    List<SecurityReference> result = new ArrayList<>();
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    result.add(new SecurityReference("Authorization", authorizationScopes));
    return result;
  }
}
