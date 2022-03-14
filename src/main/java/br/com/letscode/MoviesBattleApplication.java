package br.com.letscode;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "br.com.letscode" })
public class MoviesBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleApplication.class, args);
	}

}
