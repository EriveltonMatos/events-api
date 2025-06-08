# Events API
API RESTful para gerenciamento de eventos desenvolvida com Java e Spring Boot.

## Tecnologias Utilizadas

* Java 17

* Spring Boot 3.5.0

* Spring Data JPA

* Spring Web

* Spring Validation

* H2 Database (em memória)

* Lombok

* SpringDoc OpenAPI (Swagger)

* Maven

## Funcionalidades

* Criar eventos
* Listar todos os eventos (com e sem paginação)
* Buscar evento por ID
* Atualizar eventos
* Remover eventos (soft delete)
* Validação de dados de entrada
* Tratamento de exceções personalizado
* Documentação automática com Swagger
* Logs estruturados

## Configuração e Execução
Pré-requisitos

* Java 17 ou superior
* Maven 3.6+

## Como executar

* Clone o repositório: git clone https://github.com/EriveltonMatos/events-api.git
* cd events-api
* Acesse o projeto com alguma IDE (Intellij, VsCode, etc) e inicialize o servidor, ou inicialize pelo terminal utilizando: mvn spring-boot:run

## Acessando a aplicação

* API: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui.html

## Configuração do Banco H2
* JDBC URL: jdbc:h2:mem:testdb
* Username: sa
* Password: (pode deixar em branco)

## Documentação da API
| Método | Endpoint           | Descrição                  |
|--------|--------------------|----------------------------|
| GET    | /api/events        | Lista eventos com paginação |
| GET    | /api/events/{id}   | Busca evento por ID        |
| GET    | /api/events/all  | Busca eventos sem paginação       |
| POST   | /api/events        | Cria novo evento           |
| PUT    | /api/events/{id}   | Atualiza evento            |
| DELETE | /api/events/{id}   | Remove evento (soft delete)            |

## Exemplos de uso
### Criar Evento
POST /api/events

Content-Type: application/json
```bash
{
  "titulo": "Entrevista Técnica",
  "dataHora": "2025-12-15T14:00:00",
  "local": "Remoto"
}
```

### Buscar eventos

GET /api/events

### Buscar eventos sem paginação

GET /api/events/all

### Buscar evento pelo ID

GET /api/events/{id}

### Atualizar evento pelo ID

PUT /api/events/1

Content-Type: application/json
```bash
{
  "titulo": "Entrevista Técnica - ATUALIZADA",
  "dataHora": "2025-12-16T14:00:00",
  "local": "Presencial"
}
```
### Deletar evento pelo ID (soft delete, continua disponível no banco)

DELETE /api/events/{id}

## Validações

* **Título:** Obrigatório, máximo 100 caracteres
* **Data/Hora:** Obrigatória, deve ser no futuro
* **Local:** Obrigatório, máximo 200 caracteres

## Boas práticas

* **Separação de Responsabilidades:** Controller, Service, Repository
* **DTOs:** Separação entre dados de entrada e saída
* **Mapper Pattern:** Conversão entre Entity e DTO
* **Exception Handling:** Tratamento centralizado de exceções
* **Soft Delete:** Remoção lógica dos registros
* **Validação:** Validações no DTO, não na Entity
* **Paginação:** Suporte nativo do Spring Data

## Testes de software
Testes criados para `EventController` e `EventService`, todos funcionais e cobrindo os principais cenários de uso.
Os testes foram implementados utilizando **JUnit** e **Mockito**
