package com.project.socc.services;

import com.project.socc.entities.User;
import com.project.socc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Page<User> getAllUsersPaged(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }
}
