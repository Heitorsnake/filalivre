# Contas de demonstração

**Instituição:** Instituto Federal de Mato Grosso (IFMT)  
**Turma:** 2º ano B de Informática  
**Integrantes:** Heitor Pernet, Maicon Goulart, Eduardo Gomes e José Leandro

---

Estas contas só são criadas quando `FILALIVRE_DEMO_DATA=true` estiver definido antes da primeira inicialização do banco. Não habilite essa opção em produção:

| Perfil | E-mail | Senha | Mercado | Código |
|---|---|---|---|---|
| Administrador | `admin@filalivre.com` | `admin123` | - | - |
| Supervisor / gestor | `supervisor@filalivre.com` | `supervisor123` | Mercado Demonstração | `MERCADO1` |
| Operador | `operador@filalivre.com` | `operador123` | Mercado Demonstração | `MERCADO1` |

Para uma instalação de produção, crie a primeira conta administrativa pelas variáveis secretas `FILALIVRE_ADMIN_EMAIL` e `FILALIVRE_ADMIN_PASSWORD` antes do primeiro início do backend. A senha inicial deve conter pelo menos 12 caracteres. Para autenticar o operador de demonstração, associe-o ao mercado usando o código indicado.
