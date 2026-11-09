# Segurança e autoria do FilaLivre

## Proteção de dados

O FilaLivre implementa controles no backend, mas nenhum arquivo ou configuração isolada garante que dados nunca sejam acessados indevidamente. A proteção depende também da configuração do servidor, da conta de hospedagem, do banco e dos backups.

Controles identificados no projeto:

- Senhas de usuários são armazenadas com BCrypt.
- A autenticação usa sessões; o cookie de sessão é configurado como HttpOnly.
- Requisições que alteram dados usam proteção CSRF.
- CORS aceita origens configuradas por `FILALIVRE_CORS_ALLOWED_ORIGINS`; não use curingas com sessões autenticadas.
- Rotas e operações são protegidas por perfil, e os dados de caixas e solicitações são limitados ao mercado do usuário.
- As alterações operacionais relevantes são registradas em auditoria.
- As operações do terminal exigem o perfil de operador e validam o operador associado ao caixa.

## Checklist para produção

Antes de disponibilizar o sistema na internet:

- Use HTTPS e configure `FILALIVRE_COOKIE_SECURE=true`.
- Mantenha `FILALIVRE_DEMO_DATA=false`; esse é o padrão do Dockerfile. Ative `FILALIVRE_DEMO_DATA=true` somente para demonstrações locais controladas. As contas demonstrativas não devem ficar ativas em produção.
- Crie a conta administrativa com `FILALIVRE_ADMIN_EMAIL` e `FILALIVRE_ADMIN_PASSWORD`. Use uma senha forte e exclusiva, com pelo menos 12 caracteres, e mantenha-a somente no gerenciador de segredos do provedor.
- Configure `FILALIVRE_CORS_ALLOWED_ORIGINS` com as origens exatas e confiáveis do frontend. Nunca use `*` com cookies de sessão.
- Configure `FILALIVRE_DB_PATH` em armazenamento persistente com acesso restrito. O SQLite usado pelo projeto não está configurado com criptografia própria; proteja o volume e os backups com os controles de criptografia do provedor ou do sistema operacional.
- Restrinja o acesso administrativo ao provedor, ao volume do banco e aos backups. Crie backups protegidos e teste a restauração.
- Não envie para o repositório senhas, tokens, arquivos `.env`, bancos com dados reais ou backups. Se um segredo já tiver sido publicado, remova-o e troque-o; apagar o arquivo do último commit não invalida um segredo exposto.
- Não use dados reais em demonstrações. Revise logs e auditoria para evitar registrar senhas, tokens ou informações pessoais desnecessárias.
- Se o frontend estiver em outro domínio e exigir cookies entre sites, use HTTPS e configure `FILALIVRE_COOKIE_SAME_SITE=None` junto com `FILALIVRE_COOKIE_SECURE=true`.

O cookie seguro tem valor `false` por padrão em `application.yml` para permitir desenvolvimento local. A configuração de produção precisa sobrescrever esse valor. Uma instância publicada sem HTTPS, com contas demonstrativas ou com credenciais fracas não fica protegida apenas por este documento.

## Comunicação de vulnerabilidades

Não publique senhas, tokens, dados pessoais ou detalhes que permitam explorar uma instância em uma issue pública. Use o canal privado de relato de vulnerabilidades oferecido pelo repositório ou pelo serviço de hospedagem, quando estiver habilitado. Revogue e substitua imediatamente credenciais ou tokens que tenham sido expostos.

## Autoria e atribuição

Este arquivo não constitui prova legal de autoria, titularidade ou exclusividade. A documentação atual identifica o projeto como trabalho do IFMT, na disciplina Projeto Integrador, e lista uma equipe, mas há uma divergência de nome que precisa ser confirmada pelos integrantes antes de qualquer declaração formal:

- O `README.md` da raiz lista Eduardo Gomes, Maicon Goulart, **Heitor Hara** e José Leandro.
- A documentação em `doc/README.md` e nos documentos de escopo e requisitos lista Heitor Pernet, Maicon Goulart, Eduardo Gomes e José Leandro.

Não substitua essa divergência por uma afirmação individual de propriedade sem confirmação dos coautores e da instituição. Para demonstrar histórico de contribuição, preserve o histórico do Git, os registros de entrega e os documentos originais do projeto. Nomes, datas ou direitos não foram inferidos neste arquivo.
