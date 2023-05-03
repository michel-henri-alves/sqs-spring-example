package org.michel.queueconsuming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Component
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);



    @SqsListener(value = "myqueue")
	public void receiveMessage(String stringJson) {

		logger.info("Message Received using SQS Listner " + stringJson);
	}
}
