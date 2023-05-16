package dev.muriloabranches.avaliacao.service;

import dev.muriloabranches.avaliacao.controller.request.CreateUserRequest;
import dev.muriloabranches.avaliacao.entity.User;

import java.util.Optional;

public interface UserService {

    User create(CreateUserRequest request);

    Optional<User> findById(Long id);
}
