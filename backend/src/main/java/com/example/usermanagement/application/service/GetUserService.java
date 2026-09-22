package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.UserNotFoundException;
import com.example.usermanagement.application.usecase.GetUserUseCase;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetUserService implements GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
