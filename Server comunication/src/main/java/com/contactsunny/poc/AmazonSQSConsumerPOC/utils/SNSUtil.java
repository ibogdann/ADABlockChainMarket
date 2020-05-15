package com.contactsunny.poc.AmazonSQSConsumerPOC.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.contactsunny.poc.AmazonSNSPublisherPOC.models.SNSMessage;

@Component
public class SNSUtil {

    @Value("${sns.topic.arn}")
    private String snsTopicARN;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    private AmazonSNS amazonSNS;

    private static final Logger logger = LoggerFactory.getLogger(SNSUtil.class);

    @PostConstruct
    private void postConstructor() {

        logger.info("SQS URL: " + snsTopicARN);

        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey)
        );

        this.amazonSNS = AmazonSNSClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion)
                .build();
    }

    public void publishSNSMessage(SNSMessage message) {

        logger.info("Publishing SNS message: " + message);

        final PublishRequest request = new PublishRequest(snsTopicARN, message.getMessage())
                .withMessageAttributes(message.getMessageAttributes());
        PublishResult result = this.amazonSNS.publish(request);

        logger.info("SNS Message ID: " + result.getMessageId());
        logger.info("SNS Message: " + message.getMessage());
    }
    
}
