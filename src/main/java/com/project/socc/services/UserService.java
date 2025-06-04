package com.project.socc.services;

import com.project.socc.dtos.UserUpdateRequestDTO;
import com.project.socc.entities.Profile;
import com.project.socc.entities.User;
import com.project.socc.enums.UserStatus;
import com.project.socc.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Autowired
    public UserService(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
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

    public User updateUser(UUID id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));

        if (userUpdateRequestDTO.getUsername() != null) {
            user.setUsername(userUpdateRequestDTO.getUsername());
        }

        if (userUpdateRequestDTO.getName() != null) {
            user.setName(userUpdateRequestDTO.getName());
        }

        if (userUpdateRequestDTO.getEmail() != null) {
            user.setEmail(userUpdateRequestDTO.getEmail());
        }

        if (userUpdateRequestDTO.getPhone() != null) {
            user.setPhone(userUpdateRequestDTO.getPhone());
        }

        if (userUpdateRequestDTO.getWorkload() != null) {
            user.setWorkload(userUpdateRequestDTO.getWorkload());
        }

        if (userUpdateRequestDTO.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(userUpdateRequestDTO.getStatus()));
        }

        if (userUpdateRequestDTO.getProfileIds() != null) {
            updateUserProfiles(user, userUpdateRequestDTO.getProfileIds());
        }

        return userRepository.save(user);
    }

    private void updateUserProfiles(User user, List<Long> profileIds) {

        // Limpa a lista atual
        user.getProfiles().clear();

        profileIds.forEach(profileId -> {
            Profile profile = profileService.findProfileById(profileId);
            user.addProfile(profile);
        });
    }
}
