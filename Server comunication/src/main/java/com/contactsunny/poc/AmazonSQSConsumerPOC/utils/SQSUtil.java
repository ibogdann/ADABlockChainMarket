package com.contactsunny.poc.AmazonSQSConsumerPOC.utils;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.contactsunny.poc.AmazonSNSPublisherPOC.models.SNSMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class SQSUtil {

	@Value("${sqs.url}")
	private String sqsUrl;

	@Value("${aws.accessKey}")
	private String awsAccessKey;

	@Value("${aws.secretKey}")
	private String awsSecretKey;

	@Value("${aws.region}")
	private String awsRegion;

	private AmazonSQS amazonSQS;

	private static final Logger logger = LoggerFactory.getLogger(SQSUtil.class);
	public static final String VALUE = "Value";
	public static final String MESSAGE_ATTRIBUTES = "MessageAttributes";
	public static final String MESSAGE = "Message";

	@PostConstruct
	private void postConstructor() {

		logger.info("SQS URL: " + sqsUrl);

		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(awsAccessKey, awsSecretKey));

		this.amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(awsRegion)
				.build();
	}

	public void readMessages() {

		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withMaxNumberOfMessages(1)
				.withWaitTimeSeconds(3);

		final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

		for (Message messageObject : messages) {
			JsonObject convertedObject = new Gson().fromJson(messageObject.getBody(), JsonObject.class);
			sleep(1000);
			String messageText = convertedObject.get(MESSAGE).getAsString();
			JsonObject attributesJson = convertedObject.get(MESSAGE_ATTRIBUTES).getAsJsonObject();

			int productId = attributesJson.get(SNSMessage.PRODUCT_ID).getAsJsonObject().get(VALUE).getAsInt();
			int wwstock = attributesJson.get(SNSMessage.WORLD_WIDE_STOCK).getAsJsonObject().get(VALUE).getAsInt();

			logger.info("Read: " + messageText);
			logger.info(SNSMessage.PRODUCT_ID + ": " + productId);
			logger.info(SNSMessage.WORLD_WIDE_STOCK + ": " + wwstock);

			deleteMessage(messageObject);
		}
	}

	private void deleteMessage(Message messageObject) {

		final String messageReceiptHandle = messageObject.getReceiptHandle();
		amazonSQS.deleteMessage(new DeleteMessageRequest(sqsUrl, messageReceiptHandle));
//		logger.info("Message was deleted: " + messageObject);
	}

	private void sleep(int numberOfMilliSecconds) {
		try {
			Thread.sleep(numberOfMilliSecconds);
		} catch (InterruptedException e) {
			logger.info(e.getStackTrace().toString());
		}
	}
}
