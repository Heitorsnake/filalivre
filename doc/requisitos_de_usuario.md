# Requisitos de Usuário - FilaLivre

**Instituição:** Instituto Federal de Mato Grosso (IFMT)  
**Turma:** 2º ano B de Informática  
**Integrantes:** Heitor Pernet, Maicon Goulart, Eduardo Gomes e José Leandro

---

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
- **RU-01B - Confirmação de Gmail:** contas cadastradas com endereço `@gmail.com` só podem entrar após confirmar o e-mail por um link enviado ao endereço informado.
- **RU-02 - Terminal do operador:** o operador visualiza seu caixa, registra itens, inicia, pausa e finaliza o atendimento.
- **RU-02A - Acesso ao mercado:** o operador informa o código recebido do supervisor e visualiza somente os caixas do mercado associado.
- **RU-03 - Solicitação:** o operador informa produto, quantidade, valor e motivo para solicitar remotamente o cancelamento de um item.
- **RU-04 - Acompanhamento:** o operador visualiza o retorno da decisão no terminal.
- **RU-05 - Painel remoto do supervisor/gestor:** o supervisor/gestor acompanha remotamente caixas, status, operadores e solicitações pendentes do mercado.
- **RU-05A - Acesso AnyDesk por caixa:** ao cadastrar ou editar um caixa, o supervisor/gestor pode informar opcionalmente o ID/endereço AnyDesk do computador do operador. A seção Acesso remoto do painel lista os caixas, exibe cada ID e oferece um atalho para abrir o cliente AnyDesk nesse endereço. O terminal do operador exibe o ID associado ao caixa selecionado.
- **RU-06 - Decisão:** supervisor e gestor podem analisar, aprovar ou recusar uma solicitação.
- **RU-07 - Relatórios:** gestor e administrador consultam totais e indicadores operacionais.
- **RU-08 - Administração:** administrador cria usuários, escolhe seus perfis e ativa ou desativa contas.
- **RU-09 - Auditoria:** o sistema registra login, solicitações, decisões e alterações administrativas.

## Fluxo principal

1. O supervisor/gestor cria o mercado e recebe um código de acesso.
2. O operador informa o código e entra no mercado correto.
3. O operador escolhe um caixa livre, inicia o atendimento e registra os produtos.
4. Quando necessário, envia uma solicitação de cancelamento de item ao supervisor/gestor.
5. O painel remoto do supervisor/gestor apresenta a solicitação pendente.
6. O supervisor/gestor analisa e decide sem se deslocar até o caixa.
7. O sistema registra a decisão e atualiza o terminal do operador.
8. O administrador acompanha a operação por relatórios e auditoria.

## Configuração de acesso AnyDesk

1. O operador ou responsável pelo computador do caixa informa ao gestor o ID/endereço mostrado no AnyDesk dessa máquina. O ID do próprio computador do gestor não deve ser usado nesse cadastro.
2. O gestor informa esse identificador no campo opcional do cadastro do caixa. Também pode adicioná-lo ou alterá-lo posteriormente pelo cartão do caixa.
3. O operador visualiza o identificador associado ao caixa selecionado no terminal.
4. Na seção **Acesso remoto** do painel, o gestor aciona **Conectar** junto ao caixa desejado para solicitar ao sistema operacional a abertura do cliente AnyDesk instalado no computador do gestor com o identificador do caixa.

O FilaLivre não obtém o ID automaticamente nem incorpora a imagem da tela remota. O link `anydesk:<ID>` solicita a abertura do cliente padrão instalado e o início da sessão. A conexão depende da disponibilidade do computador remoto e da autorização exigida pelo AnyDesk; clientes personalizados podem usar outro prefixo de URL handler.

## Critérios de aceite

- Usuários não podem acessar telas incompatíveis com seu perfil.
- O backend deve rejeitar chamadas sem a permissão correta, mesmo que a tela seja acessada diretamente.
- Toda decisão deve registrar responsável e horário.
- O cadastro e a alteração de usuários devem ficar restritos ao administrador.
- Um operador não pode visualizar caixas, solicitações ou relatórios de outro mercado.
- O código de mercado inválido deve ser recusado pelo backend.
- O ID AnyDesk é opcional, pertence ao computador do caixa/operador e deve ser retornado junto aos dados do caixa sem misturar dados de mercados diferentes.
- O atalho de conexão deve usar o ID do caixa e abrir o cliente AnyDesk; não deve apresentar a interface local do gestor como se fosse a tela remota.
