package com.zsy.loan.admin.config;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 *
 * @author fengshuonan
 * @date 2017年6月1日19:42:59
 */
@Slf4j
@EnableSwagger2
@Configuration
@ConditionalOnProperty(prefix = "swagger", name = "open", havingValue = "true")
public class SwaggerConfig {

  @Value("${swagger.project.api-package-path:'com'}")
  private String apiPackage;
  @Value("${swagger.project.name:''}")
  private String projectName;
  @Value("${swagger.project.description:''}")
  private String projectDescription;
  @Value("${swagger.project.version:''}")
  private String projectVersion;
  @Value("${swagger.open}")
  private String open;


  @Bean
  public Docket createRestApi() {

    log.info("扫描api路径：" + apiPackage);
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage(apiPackage)) // 注意修改此处的包名
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(projectName)
        .description(projectDescription)
        .version(projectVersion)
        .build();
  }


}
