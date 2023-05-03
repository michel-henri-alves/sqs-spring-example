package org.michel.queueconsuming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Configuration
public class SpringCloudSQSConfig {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Value("${cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

    @Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
        logger.info("No bean..." );
		return new QueueMessagingTemplate(amazonSQSAsync());
	}

    public AmazonSQSAsync amazonSQSAsync() {
		logger.info("Configurando..." );

		return AmazonSQSAsyncClientBuilder.standard()
		.withRegion(region)
		.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
		.build();
	}
}
