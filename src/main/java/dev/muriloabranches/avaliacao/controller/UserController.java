package dev.muriloabranches.avaliacao.controller;

import dev.muriloabranches.avaliacao.controller.request.CreateUserRequest;
import dev.muriloabranches.avaliacao.controller.response.UserResponse;
import dev.muriloabranches.avaliacao.entity.User;
import dev.muriloabranches.avaliacao.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.getId(), user.getName()));
    }
}
