# Requisitos de Usuário - FilaLivre

## Atores

### Operador de caixa
Atende o cliente, registra itens, controla o atendimento e solicita autorização quando uma operação exige decisão superior. Consulta o resultado da própria solicitação.

### Supervisor/Gestor de operação
Acompanha os caixas em situação normal ou de atenção, analisa solicitações e registra aprovação ou recusa. O gestor também pode cadastrar novos caixas e consultar relatórios.

### Administrador do sistema
Mantém usuários, perfis e status de ativação. Consulta relatórios e auditoria, mas não executa decisões operacionais de solicitações.

## Requisitos funcionais

- **RU-01 - Login:** cada usuário acessa o sistema com e-mail e senha e recebe uma sessão conforme seu perfil.
- **RU-02 - Terminal do operador:** o operador visualiza seu caixa, registra itens, inicia, pausa e finaliza o atendimento.
- **RU-03 - Solicitação:** o operador informa tipo, produto, quantidade, valor e motivo para criar uma solicitação pendente.
- **RU-04 - Acompanhamento:** o operador visualiza o retorno da decisão no terminal.
- **RU-05 - Painel do gestor:** supervisor e gestor visualizam caixas, status, operadores e solicitações pendentes.
- **RU-06 - Decisão:** supervisor e gestor podem analisar, aprovar ou recusar uma solicitação.
- **RU-07 - Relatórios:** gestor e administrador consultam totais e indicadores operacionais.
- **RU-08 - Administração:** administrador cria usuários, escolhe seus perfis e ativa ou desativa contas.
- **RU-09 - Auditoria:** o sistema registra login, solicitações, decisões e alterações administrativas.

## Fluxo principal

1. O operador inicia o atendimento e registra os produtos.
2. Quando necessário, envia uma solicitação ao gestor.
3. O painel do gestor apresenta a solicitação pendente.
4. O gestor ou supervisor analisa e decide.
5. O sistema registra a decisão e atualiza o terminal do operador.
6. O administrador acompanha a operação por relatórios e auditoria.

## Critérios de aceite

- Usuários não podem acessar telas incompatíveis com seu perfil.
- O backend deve rejeitar chamadas sem a permissão correta, mesmo que a tela seja acessada diretamente.
- Toda decisão deve registrar responsável e horário.
- O cadastro e a alteração de usuários devem ficar restritos ao administrador.
