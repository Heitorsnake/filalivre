# Requisitos de Usuário - FilaLivre

## Atores

### Operador de caixa
Atende o cliente, registra itens, controla o atendimento e solicita autorização quando uma operação exige decisão superior. Consulta o resultado da própria solicitação.

### Supervisor/gestor de operação
Acompanha os caixas em situação normal ou de atenção, cadastra o mercado, compartilha o código de acesso, analisa solicitações, registra aprovação ou recusa, cadastra novos caixas e consulta relatórios. Supervisor e gestor representam a mesma conta operacional.

### Administrador do sistema
Mantém usuários, perfis e status de ativação. Consulta relatórios e auditoria, mas não executa decisões operacionais de solicitações.

## Requisitos funcionais

- **RU-01 - Login:** cada usuário acessa o sistema com e-mail e senha e recebe uma sessão conforme seu perfil.
- **RU-01A - Cadastro de perfil:** no cadastro público, o usuário escolhe operador ou supervisor/gestor. Administrador continua sendo criado apenas pela administração.
- **RU-02 - Terminal do operador:** o operador visualiza seu caixa, registra itens, inicia, pausa e finaliza o atendimento.
- **RU-02A - Acesso ao mercado:** o operador informa o código recebido do supervisor e visualiza somente os caixas do mercado associado.
- **RU-03 - Solicitação:** o operador informa tipo, produto, quantidade, valor e motivo para criar uma solicitação pendente.
- **RU-04 - Acompanhamento:** o operador visualiza o retorno da decisão no terminal.
- **RU-05 - Painel do supervisor/gestor:** o supervisor/gestor cadastra o mercado, visualiza caixas, status, operadores e solicitações pendentes.
- **RU-06 - Decisão:** supervisor e gestor podem analisar, aprovar ou recusar uma solicitação.
- **RU-07 - Relatórios:** gestor e administrador consultam totais e indicadores operacionais.
- **RU-08 - Administração:** administrador cria usuários, escolhe seus perfis e ativa ou desativa contas.
- **RU-09 - Auditoria:** o sistema registra login, solicitações, decisões e alterações administrativas.

## Fluxo principal

1. O supervisor/gestor cria o mercado e recebe um código de acesso.
2. O operador informa o código e entra no mercado correto.
3. O operador escolhe um caixa livre, inicia o atendimento e registra os produtos.
4. Quando necessário, envia uma solicitação ao supervisor/gestor.
5. O painel do supervisor/gestor apresenta a solicitação pendente.
6. O supervisor/gestor analisa e decide.
7. O sistema registra a decisão e atualiza o terminal do operador.
8. O administrador acompanha a operação por relatórios e auditoria.

## Critérios de aceite

- Usuários não podem acessar telas incompatíveis com seu perfil.
- O backend deve rejeitar chamadas sem a permissão correta, mesmo que a tela seja acessada diretamente.
- Toda decisão deve registrar responsável e horário.
- O cadastro e a alteração de usuários devem ficar restritos ao administrador.
- Um operador não pode visualizar caixas, solicitações ou relatórios de outro mercado.
- O código de mercado inválido deve ser recusado pelo backend.
