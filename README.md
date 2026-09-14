# FilaLivre

Sistema de gerenciamento remoto de caixas de supermercado. O operador registra uma ocorrência no terminal, o supervisor, que exerce a função de gestor, toma a decisão remotamente e o administrador mantém usuários, perfis e auditoria.

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
| Supervisor/gestor | [Painel de operação](frontend/painel.html) | Cadastrar mercado, acompanhar caixas, decidir solicitações, cadastrar caixas e consultar relatórios. |
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
demo-contas.md                 Credenciais e código do mercado de demonstração
database/filalivre-cloud.sql   Dump SQLite para visualização no SQLiteCloud
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

Para visualizar uma base de demonstração em um serviço SQLite, use o arquivo [database/filalivre-cloud.sql](database/filalivre-cloud.sql) no editor SQL do SQLiteCloud. O arquivo `filalivre.mv.db` não deve ser enviado: ele é um banco H2 antigo e não possui uma chave de criptografia SQLite.

O arquivo `database/filalivre-cloud.sql` é uma cópia de demonstração para consulta. Ele não conecta o Render ao SQLiteCloud e não substitui o banco usado pela aplicação. O script recria as tabelas `mercados`, `usuarios`, `caixas`, `solicitacoes` e `registros_acao`, além de inserir um mercado, três usuários e seis caixas. Execute-o no SQL Editor de um banco de demonstração, não em **Upload Database**. Como as senhas do dump são apenas dados de visualização, use as contas criadas pelo backend para fazer login na aplicação.

### Modelo de dados

- `mercados`: mercado cadastrado pelo supervisor e seu código de acesso.
- `usuarios`: contas, perfil, status e mercado associado.
- `caixas`: caixas ativos, operador atual, status, totais e mercado.
- `solicitacoes`: pedidos de cancelamento, desconto ou cupom.
- `registros_acao`: auditoria de login, atendimento, decisões e alterações.

Cada operador informa o código do mercado no terminal. A API passa a listar somente os caixas e solicitações daquele mercado.

## Executar localmente

Pré-requisitos: Java 21 e Maven.

```bash
cd backend
mvn spring-boot:run
```

Acesse:

```text
http://localhost:8080
https://filalivre.onrender.com
```

O backend serve as páginas do frontend quando `FILALIVRE_FRONTEND_PATH` aponta para `../frontend`, que é o padrão local.

## Executar com Docker

```bash
docker build -t filalivre .
docker run --rm -p 8080:8080 -v filalivre-data:/data filalivre
```

Depois, acesse `http://localhost:8080`ou `https://filalivre.onrender.com`.

As contas iniciais são criadas internamente por `DadosIniciais.java` quando a base é inicializada. Para demonstração, use:

| Perfil | E-mail | Senha | Código do mercado |
|---|---|---|---|
| Administrador | `admin@filalivre.com` | `admin123` | - |
| Supervisor / gestor | `supervisor@filalivre.com` | `supervisor123` | `MERCADO1` |
| Operador | `operador@filalivre.com` | `operador123` | `MERCADO1` |

O supervisor cria ou atualiza o nome do mercado no painel e compartilha o código exibido com os operadores. O operador informa esse código no terminal para visualizar os caixas daquele mercado.

## Publicação

O serviço pode ser publicado no Render usando o [Dockerfile](Dockerfile). Configure `FILALIVRE_FRONTEND_PATH=/frontend`, `FILALIVRE_DB_PATH=/data/filalivre.db` e a porta fornecida pelo Render. O plano gratuito não mantém um arquivo SQLite entre reinicializações; para produção, use um Persistent Disk ou troque o banco por PostgreSQL. O deploy do Render não fica automaticamente conectado ao SQLiteCloud.

### Publicar no Render

1. Conecte o repositório GitHub ao Web Service do Render.
2. Escolha ambiente Docker e mantenha a porta HTTP configurada pela variável `PORT`.
3. Adicione `FILALIVRE_FRONTEND_PATH=/frontend` e `FILALIVRE_DB_PATH=/data/filalivre.db`.
4. Adicione um Persistent Disk montado em `/data` se quiser preservar o SQLite.
5. Faça o deploy e abra `https://filalivre.onrender.com`.

O Render executa a aplicação e usa o SQLite local do serviço. O SQLiteCloud só serve para consultar o dump de demonstração, a menos que a aplicação seja alterada para usar uma conexão remota.

O GitHub Pages publica somente o frontend. Para usar o site publicado, hospede o backend Java em um serviço acessível pela internet e configure `frontend/js/config.js`:

```javascript
window.FILALIVRE_API_URL = "https://seu-backend.exemplo.com/api";
```

O backend deve aceitar CORS para o domínio do frontend e usar HTTPS quando a sessão for compartilhada entre domínios.

## Equipe

Eduardo Gomes, Maicon Goulart, Heitor Hara e José Leandro.

Disciplina: Projeto Integrador  
Professor: André Lobo
