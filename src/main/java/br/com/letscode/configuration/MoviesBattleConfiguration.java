package br.com.letscode.configuration;

import static br.com.letscode.commons.Constant.BASE_PACKAGE;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableJpaRepositories(basePackages = { BASE_PACKAGE })
@EntityScan(basePackages = { BASE_PACKAGE })
@ComponentScan(basePackages = { BASE_PACKAGE })
public class MoviesBattleConfiguration {

	public static final String AUTHENTICATION_TASK_EXECUTOR = "authentication_task_executor";

	@Bean(name = AUTHENTICATION_TASK_EXECUTOR)
	public TaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(50);
		executor.setThreadNamePrefix("authentication:");
		executor.initialize();
		return executor;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		final ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setDestinationNameTokenizer((name, nameableType) -> new String[] { name });
		mapper.getConfiguration().setSourceNameTokenizer((name, nameableType) -> new String[] { name });
		return mapper;
	}

	@Bean
	@Primary
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Logger logger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
	}

}
