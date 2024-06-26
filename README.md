
# Authentication API

Este é um microsserviço de autenticação usando Spring Boot, JWT, Flyway e MySQL. Ele suporta registro de usuários, login, refresh token e atribuição de roles a usuários.

## Tecnologias Utilizadas

- Spring Boot 3.1.1
- Spring Security
- JWT (JSON Web Token)
- Flyway
- MySQL
- JPA (Java Persistence API)
- H2 Database (para desenvolvimento e testes)
- Lombok

## Configuração

### application.properties

```properties
# Configurações do DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/authenticationapi
spring.datasource.username=root
spring.datasource.password=12345678

# Configurações do JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configurações do Flyway
flyway.enabled=true
flyway.locations=classpath:db/migration

# Configurações do JWT
jwt.secret=my-secure-secret-key-that-is-256-bits-long-and-more-than-32-characters
jwt.expiration=3600000  # 1 hour
```

### Scripts de Migração Flyway

Crie um arquivo de migração Flyway `V1__initial_setup.sql` em `src/main/resources/db/migration/`:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Endpoints da API

### Registro de Usuário

**URL:** `/api/auth/register`

**Método:** `POST`

**Exemplo de Requisição:**

```sh
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{
  "username": "user1",
  "password": "password"
}'
```

### Login

**URL:** `/api/auth/login`

**Método:** `POST`

**Exemplo de Requisição:**

```sh
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{
  "username": "user1",
  "password": "password"
}'
```

**Resposta:**

```json
{
  "jwt": "your_jwt_token_here",
  "refreshToken": "your_refresh_token_here"
}
```

### Atribuir Role a um Usuário

**URL:** `/api/auth/assign-role`

**Método:** `POST`

**Parâmetros de Query:**

- `username`: Nome de usuário
- `roleName`: Nome da role (por exemplo, `ROLE_USER`)

**Exemplo de Requisição:**

```sh
curl -X POST "http://localhost:8080/api/auth/assign-role?username=user1&roleName=ROLE_USER" -H "Authorization: Bearer your_jwt_token_here"
```

### Refresh Token

**URL:** `/api/auth/refresh-token`

**Método:** `POST`

**Exemplo de Requisição:**

```sh
curl -X POST http://localhost:8080/api/auth/refresh-token -H "Content-Type: application/json" -d '{
  "refreshToken": "your_refresh_token_here"
}'
```

**Resposta:**

```json
{
  "accessToken": "new_jwt_token_here",
  "refreshToken": "your_refresh_token_here"
}
```

### Acessar Endpoint Protegido

**URL:** `/api/protected`

**Método:** `GET`

**Exemplo de Requisição:**

```sh
curl -X GET http://localhost:8080/api/protected -H "Authorization: Bearer new_jwt_token_here"
```

## Funcionamento do Refresh Token

### Conceito

Um refresh token é utilizado para obter um novo access token sem que o usuário precise fazer login novamente. Enquanto o access token tem uma vida útil curta (por exemplo, 1 hora), o refresh token tem uma vida útil mais longa e pode ser usado para renovar o access token quando este expirar.

### Fluxo de Trabalho

1. **Login:** Quando o usuário faz login, a aplicação retorna um access token (JWT) e um refresh token.
2. **Armazenamento:**
    - O access token é armazenado no armazenamento local (localStorage) ou de sessão (sessionStorage) do navegador.
    - O refresh token é armazenado em um cookie seguro (HttpOnly) para prevenir acessos indevidos por JavaScript.
3. **Acesso a Endpoints Protegidos:**
    - O access token é enviado em cada requisição para endpoints protegidos no header de autorização.
4. **Renovação do Access Token:**
    - Quando o access token expira, a aplicação envia o refresh token para obter um novo access token.

### Implementação

**Armazenamento no Cliente:**

```javascript
// Após o login
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ username: 'user1', password: 'password' })
});

const data = await response.json();

// Armazenar o JWT
localStorage.setItem('jwt', data.jwt);

// Armazenar o refresh token em um cookie seguro
document.cookie = `refreshToken=${data.refreshToken}; Secure; HttpOnly; Path=/;`;
```

**Acesso a Endpoints Protegidos:**

```javascript
const jwt = localStorage.getItem('jwt');

const response = await fetch('http://localhost:8080/api/protected', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${jwt}`
  }
});

const data = await response.json();
console.log(data);
```

**Renovação do Access Token:**

```javascript
async function refreshAccessToken() {
  const refreshToken = getCookie('refreshToken');

  const response = await fetch('http://localhost:8080/api/auth/refresh-token', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ refreshToken })
  });

  const data = await response.json();
  localStorage.setItem('jwt', data.accessToken);
}

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

// Exemplo de requisição com renovação automática do token
async function fetchWithAuth(url, options = {}) {
  let jwt = localStorage.getItem('jwt');
  options.headers = {
    ...options.headers,
    'Authorization': `Bearer ${jwt}`
  };

  let response = await fetch(url, options);

  if (response.status === 401) { // Se o token expirou
    await refreshAccessToken(); // Renova o token
    jwt = localStorage.getItem('jwt');
    options.headers['Authorization'] = `Bearer ${jwt}`;
    response = await fetch(url, options); // Refaça a requisição com o novo token
  }

  return response;
}

// Uso da função fetchWithAuth
const response = await fetchWithAuth('http://localhost:8080/api/protected');
const data = await response.json();
console.log(data);
```

## Executando a Aplicação

1. Certifique-se de que o MySQL está em execução e crie um banco de dados chamado `authenticationapi`.
2. Configure as propriedades do banco de dados no arquivo `application.properties`.
3. Execute a aplicação Spring Boot usando o comando:

```sh
./mvnw spring-boot:run
```

