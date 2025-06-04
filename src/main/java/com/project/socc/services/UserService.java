package com.project.socc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socc.entities.User;
import com.project.socc.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
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

    public Page<User> findUsersPaged(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
    }

    public User updateUser(UUID id, Map<String, Object> fields) {
        User user = userRepository.findById(id).get();
        merge(fields, user);
        return userRepository.save(user);
    }

    public void merge(Map<String, Object> fields, User user) {
        ObjectMapper mapper = new ObjectMapper();
        User mappedUser = mapper.convertValue(fields, User.class); // Utilizado para que o Map consiga identificar os campos de atributos que são outras entidades(Profile e Permission) como objetos e não linkedHashMaps
        fields.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(User.class, propertyName);
            field.setAccessible(true);
            Object newPropertyValue = ReflectionUtils.getField(field, mappedUser);
            ReflectionUtils.setField(field, user, newPropertyValue);
        });
    }
}
