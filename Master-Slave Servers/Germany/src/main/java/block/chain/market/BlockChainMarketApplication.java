package block.chain.market;


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import block.chain.market.config.ServerConfiguration;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//
//import block.chain.market.communication.SQSUtil;
//import block.chain.market.products.Product;

import web.service.ProducingWebServiceApplication;


@SpringBootApplication
//@ConfigurationPropertiesScan("block.chain.market.config")
//@EnableConfigurationProperties(ServerConfiguration.class)
public class BlockChainMarketApplication{

//	@Autowired
//    private SQSUtil<Product> sqsUtil;
	
	public static void main(String[] args) {
		Class<?>[] sources = {BlockChainMarketApplication.class, ProducingWebServiceApplication.class};
		SpringApplication.run(sources, args);

	}
	
//	@EventListener(ApplicationReadyEvent.class)
//    public void listenForMessages(String... args) throws Exception {
//        sqsUtil.startListeningToMessages();
//	}

}
