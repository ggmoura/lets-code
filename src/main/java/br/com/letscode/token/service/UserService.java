package br.com.letscode.token.service;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.exception.BusinessException;
import br.com.letscode.token.entity.Privilege;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
		Optional<User> existsUser = userRepository.findByUsername(user.getUsername());
		if (existsUser.isEmpty()) {
			user.setPassword(encoder.encode(user.getPassword()));
			user.setPrivileges(new ArrayList<>());
			user.getPrivileges().add(Privilege.PLAYER);
			user.setTotalSteps(0);
			user.setTotalScore(0);
			final User newUser = this.userRepository.save(user);
			return modelMapper.map(newUser, UserResponse.class);
		} else {
			throw new BusinessException(ResponseMessage.error("user {0} already exists", user.getUsername()));
		}
	}

	@Transactional
	public void createUserIfNotFound(Long id, String username, String password, String name ,Privilege... privileges) {
		final User user = this.userRepository.findById(id).orElse(new User());
		if (user.getId() == null) {
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name);
			user.setTotalSteps(0);
			user.setTotalScore(0);
			user.setPrivileges(new ArrayList<>());
			Arrays.asList(privileges).forEach(privilege -> user.getPrivileges().add(privilege));
			this.userRepository.save(user);
		}
	}

	public UserResponse findByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new BusinessException(ResponseMessage.error("User {0} not found", username)));
		return modelMapper.map(user, UserResponse.class);
	}

    public List<UserResponse> findAll(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		List<UserResponse> dtos = users.stream().map(user -> modelMapper.map(user, UserResponse.class))
				.collect(Collectors.toList());
		return dtos;
    }
}
