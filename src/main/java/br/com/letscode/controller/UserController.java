package br.com.letscode.controller;

import br.com.letscode.commons.ResponseObject;
import br.com.letscode.controller.model.user.UserRequest;
import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "users")
@PreAuthorize("hasRole('authentication.crud.user')")
public class UserController {

	private final UserService service;

	@Autowired
	private ModelMapper modelMapper;


	public UserController(final UserService service) {
		super();
		this.service = service;
	}

	@PostMapping
	public ResponseObject<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
		User user = modelMapper.map(userRequest, User.class);
		final UserResponse response = service.create(user);
		return ResponseObject.of(response);
	}

}
