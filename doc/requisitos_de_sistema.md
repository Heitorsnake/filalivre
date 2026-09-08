# Requisitos de Sistema - FilaLivre

## Stack obrigatória

- Java 21 e Spring Boot no backend.
- SQLite como único banco de dados.
- HTML5 e CSS3 na apresentação.
- JavaScript Vanilla apenas para a interação das páginas e consumo da API.
- SQL em `database/schema.sql` como referência do modelo.

## Persistência

O banco é criado em modo arquivo no caminho definido por `FILALIVRE_DB_PATH`. O padrão é:

```text
database/filalivre.db
```

O Hibernate cria e atualiza as tabelas com `ddl-auto: update`. A aplicação usa o driver SQLite Xerial e o dialeto SQLite do Hibernate.

## API principal

| Método | Rota | Perfil |
|---|---|---|
| `POST` | `/api/auth/login` | Público |
| `POST` | `/api/auth/cadastro` | Público, cria operador |
| `POST` | `/api/auth/logout` | Autenticado |
| `GET` | `/api/caixas` | Autenticado |
| `POST` | `/api/caixas` | Gestor |
| `PATCH` | `/api/caixas/{id}` | Gestor/Administrador |
| `DELETE` | `/api/caixas/{id}` | Gestor/Administrador |
| `POST` | `/api/caixas/{id}/iniciar` | Operador |
| `POST` | `/api/caixas/{id}/espera` | Operador |
| `POST` | `/api/caixas/{id}/finalizar` | Operador |
| `POST` | `/api/solicitacoes` | Operador |
| `GET` | `/api/solicitacoes/pendentes` | Supervisor/Gestor |
| `POST` | `/api/solicitacoes/{id}/analisar` | Supervisor/Gestor |
| `POST` | `/api/solicitacoes/{id}/decidir` | Supervisor/Gestor |
| `GET` | `/api/relatorios/resumo` | Gestor/Administrador |
| `GET` | `/api/historico` | Gestor/Administrador |
| `GET/POST/PATCH` | `/api/usuarios` | Administrador |

## Segurança

- Sessão autenticada com Spring Security.
- Senhas armazenadas com BCrypt.
- Rotas protegidas por perfil usando `@PreAuthorize`.
- Usuários inativos não podem autenticar.
- Ações relevantes são registradas na auditoria.

## Requisitos não funcionais

- A interface deve funcionar em desktop e telas menores.
- O backend deve responder em HTTP na porta `8080` por padrão.
- O banco deve permanecer em arquivo local ou volume persistente no Docker.
- A aplicação deve iniciar sem depender de banco externo.
