package block.chain.market;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import block.chain.market.communication.SQSUtil;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class StartMessageListener {

	@Bean
	CommandLineRunner startListener(SQSUtil<String> sqsUtil) {
		log.info("Started listening for sqs messages...");
		return args -> {
			sqsUtil.startListeningToMessages();
		};
	}
}
