package com.project.socc.seeder;

import com.project.socc.entities.Permission;
import com.project.socc.entities.Profile;
import com.project.socc.enums.ProfileRole;
import com.project.socc.repositories.PermissionRepository;
import com.project.socc.repositories.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            PermissionRepository permissionRepository,
            ProfileRepository profileRepository
    ) {
        return args -> {

            // Cria permissões
            Permission teachClasses = new Permission("TEACH_CLASSES", "Ministrar turmas", "Permite ministrar aulas e gerenciar turmas");
            Permission userView = new Permission("USER_VIEW", "Visualizar usuários", "Permite visualizar todos os usuários do sistema");
            Permission userCreate = new Permission("USER_CREATE", "Criar usuários", "Permite criar novos usuários no sistema");
            Permission userEdit = new Permission("USER_EDIT", "Editar usuários", "Permite editar dados dos usuários");
            Permission userDelete = new Permission("USER_DELETE", "Excluir usuários", "Permite excluir usuários do sistema");
            Permission enrollClass = new Permission("ENROLL_CLASS", "Matricular-se em turma", "Permite se matricular em turmas disponíveis");
            Permission alterStatus = new Permission("ALTER_STATUS_USER", "Alterar status do usuário", "Permite alterar status dos usuários (ativo/suspenso)");
            Permission manageCourses = new Permission("MANAGE_COURSES", "Gerenciar cursos", "Permite criar, editar e excluir cursos");
            Permission viewCourses = new Permission("VIEW_COURSES", "Visualizar cursos", "Permite visualizar cursos disponíveis");
            Permission manageGrades = new Permission("MANAGE_GRADES", "Gerenciar notas", "Permite inserir e editar notas dos alunos");
            Permission viewGrades = new Permission("VIEW_GRADES", "Visualizar notas", "Permite visualizar suas próprias notas");
            Permission viewReports = new Permission("VIEW_REPORTS", "Visualizar relatórios", "Permite visualizar relatórios do sistema");
            Permission academicRecords = new Permission("ACADEMIC_RECORDS", "Registros acadêmicos", "Permite gerenciar registros e documentos acadêmicos");
            Permission systemAdmin = new Permission("SYSTEM_ADMIN", "Administração do sistema", "Acesso total ao sistema");

            permissionRepository.saveAll(List.of(
                    teachClasses, userView, userCreate, userEdit, userDelete, enrollClass,
                    alterStatus, manageCourses, viewCourses, manageGrades, viewGrades,
                    viewReports, academicRecords, systemAdmin
            ));

            // Cria perfis com permissões que fazem sentido
            Profile administrator = Profile.builder()
                    .role(ProfileRole.ADMINISTRATOR)
                    .description("Administrador do sistema com todos os acessos")
                    .protectedProfile(true)
                    .permissions(List.of(systemAdmin, userView, userCreate, userEdit, userDelete, alterStatus,
                            manageCourses, viewCourses, viewReports, academicRecords))
                    .build();

            Profile courseCoordinator = Profile.builder()
                    .role(ProfileRole.COURSE_COORDINATOR)
                    .description("Coordenador de curso")
                    .protectedProfile(true)
                    .permissions(List.of(userView, manageCourses, viewCourses, manageGrades, viewReports))
                    .build();

            Profile assistantCoordinator = Profile.builder()
                    .role(ProfileRole.ASSISTANT_COURSE_COORDINATOR)
                    .description("Coordenador assistente de curso")
                    .protectedProfile(true)
                    .permissions(List.of(viewCourses, manageGrades, userView))
                    .build();

            Profile professor = Profile.builder()
                    .role(ProfileRole.PROFESSOR)
                    .description("Professor da instituição")
                    .protectedProfile(false)
                    .permissions(List.of(teachClasses, viewCourses, manageGrades, viewGrades))
                    .build();

            Profile academicSecretary = Profile.builder()
                    .role(ProfileRole.ACADEMIC_SECRETARY)
                    .description("Secretário acadêmico")
                    .protectedProfile(true)
                    .permissions(List.of(academicRecords, userView, userCreate, userEdit, viewCourses))
                    .build();

            Profile technicalStaff = Profile.builder()
                    .role(ProfileRole.TECHNICAL_PEDAGOGICAL_STAFF)
                    .description("Equipe técnico-pedagógica")
                    .protectedProfile(false)
                    .permissions(List.of(viewCourses, viewGrades, userView))
                    .build();

            Profile student = Profile.builder()
                    .role(ProfileRole.STUDENT)
                    .description("Estudante da instituição")
                    .protectedProfile(false)
                    .permissions(List.of(enrollClass, viewGrades, viewCourses))
                    .build();

            Profile pedagogicalCurator = Profile.builder()
                    .role(ProfileRole.PEDAGOGICAL_CURATOR)
                    .description("Curador pedagógico")
                    .protectedProfile(false)
                    .permissions(List.of(manageCourses, viewCourses, viewReports))
                    .build();

            Profile director = Profile.builder()
                    .role(ProfileRole.DIRECTOR)
                    .description("Diretor da instituição")
                    .protectedProfile(true)
                    .permissions(List.of(userView, userCreate, userEdit, alterStatus, manageCourses,
                            viewCourses, viewReports, academicRecords))
                    .build();

            Profile deputyDirector = Profile.builder()
                    .role(ProfileRole.DEPUTY_DIRECTOR)
                    .description("Vice-diretor da instituição")
                    .protectedProfile(true)
                    .permissions(List.of(userView, manageCourses, viewCourses, viewReports))
                    .build();

            Profile boardMember = Profile.builder()
                    .role(ProfileRole.BOARD_MEMBER)
                    .description("Membro do conselho")
                    .protectedProfile(true)
                    .permissions(List.of(viewReports, viewCourses))
                    .build();

            profileRepository.saveAll(List.of(
                    administrator, director, deputyDirector, courseCoordinator, assistantCoordinator,
                    professor, academicSecretary, technicalStaff, student, pedagogicalCurator, boardMember
            ));
        };
    }
}