package com.github.mxsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MxsmLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MxsmLogApplication.class, args);
	}

}
