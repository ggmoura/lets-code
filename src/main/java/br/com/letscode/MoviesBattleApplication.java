package br.com.letscode;

import static br.com.letscode.commons.Constant.BASE_PACKAGE;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@EnableAutoConfiguration
@ComponentScan(basePackages = { BASE_PACKAGE })
public class MoviesBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleApplication.class, args);
	}

}
