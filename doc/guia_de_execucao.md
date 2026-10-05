# Guia de execução e publicação

**Instituição:** Instituto Federal de Mato Grosso (IFMT)  
**Turma:** 2º ano B de Informática  
**Integrantes:** Heitor Pernet, Maicon Goulart, Eduardo Gomes e José Leandro

---

## Pré-requisitos

- Java 21 e Maven para execução local.
- Docker para build e execução em contêiner.

## Banco SQLite

A aplicação usa SQLite em modo arquivo. O caminho padrão é `database/filalivre.db` e pode ser alterado com `FILALIVRE_DB_PATH`. O Hibernate cria e atualiza as tabelas ao iniciar (`ddl-auto: update`).

O modelo está documentado em [`../database/schema.sql`](../database/schema.sql). O arquivo [`../database/filalivre-cloud.sql`](../database/filalivre-cloud.sql) é um dump independente para consulta no SQLiteCloud: não é a conexão usada pelo Render nem substitui o banco da aplicação. Execute-o no SQL Editor de um banco de demonstração, não em **Upload Database**. As senhas contidas no dump são dados de visualização; use as contas do backend documentadas em [contas de demonstração](contas_de_demonstracao.md) para entrar na aplicação.

O arquivo `filalivre.mv.db` é H2 antigo e não deve ser enviado ao SQLiteCloud como se fosse SQLite.

No Docker, configure `FILALIVRE_DB_PATH=/data/filalivre.db` e monte volume persistente em `/data` para preservar os dados entre reinicializações.

## Execução local

Na raiz do repositório:

```bash
cd backend
mvn spring-boot:run
```

A aplicação fica disponível em `http://localhost:8080`. O backend serve o frontend pelo caminho `../frontend` por padrão local.

Em um banco novo, não há contas de demonstração por padrão. Para desenvolvimento, defina `FILALIVRE_DEMO_DATA=true` antes de iniciar a aplicação; em uma instalação sem dados de demonstração, configure `FILALIVRE_ADMIN_EMAIL` e `FILALIVRE_ADMIN_PASSWORD` para inicializar o primeiro administrador.

Contas novas com endereço `@gmail.com` (por cadastro ou criação administrativa) exigem confirmação por link enviado por SMTP antes do login. Configure `FILALIVRE_SMTP_USERNAME` e `FILALIVRE_SMTP_PASSWORD` com uma conta Gmail e uma senha de app do Google. Defina `FILALIVRE_FRONTEND_URL` como a URL pública da página `index.html` para que o link de confirmação aponte para o site correto.

## Execução com Docker

Na raiz do repositório:

```bash
docker build -t filalivre .
docker run --rm -p 8080:8080 -v filalivre-data:/data filalivre
```

Acesse `http://localhost:8080`. Para manter os dados entre execuções, preserve o volume `filalivre-data`.

## Publicação no Render

O serviço pode ser publicado usando o [Dockerfile](../Dockerfile):

1. Conecte o repositório GitHub ao Web Service do Render.
2. Escolha o ambiente Docker e use a porta HTTP fornecida pela variável `PORT`.
3. Configure `FILALIVRE_FRONTEND_PATH=/frontend`, `FILALIVRE_DB_PATH=/data/filalivre.db` e `FILALIVRE_COOKIE_SECURE=true`.
4. Configure `FILALIVRE_ADMIN_EMAIL` e `FILALIVRE_ADMIN_PASSWORD` antes do primeiro deploy para criar a conta administrativa inicial. A senha deve ter pelo menos 12 caracteres; armazene-a como segredo no provedor. O bootstrap só ocorre quando o banco ainda não tem usuários.
5. Configure `FILALIVRE_CORS_ALLOWED_ORIGINS` com as origens exatas do frontend, separadas por vírgulas. Não use `*` com cookies de sessão.
6. Configure `FILALIVRE_SMTP_USERNAME` e `FILALIVRE_SMTP_PASSWORD` como segredos do serviço. Para Gmail, use uma senha de app do Google (com a verificação em duas etapas ativada), não a senha normal da conta.
7. Configure `FILALIVRE_FRONTEND_URL` para a URL pública completa da tela de acesso, incluindo `/index.html`.
8. Adicione um Persistent Disk montado em `/data` para preservar o SQLite.
9. Faça o deploy e abra a URL atribuída ao serviço.

O backend usa sessão com cookie HttpOnly, token CSRF em requisições de alteração, CORS restrito, expiração por inatividade de 30 minutos e renovação do ID de sessão no login. Se o frontend estiver hospedado em outro domínio (por exemplo, GitHub Pages), configure HTTPS e `FILALIVRE_COOKIE_SAME_SITE=None` no backend; mantenha a origem exata em `FILALIVRE_CORS_ALLOWED_ORIGINS`. Não habilite dados de demonstração em produção.

Para testar localmente com contas fictícias, defina `FILALIVRE_DEMO_DATA=true` antes da primeira inicialização. Essas contas usam senhas públicas e nunca devem ser expostas na internet.

O Render usa o SQLite local do serviço. O plano gratuito pode não preservar arquivos entre reinicializações; sem Persistent Disk, os dados podem ser perdidos. O deploy não se conecta automaticamente ao SQLiteCloud.

## Publicação do frontend no GitHub Pages

O GitHub Pages publica somente o frontend. Para usá-lo, hospede o backend Java em um serviço acessível pela internet e configure `frontend/js/config.js`:

```javascript
window.FILALIVRE_API_URL = "https://seu-backend.exemplo.com/api";
```

O backend deve permitir CORS para o domínio do frontend. Use HTTPS no backend quando a sessão for compartilhada entre domínios.

## Acesso AnyDesk por caixa

No cadastro ou edição do caixa, o gestor informa opcionalmente o ID/endereço exibido no AnyDesk do computador do operador. Deve cadastrar o ID da máquina que será acessada, não o ID do computador do gestor. A seção **Acesso remoto** do painel reúne os identificadores e botões **Conectar**, separados dos cartões de status operacional. Consulte [Requisitos de usuário](requisitos_de_usuario.md#configuração-de-acesso-anydesk) para o fluxo completo.

O botão **Conectar** usa o URL handler `anydesk:<ID>` para solicitar que o navegador/sistema operacional abra o cliente AnyDesk instalado no computador do gestor e inicie a conexão. O formato segue a documentação oficial do AnyDesk para clientes padrão. O FilaLivre não incorpora a tela remota nem cria ou controla a sessão. A conexão requer o AnyDesk instalado no computador do gestor, o computador do caixa disponível e a autorização necessária no AnyDesk. Clientes AnyDesk personalizados podem exigir um esquema com prefixo próprio.
