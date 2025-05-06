# Sistema de Oferta de Componentes Curriculares (SOCC)
Repositório destinado para o Trabalho Incremental do Grupo 5 da disciplina de Desenvolvimento FullStack.

## 📌 Membros
* Gustavo Neves Piedade Louzada
* Hafy Mourad Jacoub de Cuba Kouzak
* Igor Rodrigues Castilho
* João Victor de Paiva Albuquerque
* Maria Eduarda de Campos Ramos

## 📌 Caso de Uso: Manter Usuário
* Listar dados do usuários (FP)
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
            + TECHNICAL_PEDAGOGICAL_STAFF
            + STUDENT
            + PEDAGOGICAL_CURATOR
            + DIRECTOR
            + DEPUTY_DIRECTOR
        }
	}

    class User {
	    - id: UUID
	    - userName: String
	    - name: String
	    - email: String
	    - phone: String
	    - workLoad: Integer
	    - status: UserStatus
	    - profiles: ArrayList~Profile~
    }
    class Profile {
	    - id: String
	    - name: ProfileRole
	    - description: String
	    - protectedProfile: Boolean
	    - permissions: ArrayList~Permission~
    }
    
    class Permission {
        - code: String
        - title: String
        - description: String
    }
    
	<<enumeration>> UserStatus
	<<enumeration>> ProfileRole

    User --> "1..*" Profile : has
    Profile --> "1..*" Permission : contains
```
