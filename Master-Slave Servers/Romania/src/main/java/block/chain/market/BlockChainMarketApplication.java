package block.chain.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import block.chain.market.config.ServerConfiguration;

import web.service.ProducingWebServiceApplication;

@SpringBootApplication
//@ConfigurationPropertiesScan("block.chain.market.config")
//@EnableConfigurationProperties(ServerConfiguration.class)
public class BlockChainMarketApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BlockChainMarketApplication.class, args);
		
		Class<?>[] sources = {BlockChainMarketApplication.class, ProducingWebServiceApplication.class};
		SpringApplication.run(sources, args);
	}

}
