package block.chain.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import block.chain.market.config.ServerConfiguration;

import block.chain.market.communication.SQSUtil;
import block.chain.market.products.Product;

@SpringBootApplication
//@ConfigurationPropertiesScan("block.chain.market.config")
//@EnableConfigurationProperties(ServerConfiguration.class)
public class BlockChainMarketApplication implements CommandLineRunner{

	@Autowired
    private SQSUtil<Product> sqsUtil;
	
	public static void main(String[] args) {
		SpringApplication.run(BlockChainMarketApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {

        sqsUtil.startListeningToMessages();
	}

}
