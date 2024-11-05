package com.ict.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ict.common.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/**") //모든경로검토
			.excludePathPatterns("/sns_login");
			//.excludePathPatterns("/sns_login","/gb2_list","/register","public/**");
	}
}
