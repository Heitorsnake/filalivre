# Escopo do Projeto - FilaLivre

## Objetivo

O FilaLivre reduz o tempo de espera nos caixas ao permitir que solicitações de cancelamento, desconto e cupom sejam analisadas remotamente pelo supervisor, que exerce a função de gestor. O sistema registra cada ação para manter rastreabilidade operacional e separa a operação por mercado.

## Escopo funcional

- Autenticação com sessão e controle de acesso por perfil.
- Cadastro público escolhendo entre operador e supervisor/gestor.
- Cadastro de mercado pelo supervisor e geração de código de acesso.
- Entrada do operador no mercado por código.
- Operação de caixas, carrinho simulado e acompanhamento do atendimento.
- Criação de solicitações pelo operador.
- Análise, aprovação ou recusa pelo gestor/supervisor.
- Administração de usuários, perfis e status de ativação.
- Relatórios resumidos e histórico de auditoria.

## Fora do escopo

- Processamento real de pagamentos.
- Integração com estoque, ERP ou fiscal.
- Aplicativo mobile nativo.
- Envio externo de notificações por e-mail ou SMS.

## Perfis

- **Operador:** atua no terminal do caixa e envia solicitações.
- **Supervisor/gestor:** cadastra o mercado, acompanha os caixas, decide solicitações, cadastra caixas e consulta relatórios.
- **Administrador:** gerencia usuários, perfis, ativação e auditoria.

## Arquitetura e tecnologias

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA e Hibernate.
- **Persistência:** SQLite em `database/filalivre.db`, com SQL de referência em `database/schema.sql` e dump de demonstração em `database/filalivre-cloud.sql`.
- **Frontend:** HTML5, CSS3 e JavaScript Vanilla para interação com a API REST.
- **Execução:** Maven localmente ou Docker com Java 21.

## Objetivos de engenharia

1. Reduzir o tempo de decisão das solicitações.
2. Centralizar a visualização dos caixas e das ocorrências.
3. Registrar decisões, acessos e alterações para auditoria.
4. Manter separação real entre as permissões de operador, gestor e administrador.
5. Isolar caixas, solicitações e relatórios pelo mercado associado ao usuário.
