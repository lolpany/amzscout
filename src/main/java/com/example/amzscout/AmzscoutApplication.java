package com.example.amzscout;

import org.aspectj.lang.Aspects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AmzscoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmzscoutApplication.class, args);
	}

	@Bean
	public ThrottleAspect interceptor() {
		return Aspects.aspectOf(ThrottleAspect.class);
	}
}
