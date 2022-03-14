package br.com.letscode.token.service;

import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.token.entity.Privilege;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	public UserResponse create(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setPrivileges(new ArrayList<>());
		final User newUser = this.userRepository.save(user);
		return modelMapper.map(newUser, UserResponse.class);
	}

	@Transactional
	public void createUserIfNotFound(Long id, String username, String password, String name ,Privilege... privileges) {
		final User user = this.userRepository.findById(id).orElse(new User());
		if (user.getId() == null) {
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name);
			user.setPrivileges(new ArrayList<>());
			Arrays.asList(privileges).forEach(privilege -> user.getPrivileges().add(privilege));
			this.userRepository.save(user);
		}
	}

}
