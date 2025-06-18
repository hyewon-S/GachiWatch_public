package ssd.springcooler.gachiwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@EnableAsync //로그인시 속도 향상을 위해 추가
public class GachiwatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachiwatchApplication.class, args);
	}

}
