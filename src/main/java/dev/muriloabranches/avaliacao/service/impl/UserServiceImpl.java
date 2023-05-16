package dev.muriloabranches.avaliacao.service.impl;

import dev.muriloabranches.avaliacao.controller.request.CreateUserRequest;
import dev.muriloabranches.avaliacao.entity.User;
import dev.muriloabranches.avaliacao.repository.UserRepository;
import dev.muriloabranches.avaliacao.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User create(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
