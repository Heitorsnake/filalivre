# Requisitos de Sistema - FilaLivre

## Stack obrigatória

- Java 21 e Spring Boot no backend.
- SQLite como banco padrão da aplicação.
- HTML5 e CSS3 na apresentação.
- JavaScript Vanilla apenas para a interação das páginas e consumo da API.
- SQL em `database/schema.sql` como referência do modelo.

## Persistência

O banco é criado em modo arquivo no caminho definido por `FILALIVRE_DB_PATH`. O padrão é:

```text
database/filalivre.db
```

O Hibernate cria e atualiza as tabelas com `ddl-auto: update`. A aplicação usa o driver SQLite Xerial e o dialeto SQLite do Hibernate.

O arquivo `database/filalivre-cloud.sql` é um dump independente para visualização no SQLiteCloud. Ele não é a conexão usada pelo Render. O arquivo `filalivre.mv.db` é H2 e não deve ser enviado ao SQLiteCloud como se fosse SQLite.

## API principal

| Método | Rota | Perfil |
|---|---|---|
| `POST` | `/api/auth/login` | Público |
| `POST` | `/api/auth/cadastro` | Público, cria operador ou supervisor |
| `POST` | `/api/auth/logout` | Autenticado |
| `GET` | `/api/caixas` | Autenticado |
| `POST` | `/api/caixas` | Supervisor/Gestor |
| `PATCH` | `/api/caixas/{id}` | Supervisor/Gestor/Administrador |
| `DELETE` | `/api/caixas/{id}` | Supervisor/Gestor/Administrador |
| `POST` | `/api/caixas/{id}/iniciar` | Operador |
| `POST` | `/api/caixas/{id}/espera` | Operador |
| `POST` | `/api/caixas/{id}/finalizar` | Operador |
| `POST` | `/api/solicitacoes` | Operador |
| `GET` | `/api/solicitacoes/pendentes` | Supervisor/Gestor |
| `POST` | `/api/solicitacoes/{id}/analisar` | Supervisor/Gestor |
| `POST` | `/api/solicitacoes/{id}/decidir` | Supervisor/Gestor |
| `GET` | `/api/relatorios/resumo` | Gestor/Administrador |
| `GET` | `/api/mercados/meu` | Autenticado |
| `POST` | `/api/mercados` | Supervisor/Gestor |
| `POST` | `/api/mercados/entrar` | Operador |
| `GET` | `/api/historico` | Gestor/Administrador |
| `GET/POST/PATCH` | `/api/usuarios` | Administrador |

## Segurança

- Sessão autenticada com Spring Security.
- Senhas armazenadas com BCrypt.
- Rotas protegidas por perfil usando `@PreAuthorize`.
- Usuários inativos não podem autenticar.
- Ações relevantes são registradas na auditoria.
- O acesso a caixas, solicitações, histórico e relatórios respeita o mercado do usuário.

## Requisitos não funcionais

- A interface deve funcionar em desktop e telas menores.
- O backend deve responder em HTTP na porta `8080` por padrão.
- O banco deve permanecer em arquivo local ou volume persistente no Docker.
- A aplicação deve iniciar sem depender de banco externo.
- O SQLiteCloud pode ser usado para consultar o dump de demonstração, mas não é usado automaticamente pela aplicação.
