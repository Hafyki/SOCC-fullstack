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

/**
 * Seeder para mockar dados de permissões e perfis no banco de dados para fins de teste, já que não estamos responsáveis pelo CRUD destes dados.
 * (Salva esses dados apenas uma vez, quando roda a aplicação a primeira vez. Da segunda vez em diante, quando vê que tem dados nas tabelas, não duplica os dados. Isto é, não cria nada.)
 */

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
            Permission manageCourses = new Permission("MANAGE_COURSES", "Gerenciar cursos", "Permite criar, editar e excluir cursos");
            Permission viewCourses = new Permission("VIEW_COURSES", "Visualizar cursos", "Permite visualizar cursos disponíveis");
            Permission manageGrades = new Permission("MANAGE_GRADES", "Gerenciar notas", "Permite inserir e editar notas dos alunos");
            Permission viewGrades = new Permission("VIEW_GRADES", "Visualizar notas", "Permite visualizar suas próprias notas");
            Permission viewReports = new Permission("VIEW_REPORTS", "Visualizar relatórios", "Permite visualizar relatórios do sistema");
            Permission academicRecords = new Permission("ACADEMIC_RECORDS", "Registros acadêmicos", "Permite gerenciar registros e documentos acadêmicos");
            Permission systemAdmin = new Permission("SYSTEM_ADMIN", "Administração do sistema", "Acesso total ao sistema");
            Permission assignProfile = new Permission("ASSIGN_PROFILE_USER", "Atribuir perfil ao usuário", "Permite atribuir ou alterar perfis de acesso aos usuários");
            Permission alterStatus = new Permission("ALTER_STATUS_USER", "Alterar status do usuário", "Permite alterar status dos usuários (ativo/suspenso)");
            Permission setMinWorkload = new Permission("SET_MIN_WORKLOAD_DOCENT", "Definir carga horária mínima do docente", "Permite configurar a carga horária mínima exigida para um docente");
            Permission exportData = new Permission("EXPORT_USERS_DATA", "Exportar dados dos usuários", "Permite exportar dados dos usuários sistema em formatos disponíveis");


            if (permissionRepository.count() == 0) {
                permissionRepository.saveAll(List.of(
                        teachClasses, userView, userCreate, userEdit, userDelete, enrollClass,
                        manageCourses, viewCourses, manageGrades, viewGrades,
                        viewReports, academicRecords, systemAdmin, assignProfile,
                        alterStatus, setMinWorkload, exportData
                ));
            }

            // Cria perfis com permissões que fazem sentido
            Profile administrator = Profile.builder()
                    .role(ProfileRole.ADMINISTRATOR)
                    .description("Administrador do sistema com todos os acessos")
                    .protectedProfile(true)
                    .permissions(List.of(userView, userCreate, userEdit, userDelete,
                            manageCourses, viewCourses, manageGrades, viewGrades,
                            viewReports, academicRecords, systemAdmin, assignProfile,
                            alterStatus, setMinWorkload, exportData))
                    .build();

            Profile courseCoordinator = Profile.builder()
                    .role(ProfileRole.COURSE_COORDINATOR)
                    .description("Coordenador de curso")
                    .protectedProfile(true)
                    .permissions(List.of(userView, manageCourses, viewCourses, manageGrades, viewReports, academicRecords))
                    .build();

            Profile assistantCoordinator = Profile.builder()
                    .role(ProfileRole.ASSISTANT_COURSE_COORDINATOR)
                    .description("Coordenador assistente de curso")
                    .protectedProfile(true)
                    .permissions(List.of(userView, manageCourses, viewCourses, manageGrades, viewReports, academicRecords))
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
                    .permissions(List.of(academicRecords, viewCourses, viewGrades, userView))
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
                    .permissions(List.of(userView, userCreate, userEdit, assignProfile,
                            alterStatus, setMinWorkload, exportData, manageCourses,
                            viewCourses, viewReports, academicRecords))
                    .build();

            Profile deputyDirector = Profile.builder()
                    .role(ProfileRole.DEPUTY_DIRECTOR)
                    .description("Vice-diretor da instituição")
                    .protectedProfile(true)
                    .permissions(List.of(userView, manageCourses, viewCourses, viewReports,
                            assignProfile, alterStatus, setMinWorkload, exportData))
                    .build();

            Profile boardMember = Profile.builder()
                    .role(ProfileRole.BOARD_MEMBER)
                    .description("Membro do conselho")
                    .protectedProfile(true)
                    .permissions(List.of(viewReports, viewCourses))
                    .build();

            if (profileRepository.count() == 0) {
                profileRepository.saveAll(List.of(
                        administrator, courseCoordinator, assistantCoordinator, professor,
                        academicSecretary, technicalStaff, student, pedagogicalCurator, director,
                        deputyDirector, boardMember
                ));
            }
        };
    }
}