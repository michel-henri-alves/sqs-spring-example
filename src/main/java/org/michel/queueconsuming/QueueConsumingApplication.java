package org.michel.queueconsuming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.awspring.cloud.sqs.annotation.SqsListener;


@SpringBootApplication
@ComponentScan(basePackages = "org.michel.queueconsuming")

// @ComponentScan(basePackages = "org.michel.queueconsuming.*")
public class QueueConsumingApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueConsumingApplication.class, args);
	}

	@SqsListener("myqueue")
    public void listen(String message) {
        System.out.println(message);
    }
}
