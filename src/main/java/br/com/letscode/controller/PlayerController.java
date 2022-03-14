package br.com.letscode.controller;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import br.com.letscode.controller.model.user.UserRequest;
import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "players")
public class PlayerController {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper mapper;

    @RolesAllowed("MANAGER")
    @GetMapping
    public ResponseObject<UserResponse> find(@Parameter(hidden = true) Pageable pageable) {
        return ResponseObject.page(service.findAll(pageable));
    }

    @GetMapping("/{username}")
    @PostAuthorize("#username == authentication.principal.username")
    public ResponseObject<UserResponse> findByUsername(@PathVariable(value = "username") String username) {
        UserResponse response = service.findByUsername(username);
        return ResponseObject.of(response);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseObject<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        User player = mapper.map(userRequest, User.class);
        UserResponse response = service.create(player);
        return ResponseObject.of(response, ResponseMessage.success("Player {0} saved", response.getName()));
    }

}
