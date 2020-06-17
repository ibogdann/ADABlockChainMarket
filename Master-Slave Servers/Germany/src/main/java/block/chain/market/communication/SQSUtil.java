package block.chain.market.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.amazonaws.services.sqs.model.SendMessageResult;
//import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import block.chain.market.orders.OrderController;
//import block.chain.market.orders.OrderModelAssembler;
//import block.chain.market.orders.OrderRepository;
import block.chain.market.products.Product;
import block.chain.market.products.ProductController;
//import block.chain.market.products.ProductModelAssembler;
//import block.chain.market.products.ProductRepository;
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
    
    @Autowired
	private OrderMessageCompiler messageCompiler;
    
    @Autowired
    private ProductController productController;

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
        
        this.messageCompiler = new OrderMessageCompiler();
     
    }

    public void sendSQSMessage(T message) throws JsonProcessingException {

        log.info("Sending SQS message: " + message);

        SendMessageResult result = this.amazonSQS.sendMessage(this.sqsUrl, this.objectMapper.writeValueAsString(message));

        log.info("SQS Message ID: " + result.getMessageId());
    }
    
    public void startListeningToMessages() {
    	
    	final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl)
                .withMaxNumberOfMessages(1)
                .withWaitTimeSeconds(3);
    	
    	while (true) {

            final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
            List<Product> productList = new ArrayList<Product>();
            List<Product> availableProducts = productController.getProducts();
            List<Integer> quantities = new ArrayList<Integer>();
            Map<Product, Integer> orderMap = null;
            
            for (Message messageObject : messages) {
            	
            	String message = messageObject.getBody();
            	log.info("message: " + message);
            	log.info("Available products: " + availableProducts);
            	orderMap = messageCompiler.decompile(message, availableProducts);
            	log.info("Order map: " + orderMap.size());
            	for(Map.Entry<Product, Integer> entry: orderMap.entrySet()) {
            		Product product = entry.getKey();
            		Integer quantity = entry.getValue();
            		log.info("Product: " + product.getName() + " Quantity: " + quantity);
            		productList.add(product);
            		quantities.add(quantity);
            	}
            	
            	deleteMessage(messageObject);
            	orderController.foreignOrder(productList, quantities);
            	
//            	try {
//            		productList = this.objectMapper.readValue(message, new TypeReference<List<Product>>(){});
//            		log.info("Received productList: " + productList);
//            		
//            	}catch(Exception e1) {
//            		
//            		try {
//	            		quantities = this.objectMapper.readValue(message, new TypeReference<List<Integer>>(){});
//	            		log.info("Received quantities: " + quantities);
//	            		
//            		}catch(Exception e2) {
//            			log.info("Delete unknown message type: " + message);
//            			deleteMessage(messageObject);
//                		continue;
//            		}
//            	}
//	            
//	            
//	            
//	            log.info("productList before func call: " + productList);
//                log.info("Quantities before func call: " + quantities);
//                
            
            }
    	}
    }
    
    private void deleteMessage(Message message) {

        final String messageReceiptHandle = message.getReceiptHandle();
        amazonSQS.deleteMessage(new DeleteMessageRequest(sqsUrl, messageReceiptHandle));

    }
}