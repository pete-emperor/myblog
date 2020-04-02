package com.pyt;

import com.pyt.scheduled.WebSpider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyblogApplication {

	public static void main(String[] args) {

		SpringApplication.run(MyblogApplication.class, args);
		WebSpider webSpider = new WebSpider();
		webSpider.start();
	}

}
