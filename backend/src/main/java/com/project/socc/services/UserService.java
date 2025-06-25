package com.project.socc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.socc.dtos.UserUpdateRequestDTO;
import com.project.socc.entities.Profile;
import com.project.socc.entities.User;
import com.project.socc.enums.UserStatus;
import com.project.socc.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

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

    public Page<User> findUsersByUsername(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }


    public User updateUser(UUID id, UserUpdateRequestDTO userRequest) {

        // Antes de tudo, verifica se existe usuário com tal ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));

        // Só vai atualizar os campos que aparecem no DTO
        if (userRequest.getUsername() != null) {
            user.setUsername(userRequest.getUsername());
        }

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }

        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
        }

        if (userRequest.getWorkload() != null) {
            user.setWorkload(userRequest.getWorkload());
        }

        if (userRequest.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(userRequest.getStatus()));
        }

        if (userRequest.getProfileIds() != null) {
            updateUserProfiles(user, userRequest.getProfileIds());
        }

        // Salva as alterações feitas no usuário
        return userRepository.save(user);
    }

    /**
     * Método auxiliar para adicionar e remover perfis associados a um usuário (atualizar os perfis associados a um usuário)
     * Pré-requisito: O front-end deve enviar a lista completa de perfis marcados
     */

    private void updateUserProfiles(User user, List<Long> profileIds) {

        // Limpa a lista atual de perfis desse usuário para garantir que apenas os perfis informados na requisição permaneçam vinculados ao usuário
        user.getProfiles().clear();

        // Para cada perfil da lista, busca o perfil no banco pelo seu ID e adiciona na lista de perfis do usuário
        profileIds.forEach(profileId -> {
            Profile profile = profileService.findProfileById(profileId);
            user.addProfile(profile);
        });
    }
}
