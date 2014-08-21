package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClientApplication.class, args);
	}
}
