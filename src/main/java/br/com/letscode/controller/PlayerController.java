package br.com.letscode.controller;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import br.com.letscode.controller.model.user.UserRequest;
import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "players")
public class PlayerController {

    @Autowired
    private UserService service;

    private ModelMapper mapper;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseObject<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        User player = mapper.map(userRequest, User.class);
        UserResponse response = service.create(player);
        return ResponseObject.of(response, ResponseMessage.success("Player {0} saved", response.getName()));
    }


}
