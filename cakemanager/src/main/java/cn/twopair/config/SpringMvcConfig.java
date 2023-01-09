package cn.twopair.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @description: TODO
 * @author: 李佳骏
 * @time: 2022/11/2 16:55
 */

@Configuration
@ComponentScan({"cn.twopair.controller", "cn.twopair.config", "cn.twopair.api"})
@EnableWebMvc
public class SpringMvcConfig {

}

