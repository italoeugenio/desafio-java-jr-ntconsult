# 📚 Livros — API de Gerenciamento de Livros

API REST para gerenciamento de livros, desenvolvida com Spring Boot como parte do desafio técnico Java Jr.

---

## 👤 Informações de Contato

- **Nome:** Ítalo Cezar Eugênio de Santana
- **Email:** italoeugenio539@gmail.com
- **Telefone:** (61) 99843-0733
- **Linkedin:** www.linkedin.com/in/italoeugenio

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker / Docker Compose**
- **Lombok**
- **Validation**

---

## 🚀 Como Rodar o Projeto

### Requisitos

- Java 17
- Maven
- PostgreSQL
- Docker

---

### 🐳 Rodar com Docker (Recomendado)

1. Crie o arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
PORT=8080

DB_HOST=app-database
DB_USER=dev
DB_PASSWORD=dev
DB_PORT=5432
DB_NAME=NtConsult
```

2. Suba os containers:

```bash
# Iniciar os containers
docker compose up -d --build

# Ver logs
docker logs livros-api -f

# Parar os containers
docker compose down
```

A API estará disponível em: `http://localhost:8080`

---

### ☕ Rodar Localmente (sem Docker)

1. Certifique-se de ter um PostgreSQL rodando localmente.

2. Configure as variáveis de ambiente ou edite o `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/NtConsult
spring.datasource.username=dev
spring.datasource.password=dev
```

3. Não esqueça de criar o database `NtConsult` no PostgreSQL.

4. Execute com Maven:

```bash
mvn spring-boot:run
```

---

## 🐳 Infraestrutura Docker

O `docker-compose.yml` sobe **3 serviços** conectados por uma rede interna isolada.

### Serviços

| Serviço | Imagem | Porta | Descrição |
|---|---|---|---|
| `livros-api` | Build local | `8080` | Aplicação Spring Boot |
| `app-database` | `postgres:17-alpine` | `5432` | Banco de dados PostgreSQL |
| `pgadmin-application` | `dpage/pgadmin4` | `4332` | Interface visual para o banco |

---

### 🔐 Rede Interna (`database-network`)

Todos os serviços compartilham a rede `database-network` (bridge), o que significa que eles se comunicam pelo **nome do container**, sem expor serviços desnecessariamente para o mundo externo.

```
┌─────────────────────────────────────────────┐
│              database-network               │
│                                             │
│  livros-api  ──────►  app-database          │
│                                             │
│  pgadmin     ──────►  app-database          │
│                                             │
└─────────────────────────────────────────────┘
         ▲                    ▲
    porta 8080            porta 4332
    (pública)             (pública)
```

> A `livros-api` se conecta ao banco usando `DB_HOST=app-database` o nome do container funciona como hostname dentro da rede interna.

---

### 🌐 Boas Práticas com Frontend

Caso exista um frontend na arquitetura, ele **não deve ter acesso direto ao banco de dados**. O frontend deve se comunicar apenas com a API, que é a responsável por toda a lógica e acesso aos dados.

Para isso, basta colocar o frontend em uma rede separada, sem incluí-lo na `database-network`:

```yaml
services:
  frontend:
    image: frontend
    ports:
      - "3000:3000"
    networks:
      - public-network 

  livros-api:
    networks:
      - public-network    
      - database-network  

  app-database:
    networks:
      - database-network  

networks:
  public-network:
    driver: bridge
  database-network:
    driver: bridge
```

```
frontend  ──►  livros-api  ──►  app-database
   ✅               ✅               ❌
  (acessa         (acessa         (frontend não
   a API)          o banco)        enxerga o banco)
```

---

### 🖥️ Acessando o pgAdmin

O pgAdmin é uma interface gráfica para visualizar e gerenciar o banco de dados PostgreSQL.

**URL:** `http://localhost:4332`

| Campo | Valor |
|---|---|
| Email | `ntconsult@application.com` |
| Senha | `admin` |

Após fazer login, registre o servidor com as seguintes configurações:

| Campo | Valor |
|---|---|
| Host | `app-database` |
| Port | `5432` |
| Username | `dev` |
| Password | `dev` |
| Database | `NtConsult` |

> ⚠️ Use `app-database` como host (nome do container na rede interna), e não `localhost`.

---

## 🌱 Seed Inicial

Ao iniciar a aplicação com o banco vazio, **10 livros são inseridos automaticamente** para facilitar os testes:

| Título | Autor | Ano |
|---|---|---|
| O Hobbit | J.R.R. Tolkien | 1937 |
| A Sociedade do Anel | J.R.R. Tolkien | 1954 |
| Clean Code | Robert C. Martin | 2008 |
| O Guia do Mochileiro das Galáxias | Douglas Adams | 1979 |
| 1984 | George Orwell | 1949 |
| Dom Casmurro | Machado de Assis | 1899 |
| O Alquimista | Paulo Coelho | 1988 |
| Harry Potter e a Pedra Filosofal | J.K. Rowling | 1997 |
| O Senhor das Moscas | William Golding | 1954 |
| A Metamorfose | Franz Kafka | 1915 |

---

## 📖 Endpoints da API

Base URL: `/livros`

> ℹ️ Todos os endpoints consomem e produzem `Content-Type: application/json`.

---

### ➕ Cadastrar Livro

```
POST /livros
Content-Type: application/json
```

**Body:**
```json
{
  "titulo": "O Hobbit",
  "autor": "J.R.R. Tolkien",
  "anoPublicacao": 1937
}
```

**Resposta:** `201 Created`
```
Content-Type: application/json
```
```json
{
  "id": 1,
  "titulo": "O Hobbit",
  "autor": "J.R.R. Tolkien",
  "anoPublicacao": 1937
}
```

---

### 📋 Listar Todos os Livros (Paginado)

```
GET /livros?numeroDaPagina=1
```

| Parâmetro (Opcional) | Tipo | Padrão | Descrição |
|----------------------|---|---|---|
| `numeroDaPagina` | Integer | `1` | Temos 60 itens por página, e ela inicia na 1 |

**Resposta:** `200 OK`
```
Content-Type: application/json
```
```json
{
  "livros": [...],
  "totalDePaginas": 1
}
```

> ⚠️ Páginas menores que 1 retornam `400 Bad Request`.

---

### 🔍 Buscar Livro por ID

```
GET /livros/{id}
```

**Resposta:** `200 OK`
```
Content-Type: application/json
```
```json
{
  "id": 1,
  "titulo": "O Hobbit",
  "autor": "J.R.R. Tolkien",
  "anoPublicacao": 1937
}
```

> ⚠️ ID não encontrado retorna `404 Not Found`.

---

### ✏️ Atualizar Livro

```
PUT /livros/{id}
Content-Type: application/json
```

**Body:**
```json
{
  "titulo": "O Hobbit - Edição Revisada",
  "autor": "J.R.R. Tolkien",
  "anoPublicacao": 1937
}
```

**Resposta:** `200 OK`
```
Content-Type: application/json
```

> ⚠️ ID não encontrado retorna `404 Not Found`.

---

### 🗑️ Deletar Livro

```
DELETE /livros/{id}
```

**Resposta:** `204 No Content`

> ⚠️ ID não encontrado retorna `404 Not Found`.

---

## 📦 Estrutura do Projeto

```
src/
├── controller/     # Entrada HTTP (API REST)
├── service/        # Regras de negócio
├── repository/     # Comunicação com o banco de dados
├── domain/
│   ├── entities/   # Entidades JPA (tabelas do banco)
│   └── dtos/       # Objetos de entrada/saída da API
├── mapper/         # Conversão entre Entity ↔ DTO
├── handler/        # Tratamento de erros
├── exception/      # Exceções personalizadas
└── config/         # Configurações e seed de dados
```

---

## 💡 Decisões Técnicas

- **PostgreSQL:** banco relacional robusto, escolhido por ser amplamente utilizado em ambientes de produção.
- **Docker:** garante um ambiente padronizado e reproduzível, eliminando problemas de configuração local.
- **Lombok:** elimina código boilerplate como getters, construtores e logs.
- **Separação em DTOs:** mantém a entidade de persistência isolada dos dados expostos pela API, seguindo boas práticas de arquitetura.
- **Paginação:** a listagem usa paginação para evitar sobrecarga em consultas com muitos registros.
- **Rede isolada no Docker:** o banco de dados não é acessível diretamente por serviços externos à `database-network`, garantindo maior segurança na comunicação entre os containers.
- **Stack Trace:** foi configurado `server.error.include-stacktrace=never` no `application.properties` para evitar o vazamento de informações sensíveis (stack trace) para o cliente, seguindo boas práticas de segurança.

## 🚀 Melhorias que eu faria futuramente

- Criar documentação da API utilizando ferramentas como Swagger para facilitar a visualização e o teste dos endpoints.
- Alterar a modelagem para fazer com que **Autor** seja uma tabela separada, pensando em normalização. Porém, dependendo do contexto e da necessidade de performance, poderia manter desnormalizado para evitar JOINs e reduzir o custo de consultas.
- Utilizar o **Flyway** para versionamento e controle das migrações do banco de dados.
- Criar filtros no endpoint de listagem de livros utilizando **Specification** para permitir consultas mais flexíveis e dinâmicas.
- Proteger a api utilizando Spring Security.