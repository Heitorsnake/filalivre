# FilaLivre

Sistema de gerenciamento remoto de caixas de supermercado. O operador registra uma ocorrência no terminal, o gestor ou supervisor toma a decisão remotamente e o administrador mantém usuários, perfis e auditoria.

## Tecnologias

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA e Hibernate.
- **Banco:** SQLite em arquivo (`database/filalivre.db`).
- **SQL:** modelo de referência em [database/schema.sql](database/schema.sql).
- **Frontend:** HTML5 e CSS3, com JavaScript Vanilla somente para interação e chamadas REST.
- **Execução:** Maven ou Docker.

## Perfis e interfaces

| Perfil | Interface | Responsabilidades |
|---|---|---|
| Operador | [Terminal do caixa](frontend/caixa.html) | Registrar itens, controlar atendimento e enviar solicitações. |
| Supervisor | [Painel de operação](frontend/painel.html) | Acompanhar caixas e decidir solicitações. |
| Gestor | [Painel de operação](frontend/painel.html) | Acompanhar caixas, decidir solicitações e cadastrar caixas. |
| Administrador | [Administração](frontend/admin.html) | Gerenciar usuários, perfis, ativação, relatórios e auditoria. |

O backend também aplica essas permissões nas rotas. Acesso direto a uma tela não permite contornar a autorização do servidor.

## Estrutura

```text
backend/                       API Java/Spring Boot
  src/main/java/               Controllers, serviços, modelos e segurança
  src/main/resources/          Configuração da aplicação
database/schema.sql            Schema SQL de referência
doc/                           Documentação do projeto
frontend/                      Interfaces HTML, CSS e scripts de interação
Dockerfile                     Build e execução com Java 21
```

Documentação detalhada:

- [Escopo do projeto](doc/escopo_do_projeto.md)
- [Requisitos de sistema](doc/requisitos_de_sistema.md)
- [Requisitos de usuário](doc/requisitos_de_usuario.md)

## Banco SQLite

A aplicação usa SQLite em modo arquivo. O caminho padrão é:

```text
database/filalivre.db
```

É possível alterar o local com a variável `FILALIVRE_DB_PATH`. No Docker, o banco fica em `/data/filalivre.db`; monte um volume persistente em `/data` para preservar os dados entre reinicializações.

O Hibernate cria e atualiza as tabelas ao iniciar a aplicação. O arquivo [database/schema.sql](database/schema.sql) documenta as tabelas, relacionamentos e dados iniciais esperados.

## Executar localmente

Pré-requisitos: Java 21 e Maven.

```bash
cd backend
mvn spring-boot:run
```

Acesse:

```text
http://localhost:8080
```

O backend serve as páginas do frontend quando `FILALIVRE_FRONTEND_PATH` aponta para `../frontend`, que é o padrão local.

## Executar com Docker

```bash
docker build -t filalivre .
docker run --rm -p 8080:8080 -v filalivre-data:/data filalivre
```

Depois, acesse `http://localhost:8080`.

As contas iniciais são criadas internamente por `DadosIniciais.java` quando a base é inicializada. As credenciais não são exibidas na interface pública de login.

## Publicação

O GitHub Pages publica somente o frontend. Para usar o site publicado, hospede o backend Java em um serviço acessível pela internet e configure `frontend/js/config.js`:

```javascript
window.FILALIVRE_API_URL = "https://seu-backend.exemplo.com/api";
```

O backend deve aceitar CORS para o domínio do frontend e usar HTTPS quando a sessão for compartilhada entre domínios.

## Equipe

Eduardo Gomes, Maicon Goulart, Heitor Hara e José Leandro.

Disciplina: Projeto Integrador  
Professor: André Lobo
