# Contas de demonstração

**Instituição:** Instituto Federal de Mato Grosso (IFMT)  
**Turma:** 2º ano B de Informática  
**Integrantes:** Heitor Pernet, Maicon Goulart, Eduardo Gomes e José Leandro

---

Estas contas são criadas automaticamente ao iniciar com `mvn spring-boot:run` (perfil local `demo`). No Docker, `FILALIVRE_DEMO_DATA` permanece desativado por padrão e deve ser habilitado explicitamente apenas em ambientes de demonstração isolados. Enquanto a opção estiver ativa, o seed restaura as senhas, perfis e o estado ativo destas contas a cada inicialização, inclusive se elas já existirem no banco. Não habilite a opção em serviços expostos à internet:

| Perfil | E-mail | Senha | Mercado | Código |
|---|---|---|---|---|
| Administrador | `admin@filalivre.com` | `admin123` | - | - |
| Supervisor / gestor | `supervisor@filalivre.com` | `supervisor123` | Mercado Demonstração | `MERCADO1` |
| Operador | `operador@filalivre.com` | `operador123` | Mercado Demonstração | `MERCADO1` |

Para uma instalação de produção, mantenha `FILALIVRE_DEMO_DATA=false` e crie a primeira conta administrativa pelas variáveis secretas `FILALIVRE_ADMIN_EMAIL` e `FILALIVRE_ADMIN_PASSWORD` antes do primeiro início do backend. A senha inicial deve conter pelo menos 12 caracteres. Para autenticar o operador de demonstração, associe-o ao mercado usando o código indicado.
