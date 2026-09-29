# Guia de execução e publicação

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
3. Configure `FILALIVRE_FRONTEND_PATH=/frontend` e `FILALIVRE_DB_PATH=/data/filalivre.db`.
4. Adicione um Persistent Disk montado em `/data` para preservar o SQLite.
5. Faça o deploy e abra a URL atribuída ao serviço.

O Render usa o SQLite local do serviço. O plano gratuito pode não preservar arquivos entre reinicializações; sem Persistent Disk, os dados podem ser perdidos. O deploy não se conecta automaticamente ao SQLiteCloud.

## Publicação do frontend no GitHub Pages

O GitHub Pages publica somente o frontend. Para usá-lo, hospede o backend Java em um serviço acessível pela internet e configure `frontend/js/config.js`:

```javascript
window.FILALIVRE_API_URL = "https://seu-backend.exemplo.com/api";
```

O backend deve permitir CORS para o domínio do frontend. Use HTTPS no backend quando a sessão for compartilhada entre domínios.

## Acesso AnyDesk por caixa

No cadastro ou edição do caixa, o gestor informa opcionalmente o ID/endereço exibido no AnyDesk do computador do operador. Deve cadastrar o ID da máquina que será acessada, não o ID do computador do gestor. A seção **Acesso remoto** do painel reúne os identificadores e botões **Conectar**, separados dos cartões de status operacional. Consulte [Requisitos de usuário](requisitos_de_usuario.md#configuração-de-acesso-anydesk) para o fluxo completo.

O botão **Conectar** solicita que o navegador/sistema operacional abra o cliente AnyDesk no computador do gestor. O FilaLivre não incorpora a tela remota nem cria ou controla a sessão. A conexão requer o AnyDesk instalado no computador do gestor, o computador do caixa disponível e a autorização necessária no AnyDesk.
