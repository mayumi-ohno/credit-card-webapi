package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CreditCardWebapiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardWebapiApplication.class, args);
	}

	// 外部tomcatデプロイ用にサーブレットとして処理できるようにするためOverride
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CreditCardWebapiApplication.class);
	}
}