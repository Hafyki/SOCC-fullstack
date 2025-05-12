package com.project.socc.seeder;

import com.project.socc.entities.Permission;
import com.project.socc.entities.Profile;
import com.project.socc.entities.User;
import com.project.socc.enums.ProfileRole;
import com.project.socc.enums.UserStatus;
import com.project.socc.repositories.PermissionRepository;
import com.project.socc.repositories.ProfileRepository;
import com.project.socc.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            PermissionRepository permissionRepository,
            ProfileRepository profileRepository,
            UserRepository userRepository
    ) {
        return args -> {

            // Cria permissões
            Permission perm1 = new Permission("TEACH_CLASSES", "Ministrar turmas", "Essa permissão é para ministrar turmas");
            Permission perm2 = new Permission("USER_VIEW", "Visualizar usuários", "Essa é permissão é para visualizar todos os usuários do sistema");
            Permission perm3 = new Permission("ENROLL_CLASS", "Matricular-se em uma turma", "Essa permissão é para se matricular se em uma turma");
            Permission perm4 = new Permission("ALTER_STATUS_USER", "Alterar status do usuário", "Essa permissão é para alterar o status dos usuários");

            permissionRepository.saveAll(List.of(perm1, perm2, perm3, perm4));

            // Cria perfis
            Profile director = Profile.builder()
                    .role(ProfileRole.DIRECTOR)
                    .description("Perfil de diretor do INF")
                    .protectedProfile(true)
                    .permissions(List.of(perm2, perm4))
                    .build();

            Profile professor = Profile.builder()
                    .role(ProfileRole.PROFESSOR)
                    .description("Perfil de professor do INF")
                    .protectedProfile(false)
                    .permissions(List.of(perm1))
                    .build();

            Profile student = Profile.builder()
                    .role(ProfileRole.STUDENT)
                    .description("Perfil de aluno do INF")
                    .protectedProfile(false)
                    .permissions(List.of(perm3))
                    .build();

            profileRepository.saveAll(List.of(director, professor, student));

            // Cria usuários
            User user1 = User.builder()
                    .username("joao123")
                    .name("João da Silva")
                    .email("joao@email.com")
                    .phone("11999999999")
                    .workload(64)
                    .status(UserStatus.ACTIVE)
                    .profiles(List.of(director, professor))
                    .build();

            User user2 = User.builder()
                    .username("maria123")
                    .name("Maria Santos")
                    .email("maria@email.com")
                    .phone("21999999999")
                    .status(UserStatus.SUSPENDED)
                    .profiles(List.of(student))
                    .build();

            userRepository.saveAll(List.of(user1, user2));
        };
    }
}
