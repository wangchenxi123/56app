package com.mierro.common.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by 唐亮 on 2017/4/1.
 */
@EnableWebMvc
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi1() {
        System.out.println("SwaggerConfig.createRestApi--1");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("权限管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mierro.authority.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket createRestApi2() {
        System.out.println("SwaggerConfig.createRestApi--1");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("文件上传")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mierro.fileOperation.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket createRestApi3() {
        System.out.println("SwaggerConfig.createRestApi--1");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("主业务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mierro.main.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket createRestApi4() {
        System.out.println("SwaggerConfig.createRestApi--1");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("机器人")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mierro.robot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("下载有钱赚")
                .description("### 返回数据结构说明\n" +
                        "默认response200只能显示 ResultMessage原来的属性，resultParm中的数据结构只能自定义在在后面的 Response Messages中显示，\n" +
                        "1. HTTP Status Code： 从1开始\n" +
                        "2. Reason：是字段名及说明，有两种格式： \"字段名=对象\"，\"字段名=[数组]\n" +
                        "3. Response Model：显示对象的结构，或数组中对象的结构\n" +
                        "\n" +
                        "### 默认返回状态\n" +
                        "| 状态码（responseCode）|说明（message） |\n" +
                        "|------|-----|\n" +
                        "| 200 | 操作成功|\n" +
                        "| 400 | 语义错误、请求参数错误|\n" +
                        "| 401 | 未登录|\n" +
                        "| 403 | 无权限操作|\n" +
                        "| 405 | 业务失败|\n" +
                        "| 406 | 服务器拒绝访问|\n" +
                        "| 407 | 系统维护中|\n" +
                        "| 408 | 找不到代理或子代理|\n" +
                        "| 500 | INTERNAL_SREVER_ERROR|")
                .termsOfServiceUrl("http://ios1.mierro.com/")
                .version("1.0")
                .build();
    }
}
