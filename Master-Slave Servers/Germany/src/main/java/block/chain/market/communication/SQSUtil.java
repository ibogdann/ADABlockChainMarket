package block.chain.market.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import block.chain.market.orders.OrderController;
import block.chain.market.orders.OrderModelAssembler;
import block.chain.market.orders.OrderRepository;
import block.chain.market.products.Product;
import block.chain.market.products.ProductModelAssembler;
import block.chain.market.products.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SQSUtil<T> {

    @Value("${sqs.url}")
    private String sqsUrl;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;
    
    @Autowired
	private OrderController orderController;

    private AmazonSQS amazonSQS;
    
    private ObjectMapper objectMapper;


    @PostConstruct
    private void postConstructor() {

        log.info("SQS URL: " + sqsUrl);
        log.info("Region: " + awsRegion);

        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey)
        );

        this.amazonSQS = AmazonSQSClientBuilder.standard()
        					.withRegion(this.awsRegion)
        					.withCredentials(awsCredentialsProvider)
        					.build();
        
        this.objectMapper = new ObjectMapper();
     
    }

    public void sendSQSMessage(T message) throws JsonProcessingException {

        log.info("Sending SQS message: " + message);

        SendMessageResult result = this.amazonSQS.sendMessage(this.sqsUrl, this.objectMapper.writeValueAsString(message));

        log.info("SQS Message ID: " + result.getMessageId());
    }
    
    public void startListeningToMessages() throws JsonParseException, JsonMappingException, IOException {
    	
    	final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl)
                .withMaxNumberOfMessages(2)
                .withWaitTimeSeconds(3);
    	
    	while (true) {

            final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
            List<Product> productList = null;
            List<Integer> quantities = null;
            
            if(messages.size() == 2) {

	            for (Message messageObject : messages) {
	            	
	            	String message = messageObject.getBody();
	            	
	            	try {
	            		productList = this.objectMapper.readValue(message, new TypeReference<List<Product>>(){});
	            		log.info("Received productList: " + productList);
	            		
	            	}catch(Exception e1) {
	            		
	            		try {
		            		quantities = this.objectMapper.readValue(message, new TypeReference<List<Integer>>(){});
		            		log.info("Received quantities: " + quantities);
		            		
	            		}catch(Exception e2) {
	            			log.info("Delete unknown message type: " + message);
	            			deleteMessage(messageObject);
	                		continue;
	            		}
	            	}
	            }
	            
	            for (Message messageObject : messages) {
	            	deleteMessage(messageObject);
	            }
	            
	            log.info("productList before func call: " + productList);
                log.info("Quantities before func call: " + quantities);
                
                orderController.foreignOrder(productList, quantities);
            
            }
    	}
    }
    
    private void deleteMessage(Message message) {

        final String messageReceiptHandle = message.getReceiptHandle();
        amazonSQS.deleteMessage(new DeleteMessageRequest(sqsUrl, messageReceiptHandle));

    }
}