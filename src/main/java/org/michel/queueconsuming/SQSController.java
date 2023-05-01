package com.sensedia.queueconsuming;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SetQueueAttributesRequest;

@RestController
public class SQSController {

    private static final String QUEUE_PREFIX = "myqueue";
    private static final SqsClient SQS_CLIENT = SqsClient.builder().region(Region.US_EAST_1).build();
    // private static String queueUrl;
    private static String queueUrl = "https://sqs.us-east-1.amazonaws.com/097604633943/myqueue";

    private static final Logger logger = LoggerFactory.getLogger(SQSController.class);

    @GetMapping("/createQueue")
    public void createQueue() {
        String queueName = QUEUE_PREFIX + System.currentTimeMillis();

        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(queueName)
                .build();

        SQS_CLIENT.createQueue(createQueueRequest);

        GetQueueUrlResponse getQueueUrlResponse = SQS_CLIENT
                .getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
        queueUrl = getQueueUrlResponse.queueUrl();
    }

    @PostMapping("sendMessage")
    public void sendMessage(@RequestParam("text") String text) {
        SendMessageRequest messageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(text)
                .build();
        SQS_CLIENT.sendMessage(messageRequest);
    }

    @GetMapping("receiveMessagesWithoutDelete")
    public String receiveMessagesWithoutDelete() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();
        List<Message> receivedMessages = SQS_CLIENT.receiveMessage(receiveMessageRequest).messages();

        String messages = "";
        for (Message receivedMessage : receivedMessages) {
            messages += receivedMessage.body() + "\n";
        }
        return messages;
    }

    @GetMapping("/createQueueWithLongPolling")
    public void createQueueWithLongPolling() {
        // String queueName = QUEUE_PREFIX + System.currentTimeMillis();

        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(QUEUE_PREFIX).build();

        SQS_CLIENT.createQueue(createQueueRequest);

        GetQueueUrlResponse getQueueUrlResponse = SQS_CLIENT
                .getQueueUrl(GetQueueUrlRequest.builder().queueName(QUEUE_PREFIX).build());
        queueUrl = getQueueUrlResponse.queueUrl();
        logger.info(queueUrl);

        HashMap<QueueAttributeName, String> attributes = new HashMap<QueueAttributeName, String>();
        attributes.put(QueueAttributeName.RECEIVE_MESSAGE_WAIT_TIME_SECONDS, "20");

        SetQueueAttributesRequest setAttrsRequest = SetQueueAttributesRequest.builder()
                .queueUrl(queueUrl)
                .attributes(attributes)
                .build();

        SQS_CLIENT.setQueueAttributes(setAttrsRequest);
    }

    @GetMapping("receiveMessagesWithLongPolling")
    // public String receiveMessagesWithLongPolling() {
    public void receiveMessagesWithLongPolling() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(1)
                .build();
        List<Message> receivedMessages = SQS_CLIENT.receiveMessage(receiveMessageRequest).messages();

        String messages = "";
        logger.info("chegaram " + receivedMessages.size() + " novas");
        if (!receivedMessages.isEmpty()) {
            for (Message receivedMessage : receivedMessages) {
                messages += receivedMessage.body() + "\n";
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(receivedMessage.receiptHandle())
                        .build();
                SQS_CLIENT.deleteMessage(deleteMessageRequest);
            }
            // return messages;
            logger.info("Recebida nova mensagem: " + messages);
        }
    }

    @GetMapping("/oi")
    public String oi() {
        System.out.println("hello");
        return "hello";
    }

    @PostConstruct
    // @Async
    @Scheduled(fixedDelay = 5000)
    public void invokeMsgEvery5sec() throws InterruptedException {

        logger.info("Enviando 5...");
        sendMessage("Uma nova mensagem a cada 5 seg: " + LocalDateTime.now());
    }

    @PostConstruct
    @Scheduled(fixedDelay = 20000)
    public void invokeMsgEvery20sec() throws InterruptedException {

        logger.info("Enviando 20...");
        sendMessage("Uma nova mensagem a cada 20 seg: " + LocalDateTime.now());
    }

    @PostConstruct
    @Scheduled(fixedDelay = 1000)
    public void polSQS() {

        receiveMessagesWithLongPolling();
    }
}