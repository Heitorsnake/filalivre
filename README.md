# FilaLivre

Sistema para acompanhamento remoto de caixas de supermercado. Operadores registram itens e enviam solicitações de cancelamento; supervisores/gestores acompanham os caixas e decidem solicitações; administradores mantêm usuários e auditoria.

## Interfaces

| Perfil | Interface | Responsabilidades |
|---|---|---|
| Operador | [Terminal do caixa](frontend/caixa.html) | Registrar itens, controlar atendimento, consultar o ID AnyDesk associado e enviar solicitações. |
| Supervisor/gestor | [Painel de operação](frontend/painel.html) | Gerenciar mercado e caixas, configurar IDs AnyDesk, acompanhar atendimentos e decidir solicitações. |
| Administrador | [Administração](frontend/admin.html) | Gerenciar usuários, perfis, ativação e auditoria. |

## Tecnologias

- Backend: Java 21, Spring Boot, Spring Security, Spring Data JPA e Hibernate.
- Banco padrão: SQLite em arquivo.
- Frontend: HTML5, CSS3 e JavaScript Vanilla.
- Execução: Maven ou Docker.

## Documentação

A documentação funcional, técnica e operacional está organizada em [`doc/`](doc/README.md):

- [Índice da documentação](doc/README.md)
- [Escopo do projeto](doc/escopo_do_projeto.md)
- [Requisitos de usuário](doc/requisitos_de_usuario.md)
- [Requisitos de sistema e API](doc/requisitos_de_sistema.md)
- [Guia de execução e publicação](doc/guia_de_execucao.md)
- [Contas de demonstração](doc/contas_de_demonstracao.md)

O modelo SQL de referência está em [database/schema.sql](database/schema.sql), e o dump para consulta no SQLiteCloud está em [database/filalivre-cloud.sql](database/filalivre-cloud.sql). Instruções de uso estão no [guia de execução](doc/guia_de_execucao.md).

## Equipe

Eduardo Gomes, Maicon Goulart, Heitor Hara e José Leandro.

Disciplina: Projeto Integrador  
Professor: André Lobo
