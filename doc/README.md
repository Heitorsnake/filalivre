# Documentação do FilaLivre

Esta pasta reúne a documentação funcional, técnica e operacional do projeto.

## Índice

- [Escopo do projeto](escopo_do_projeto.md): objetivo, funcionalidades, perfis, limites e arquitetura.
- [Requisitos de usuário](requisitos_de_usuario.md): atores, fluxos e critérios de aceite.
- [Requisitos de sistema](requisitos_de_sistema.md): stack, persistência, API, segurança e requisitos não funcionais.
- [Guia de execução e publicação](guia_de_execucao.md): banco SQLite, execução local, Docker e publicação.
- [Contas de demonstração](contas_de_demonstracao.md): usuários e mercado inicial para testes.

## Artefatos relacionados

Os arquivos SQL permanecem em `../database/`:

- [`schema.sql`](../database/schema.sql): referência do esquema e das tabelas.
- [`filalivre-cloud.sql`](../database/filalivre-cloud.sql): dump de demonstração para consulta no SQLiteCloud.

O README da raiz apresenta o projeto e encaminha para esta documentação.
