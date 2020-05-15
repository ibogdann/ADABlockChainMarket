package com.contactsunny.poc.AmazonSQSConsumerPOC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.contactsunny.poc.AmazonSNSPublisherPOC.models.SNSMessage;
import com.contactsunny.poc.AmazonSQSConsumerPOC.utils.SNSUtil;
import com.contactsunny.poc.AmazonSQSConsumerPOC.utils.SQSUtil;

@SpringBootApplication
@EnableAsync
public class App implements CommandLineRunner {

	@Autowired
	private SQSUtil sqsUtil;
	@Autowired
    private SNSUtil snsUtil;
	private ExecutorService executor;

	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
   
	@Override
    public void run(String... args) throws Exception {
		
		final SNSMessage message = new SNSMessage("Message to be deleted");
		message.addAttribute(SNSMessage.PRODUCT_ID, 16);
		message.addAttribute(SNSMessage.WORLD_WIDE_STOCK, -7);
		this.snsUtil.publishSNSMessage(message);

		executor = Executors.newSingleThreadExecutor();	
		executor.submit(() -> {
			while(true) {
				sqsUtil.readMessages();
			}
		});
		
    }
	
}
