# Sistema de Oferta de Componentes Curriculares (SOCC)
Repositório destinado ao Trabalho Incremental do Grupo 5 da disciplina de Desenvolvimento FullStack.

## 📌 Membros
* Gustavo Neves Piedade Louzada
* Hafy Mourad Jacoub de Cuba Kouzak
* Igor Rodrigues Castilho
* João Victor de Paiva Albuquerque
* Maria Eduarda de Campos Ramos

## 📌 Caso de Uso: Manter Usuário
* Listar dados dos usuários (FP)
* Atribuir perfil ao usuário (FA01)
* Alterar status do usuário (FA02)

## 📌 Diagrama de Classes

```mermaid
classDiagram
direction TB
	namespace enums {
        class UserStatus {
            + ACTIVE
            + SUSPENDED
        }

        class ProfileRole {
            + ADMINISTRATOR
            + COURSE_COORDINATOR
            + ASSISTANT_COURSE_COORDINATOR
            + PROFESSOR
            + ACADEMIC_SECRETARY
            + TECHNICAL_PEDAGOGICAL_STAFF
            + STUDENT
            + PEDAGOGICAL_CURATOR
            + DIRECTOR
            + DEPUTY_DIRECTOR
            + BOARD_MEMBER
        }
	}

    class User {
	    - id: UUID
	    - username: String
	    - name: String
	    - email: String
	    - phone: String
	    - workload: Integer
	    - status: UserStatus
	    - profiles: ArrayList~Profile~
    }
    class Profile {
	    - id: UUID
	    - role: ProfileRole
	    - description: String
	    - protectedProfile: boolean
	    - permissions: ArrayList~Permission~
    }
    
    class Permission {
        - code: String
        - title: String
        - description: String
    }
    
	<<enumeration>> UserStatus
	<<enumeration>> ProfileRole

    User "*" --> "*" Profile : has
    Profile "*" --> "*" Permission : contains
```

## 📌 Diagrama do Banco de Dados
```mermaid
erDiagram
    PERMISSION {
        varchar(255) code PK
        varchar(255) title
        varchar(255) description
    }

    PROFILE_PERMISSION {
        uuid profile_id FK
        varchar(255) permission_code FK
    }

    PROFILE {
        uuid id PK
        varchar(255) description
        bool protected_profile
        varchar(255) role
    }

    USER_PROFILE {
        uuid user_id FK
        uuid profile_id FK
    }

    USER {
        uuid id PK
        varchar(255) email
        varchar(100) name
        varchar(11) phone
        varchar(255) status
        varchar(20) username
        int4 workload
    }

    PERMISSION ||--|{ PROFILE_PERMISSION : contained
    PROFILE ||--|{ PROFILE_PERMISSION : contains
    PROFILE ||--|{ USER_PROFILE : contained
    USER ||--|{ USER_PROFILE : contained
```


## 📌 Rotas
Considerando nosso caso de uso, teríamos os seguintes métodos HTTP:
* **POST /users:** Criar um novo usuário
* **GET /users:** Resgatar todos os dados de todos os usuários
* **GET /users/id:** Resgatar os dados de um usuário específico
* **PATCH /users/id:** Vai atualizar parcialmente os dados de um usuário, mais especificamente os perfis atrelados a ele e o seu status

> Os grupos responsáveis pela manutenção do perfil (Grupo 7) e pelo gerenciamento de permissões (Grupo 8) serão encarregados de implementar as rotas e operações CRUD das entidades relacionadas a Perfil e Permissões. O nosso foco será direcionado especificamente aos itens do nosso caso de uso principal: o Usuário.
