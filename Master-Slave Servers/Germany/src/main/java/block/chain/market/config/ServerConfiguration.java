package block.chain.market.config;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration
@Component
//@ConfigurationProperties("server")
public class ServerConfiguration {
//	@Value("${server.location}")
//	private static String location;
	private static String location = "Germany";

	public static String getLocation() {
		return location;
	}

	public static void setLocation(String location) {
		ServerConfiguration.location = location;
	}
}
