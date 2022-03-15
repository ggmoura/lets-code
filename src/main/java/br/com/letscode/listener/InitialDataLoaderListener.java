package br.com.letscode.listener;

import br.com.letscode.token.entity.Privilege;
import br.com.letscode.token.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialDataLoaderListener {

	private static final String DEFAULT_USEER_CREDENTIAL = "123456";

	@Autowired
	private UserService userService;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Value(value = "${letscode.authentication.mustbeinitialize.data:true}")
	private boolean mustBeInitialize;


	@EventListener
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (this.mustBeInitialize) {
			updateDefaultAccounts();
			this.mustBeInitialize = Boolean.FALSE;
		}
	}

	private void updateDefaultAccounts() {
		Privilege manager = Privilege.MANAGER;
		Privilege player = Privilege.PLAYER;
		String defaultPass = encoder.encode(DEFAULT_USEER_CREDENTIAL);
		taskExecutor.execute(() -> {
			this.userService.createUserIfNotFound(1L, "admin", defaultPass, "Gleidson", manager);
			this.userService.createUserIfNotFound(2L, "player", defaultPass, "Aline", player);
			this.userService.createUserIfNotFound(3L, "sophia", defaultPass, "Maria Sophia", player, manager);
			this.userService.createUserIfNotFound(4L, "maite", defaultPass, "Maitê", player);
			this.userService.createUserIfNotFound(4L, "marta", defaultPass, "Marta Silva", player);
			this.userService.createUserIfNotFound(5L, "davi", defaultPass, "Davi", player);
		});
	}

}