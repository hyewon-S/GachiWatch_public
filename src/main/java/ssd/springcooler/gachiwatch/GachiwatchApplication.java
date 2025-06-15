package ssd.springcooler.gachiwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
public class GachiwatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachiwatchApplication.class, args);
	}

}
