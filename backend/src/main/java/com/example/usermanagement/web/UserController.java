package com.example.usermanagement.web;

import com.example.usermanagement.application.usecase.*;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.web.dto.ApiResponse;
import com.example.usermanagement.web.dto.CreateUserRequest;
import com.example.usermanagement.web.dto.UpdateUserRequest;
import com.example.usermanagement.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Thin HTTP adapter: request/response mapping and delegation to use cases
 * only. No business rules live here.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
                           GetUserUseCase getUserUseCase,
                           ListUsersUseCase listUsersUseCase,
                           UpdateUserUseCase updateUserUseCase,
                           DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User created = createUserUseCase.createUser(new CreateUserCommand(request.fullName(), request.email()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(UserResponse.fromDomain(created), "User created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        User user = getUserUseCase.getUser(id);
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.fromDomain(user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> listUsers() {
        List<UserResponse> users = listUsersUseCase.listUsers().stream()
                .map(UserResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
                                                                 @Valid @RequestBody UpdateUserRequest request) {
        User updated = updateUserUseCase.updateUser(new UpdateUserCommand(id, request.fullName(), request.email()));
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.fromDomain(updated), "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "User deleted successfully"));
    }
}
