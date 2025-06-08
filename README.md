# Zelus - Sistema de Gerenciamento de Abrigos e Lotes

## 📌 Descrição

**Zelus** é uma aplicação web desenvolvida com **Spring Boot** para gerenciamento de **abrigos** e **lotes de alimentos**. O sistema oferece cadastro, atualização, listagem e exclusão de abrigos, além do gerenciamento de lotes vinculados a esses abrigos.

A aplicação implementa **autenticação com tokens JWT**, **controle de acesso por perfil**, **cache de dados** e documentação completa com **Swagger**.

---

## ✅ Funcionalidades

### 🏠 Gerenciamento de Abrigos

* Cadastro de novos abrigos com validação de e-mail único.
* Atualização e remoção de abrigos.
* Listagem paginada com filtros por nome, status, etc.

### 📦 Gerenciamento de Lotes

* Criação, atualização e remoção de lotes vinculados a abrigos.
* Listagem com filtros por abrigo e estado, com suporte a paginação.

### 🔐 Autenticação e Segurança

* Login com e-mail e senha usando JWT.
* Senhas armazenadas com criptografia.
* Controle de acesso.

### 🌐 CORS

* Configuração para aceitar requisições de qualquer origem.

### ⚡ Cache

* Cache implementado para listagens de lotes.
* Cache invalidado automaticamente em atualizações.

### 📚 Documentação

* API documentada com **Swagger**, acessível em:
  `http://localhost:8080/swagger-ui/`

---

## 🛠 Tecnologias Utilizadas

* Java 17+
* Spring Boot (Web, Security, JPA, Validation)
* Spring Security + JWT
* Hibernate / JPA
* H2 Database (ambiente de desenvolvimento)
* Lombok
* Swagger/OpenAPI
* Spring Cache
* Maven

---

## 🗂 Estrutura do Projeto

src/
└── main/
    ├── java/
    │   └── br/
    │       └── com/
    │           └── fiap/
    │               └── zelus/
    │                   ├── config/
    │                   │   ├── SecurityConfig.java
    │                   │   ├── CorsConfig.java
    │                   │   ├── AuthFilter.java
    │                   │   └── DataSeeder.java
    │                   ├── controller/
    │                   │   ├── AbrigoController.java
    │                   │   ├── LoteController.java
    │                   │   └── AuthController.java
    │                   ├── dto/
    │                   │   ├── AbrigoDTO.java
    │                   │   └── LoteDTO.java
    │                   ├── exception/
    │                   │   ├── ValidationHandler.java
    │                   │   └── PesoInvalidoException.java
    │                   ├── model/
    │                   │   ├── Abrigo.java
    │                   │   └── Lote.java
    │                   ├── repository/
    │                   │   ├── AbrigoRepository.java
    │                   │   └── LoteRepository.java
    │                   ├── service/
    │                   │   ├── AuthService.java
    │                   │   ├── LoteService.java
    │                   │   └── TokenService.java
    │                   └── specification/
    │                       └── LoteSpecification.java
    └── resources/
        ├── application.properties


---

## 🔗 Principais Endpoints

### Abrigos

* `POST /abrigo`: Criar novo abrigo
* `GET /abrigo`: Listar abrigos (paginação)
* `GET /abrigo/{id}`: Buscar abrigo por ID
* `PUT /abrigo/{id}`: Atualizar abrigo
* `DELETE /abrigo/{id}`: Remover abrigo

### Lotes

* `POST /lote/abrigo/{abrigoId}`: Criar lote para abrigo
* `GET /lote`: Listar lotes com filtros e paginação
* `GET /lote/abrigo/{abrigoId}`: Listar lotes por abrigo
* `GET /lote/{id}`: Buscar lote por ID
* `PUT /lote/{id}`: Atualizar lote
* `DELETE /lote/{id}`: Remover lote

### Autenticação

* `POST /login`: Login e geração de token JWT

---

## ▶️ Como Executar

### Pré-requisitos

* Java 17+
* Maven

### Passos

```bash
# Clone o repositório
git clone <URL_DO_REPOSITORIO>

# Acesse o diretório do projeto
cd zelus

# Compile e execute o projeto
mvn spring-boot:run
```

* Acesse a API em `http://localhost:8080`
* Acesse a documentação Swagger em `http://localhost:8080/swagger-ui/`

### Banco de Dados

* Utiliza **H2 em memória** por padrão.
* Dados de teste são inseridos automaticamente pelo `DataSeeder`.

---

## 🧪 Exemplos de Uso

### Criar Abrigo

```bash
curl -X POST http://localhost:8080/abrigo \
-H "Content-Type: application/json" \
-d '{
  "nome": "Abrigo Teste",
  "email": "teste@abrigo.org",
  "password": "123456",
  "cep": "12345-678",
  "status": "ATIVO"
}'
```

### Autenticar

```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/json" \
-d '{
  "email": "sp@abrigo.org",
  "password": "123456"
}'
```

### Criar Lote

```bash
curl -X POST http://localhost:8080/lote/abrigo/1 \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <SEU_TOKEN>" \
-d '{
  "pesoI": 20.0,
  "pesoFina": 15.0,
  "temperatura": 5.0,
  "estado": "ABERTO"
}'
```

---

## ℹ️ Observações

* Cache aplicado em listagens (`lotes`, `lotesByAbrigo`, `lotesPaged`).
* Apenas abrigos com status `ATIVO` conseguem fazer login.
* Suporte a CORS para facilitar a integração com frontends.

---

## 🤝 Integrantes

| Nome Completo                  | RM     |
| -------------------------------|--------|
| Hellen Marinho Cordeiro        | 558841 |
| Heloisa Alves de Mesquita      | 559145 |
| Júlia Soares Farias dos Santos | 554609 |
