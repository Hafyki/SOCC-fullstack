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

## 📌 Frameworks
* Back-end: Spring Boot
* Front-end: Angular

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
	    - id: Long
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
        int8 profile_id FK
        varchar(255) permission_code FK
    }

    PROFILE {
        int8 id PK
        varchar(255) description
        bool protected_profile
        varchar(255) role
    }

    USER_PROFILE {
        uuid user_id FK
        int8 profile_id FK
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
    PROFILE ||--|{ USER_PROFILE : has
    USER ||--|{ USER_PROFILE : has
```

## 📌 Rotas
Considerando nosso caso de uso, teríamos os seguintes métodos HTTP:
* **POST /users:** Criar um novo usuário
* **GET /users:** Resgatar os dados de todos os usuários (com paginação)
* **GET /users/{id}:** Resgatar os dados de um usuário específico com base no ID
* **GET /users/search?username={username}:** Resgatar os dados de usuários com base no username
* **PATCH /users/{id}:** Vai atualizar parcialmente os dados de um usuário, em particular os perfis que ele possui no sistema e o seu status (também é possível alterar a carga horária do usuário, embora não seja da nossa responsabilidade)

> Os grupos responsáveis pela manutenção do perfil (Grupo 7) e pelo gerenciamento de permissões (Grupo 8) serão encarregados de implementar as rotas e operações CRUD das entidades relacionadas a Perfil (Profile) e Permissões (Permission). Portanto, o nosso foco será direcionado especificamente aos itens do nosso caso de uso: o Usuário (User).

## 📌 Documentação API (Back-end)

Para mais detalhes sobre os endpoints da API — incluindo exemplos de parâmetros, RequestBody e ResponseBody — consulte a [documentação completa aqui](https://www.postman.com/ultralight-4892/projeto-final-de-desenvolvimento-fullstack/overview). 

Também é possível visualizar e testar os endpoints diretamente através do Swagger da API. Basta executar a aplicação e acessar: ```http://localhost:8080/swagger-ui/index.html```.

O arquivo JSON com os dados de múltiplos usuários, que serão criados via requisição POST usando o Collection Runner do Postman, está disponível [aqui](https://drive.google.com/file/d/1VH235ayRvyyo3TcEE7XQcBKcD0paOVU0/view?usp=sharing).

## 📌 Comandos para executar a aplicação 

* Back-end:

```bash
cd backend
./mvnw spring-boot:run
```

* Front-end:

```bash
cd frontend
npm install
npm start // se não funcionar: "ng serve"
```
