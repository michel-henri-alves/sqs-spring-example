package org.michel.queueconsuming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "org.michel.queueconsuming")

// @ComponentScan(basePackages = "org.michel.queueconsuming.*")
public class QueueConsumingApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueConsumingApplication.class, args);
	}

}
