package com.example.usermanagement.ports;

import com.example.usermanagement.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port describing the persistence operations the application layer
 * needs, independent of any specific storage technology.
 */
public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}
