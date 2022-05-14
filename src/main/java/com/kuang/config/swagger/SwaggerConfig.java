package com.kuang.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;


/*
http://localhost:8080/swagger-ui/index.html
*/


@Configuration
@EnableOpenApi
public class SwaggerConfig {///swagger-ui.html

    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试");
    }


    @Bean //配置docket以配置Swagger具体参数
    public Docket docket(Environment environment) {

        //获取要显示的swagger的环境
        Profiles profiles=Profiles.of("dev","test");

        //获取当前项目环境是否为指定环境
        Boolean flag=environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("佳林")//设置分组
                .enable(flag)//是否启用Swagger,false则不能浏览器访问
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口

                //指定要扫描的包
                /*
                any() // 扫描所有，项目中的所有接口都会被扫描到
                none() // 不扫描接口
                // 通过方法上的注解扫描，如withMethodAnnotation(GetMapping.class)只扫描get请求
                withMethodAnnotation(final Class<? extends Annotation> annotation)
                // 通过类上的注解扫描，如.withClassAnnotation(Controller.class)只扫描有controller注解的类中的接口
                withClassAnnotation(final Class<? extends Annotation> annotation)
                basePackage(final String basePackage) // 根据包路径扫描接口
                 */
                .apis(RequestHandlerSelectors.basePackage("com.huang.swagger.controller"))
                //.paths(PathSelectors.ant("/huang/**"))//过滤路径
                .build();
    }

    //配置文档信息
    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("联系人名字", "http://xxx.xxx.com/联系人访问链接", "联系人邮箱");
        return new ApiInfo(
                "Swagger学习", // 标题
                "学习演示如何配置Swagger", // 描述
                "v1.0", // 版本
                "http://terms.service.url/组织链接", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "许可链接", // 许可连接
                new ArrayList<>()// 扩展
        );
    }

}