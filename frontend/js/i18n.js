(function () {
  let idioma = localStorage.getItem("filalivre-idioma") || "pt-BR";
  const textosOriginais = new WeakMap();
  const atributosOriginais = new WeakMap();
  const traducoes = {
    "FilaLivre — Central de Controle": "FilaLivre — Panel de control",
    "FilaLivre — Terminal do Caixa": "FilaLivre — Terminal de caja",
    "FilaLivre — Administração": "FilaLivre — Administración",
    "Central de Controle": "Panel de control",
    "Terminal de Caixa": "Terminal de caja",
    "Administração do sistema": "Administración del sistema",
    "Acesso remoto": "Acceso remoto",
    "Conecte-se aos computadores dos caixas usando o AnyDesk instalado neste computador.": "Conéctese a las computadoras de caja usando AnyDesk instalado en este equipo.",
    "Verificando conexão": "Verificando conexión",
    "Mercado": "Supermercado",
    "Cadastre o mercado para liberar seus caixas.": "Registre el supermercado para habilitar sus cajas.",
    "Nome do mercado": "Nombre del supermercado",
    "Cadastrar mercado": "Registrar supermercado",
    "Visão geral": "Resumen",
    "Senha inicial": "Contraseña inicial",
    "Preço (R$)": "Precio (R$)",
    "Relatório de solicitações": "Informe de solicitudes",
    "Consulte decisões e pedidos registados no período selecionado.": "Consulte las decisiones y solicitudes registradas en el período seleccionado.",
    "Data inicial": "Fecha de inicio",
    "Data final": "Fecha de fin",
    "Estado": "Estado",
    "Todos os estados": "Todos los estados",
    "Pendente": "Pendiente",
    "Aprovada": "Aprobada",
    "Recusada": "Rechazada",
    "Gerar relatório": "Generar informe",
    "Exportar CSV (compatível com Excel)": "Exportar CSV (compatible con Excel)",
    "Imprimir / guardar PDF": "Imprimir / guardar PDF",
    "A consultar os registos...": "Consultando los registros...",
    "Solicitações": "Solicitudes",
    "Resumo executivo": "Resumen ejecutivo",
    "Solicitações no período:": "Solicitudes en el período:",
    "Estado atual — pendentes:": "Estado actual — pendientes:",
    "aprovadas:": "aprobadas:",
    "recusadas:": "rechazadas:",
    "Taxa de aprovação entre decisões concluídas:": "Tasa de aprobación entre decisiones completadas:",
    "O período usa a data de criação. O estado apresentado é o atual; a taxa considera apenas solicitações decididas e todos os indicadores respeitam o filtro de estado.": "El período usa la fecha de creación. El estado mostrado es el actual; la tasa solo considera solicitudes decididas y todos los indicadores respetan el filtro de estado.",
    "Aprovação": "Aprobación",
    "Tempo médio": "Tiempo medio",
    "Solicitações por dia": "Solicitudes por día",
    "Nenhuma solicitação encontrada neste período.": "No se encontraron solicitudes en este período.",
    "Decisão": "Decisión",
    "Valor": "Valor",
    "Gerado em": "Generado el",
    "Período:": "Período:",
    "solicitação(ões) encontradas.": "solicitud(es) encontradas.",
    "Não foi possível carregar o histórico.": "No se pudo cargar el historial.",
    "Não foi possível carregar o resumo.": "No se pudo cargar el resumen.",
    "Não foi possível carregar as caixas.": "No se pudieron cargar las cajas.",
    "A data inicial não pode ser posterior à data final": "La fecha de inicio no puede ser posterior a la fecha de fin",
    "O período máximo do relatório é de 366 dias": "El período máximo del informe es de 366 días",
    "Pendentes": "Pendientes",
    "Aprovadas": "Aprobadas",
    "Recusadas": "Rechazadas",
    "Tempo médio decisão": "Tiempo medio de decisión",
    "Caixas em atenção": "Cajas que requieren atención",
    "Solicitações pendentes de decisão": "Solicitudes pendientes de decisión",
    "Painel de caixas": "Panel de cajas",
    "Histórico de ações (auditoria)": "Historial de acciones (auditoría)",
    "Momento": "Fecha y hora",
    "Usuário": "Usuario",
    "Caixa": "Caja",
    "Ação": "Acción",
    "Detalhes": "Detalles",
    "CONFIGURAÇÃO DE TERMINAL": "CONFIGURACIÓN DE TERMINAL",
    "Cadastrar novo caixa": "Registrar nueva caja",
    "Defina a identificação do caixa. Para configurar o acesso remoto, peça ao operador o ID mostrado no AnyDesk do computador do caixa.": "Defina la identificación de la caja. Para configurar el acceso remoto, pida al operador el ID que aparece en AnyDesk en la computadora de caja.",
    "Nome/localização do caixa": "Nombre/ubicación de la caja",
    "Ex.: Perto do banheiro": "Ej.: Cerca del baño",
    "O número será gerado automaticamente pelo sistema.": "El sistema generará el número automáticamente.",
    "ID AnyDesk do computador do operador": "ID de AnyDesk de la computadora del operador",
    "opcional": "opcional",
    "ID do computador do caixa": "ID de la computadora de caja",
    "Digite o ID que aparece no AnyDesk da pessoa que será acessada. Não use o ID do computador do gestor. Pode preencher depois.": "Escriba el ID que aparece en AnyDesk de la persona a la que se accederá. No use el ID de la computadora del gerente. Puede completarlo después.",
    "Cancelar": "Cancelar",
    "Cadastrar": "Registrar",
    "Novo caixa": "Nueva caja",
    "Sair": "Salir",
    "Seu mercado": "Su supermercado",
    "Nenhum mercado selecionado.": "No hay ningún supermercado seleccionado.",
    "Código do mercado": "Código del supermercado",
    "Entrar no mercado": "Acceder al supermercado",
    "ID AnyDesk deste computador": "ID de AnyDesk de esta computadora",
    "Salvar ID AnyDesk": "Guardar ID de AnyDesk",
    "Computador do caixa selecionado": "Computadora de la caja seleccionada",
    "Selecione um caixa": "Seleccione una caja",
    "Seu caixa": "Su caja",
    "Carregando…": "Cargando…",
    "Total da compra": "Total de la compra",
    "Itens registrados": "Artículos registrados",
    "Tempo de atendimento": "Tiempo de atención",
    "Iniciar atendimento": "Iniciar atención",
    "Colocar em espera / Retomar": "Poner en espera / Reanudar",
    "Finalizar venda": "Finalizar venta",
    "Solicitar autorização": "Solicitar autorización",
    "Carrinho da compra (simulação)": "Carrito de compra (simulación)",
    "Produto (ex.: Arroz 5kg)": "Producto (ej.: arroz 5 kg)",
    "Preço R$": "Precio R$",
    "Qtd": "Cant.",
    "Registrar": "Registrar",
    "Nenhum produto registrado ainda.": "Aún no hay productos registrados.",
    "Solicitar autorização ao gestor": "Solicitar autorización al gerente",
    "Tipo de solicitação": "Tipo de solicitud",
    "Cancelamento de item": "Cancelación de artículo",
    "Produto": "Producto",
    "Ex.: Arroz 5kg": "Ej.: arroz 5 kg",
    "Quantidade": "Cantidad",
    "Valor (R$)": "Valor (R$)",
    "Motivo da solicitação": "Motivo de la solicitud",
    "Ex.: cliente desistiu do produto": "Ej.: el cliente ya no quiere el producto",
    "Enviar solicitação": "Enviar solicitud",
    "Visão administrativa": "Resumen administrativo",
    "Usuários ativos": "Usuarios activos",
    "Manutenção de usuários e perfis": "Administración de usuarios y perfiles",
    "Nome completo": "Nombre completo",
    "E-mail": "Correo electrónico",
    "Senha (mínimo 12 caracteres)": "Contraseña (mínimo 12 caracteres)",
    "Operador": "Cajero",
    "Supervisor": "Supervisor",
    "Supervisor / gestor": "Supervisor / gerente",
    "Administrador": "Administrador",
    "Criar usuário": "Crear usuario",
    "Nome": "Nombre",
    "Perfil": "Perfil",
    "Status": "Estado",
    "Ação": "Acción",
    "Caixas cadastrados": "Cajas registradas",
    "Auditoria do sistema": "Auditoría del sistema",
    "Ex.: Entrada principal": "Ej.: entrada principal",
    "Criar caixa": "Crear caja",
    "Ativo": "Activo",
    "Inativo": "Inactivo",
    "Editar local": "Editar ubicación",
    "Desativar": "Desactivar",
    "Ativar": "Activar",
    "Localização": "Ubicación",
    "Não informada": "No informada",
    "Não identificado": "No identificado",
    "Valor da compra": "Valor de la compra",
    "Itens": "Artículos",
    "Caixa em espera": "Caja en espera",
    "Espera removida": "Espera finalizada",
    "Início de atendimento": "Inicio de atención",
    "Fim de atendimento": "Fin de atención",
    "Solicitação enviada": "Solicitud enviada",
    "Análise iniciada": "Análisis iniciado",
    "Solicitação APROVADA": "Solicitud APROBADA",
    "Solicitação RECUSADA": "Solicitud RECHAZADA",
    "Usuário criado": "Usuario creado",
    "Usuário ativado": "Usuario activado",
    "Usuário desativado": "Usuario desactivado",
    "Login": "Inicio de sesión",
    "Solicitação criada": "Solicitud creada",
    "Solicitação aprovada": "Solicitud aprobada",
    "Solicitação recusada": "Solicitud rechazada",
    "Nenhuma solicitação pendente. Tudo sob controle.": "No hay solicitudes pendientes. Todo está bajo control.",
    "Analisar": "Analizar",
    "Aprovar": "Aprobar",
    "Recusar": "Rechazar",
    "Operador:": "Cajero:",
    "Solicitado às": "Solicitado a las",
    "Motivo:": "Motivo:",
    "Conectado ao painel remoto": "Conectado al panel remoto",
    "Sem conexão com o servidor": "Sin conexión con el servidor",
    "Cancelamento": "Cancelación",
    "Tipo legado": "Tipo anterior",
    "Análise registrada. O caixa está em aprovação.": "Análisis registrado. La caja está pendiente de aprobación.",
    "Solicitação aprovada e comunicada ao caixa.": "Solicitud aprobada y comunicada a la caja.",
    "Solicitação recusada.": "Solicitud rechazada.",
    "ID AnyDesk": "ID de AnyDesk",
    "Não configurado": "No configurado",
    "Sem acesso configurado": "Acceso no configurado",
    "Conectar": "Conectar",
    "Editar ID": "Editar ID",
    "Adicionar ID": "Añadir ID",
    "PENDENTE": "PENDIENTE",
    "Localização atualizada": "Ubicación actualizada",
    "Caixa desativado": "Caja desactivada",
    "Desativar este caixa? O número será preservado no histórico.": "¿Desactivar esta caja? El número se conservará en el historial.",
    "Nova localização do caixa:": "Nueva ubicación de la caja:",
    "ID ou endereço AnyDesk do computador do caixa (deixe vazio para remover):": "ID o dirección de AnyDesk de la computadora de caja (déjelo vacío para quitarlo):",
    "Acesso AnyDesk atualizado": "Acceso de AnyDesk actualizado",
    "Acesso AnyDesk removido": "Acceso de AnyDesk eliminado",
    "Não informada": "No informada",
    "Mercado cadastrado. Compartilhe o código com os operadores.": "Supermercado registrado. Comparta el código con los cajeros.",
    "Caixa cadastrado com sucesso": "Caja registrada correctamente",
    "Caixa criado com numeração automática": "Caja creada con numeración automática",
    "Usuário criado com sucesso": "Usuario creado correctamente",
    "Status do usuário atualizado": "Estado del usuario actualizado",
    "Digite o código fornecido pelo gestor para acessar um mercado.": "Introduzca el código proporcionado por el gerente para acceder a un supermercado.",
    "Mercado conectado": "Conectado al supermercado",
    "ID AnyDesk atualizado": "ID de AnyDesk actualizado",
    "Venda finalizada com sucesso": "Venta finalizada correctamente",
    "Solicitação enviada ao gestor": "Solicitud enviada al gerente",
    "Inicie o atendimento antes de registrar produtos": "Inicie la atención antes de registrar productos",
    "Selecione um caixa primeiro": "Seleccione una caja primero",
    "Acesso remoto não configurado": "Acceso remoto no configurado",
    "+ Novo caixa": "+ Nueva caja",
    "Solicitação de": "Solicitud de",
    "· Solicitado às": "· Solicitado a las",
    "SOLICITACAO": "SOLICITUD",
    "APROVACAO": "APROBACIÓN",
    "SUPERVISOR": "SUPERVISOR",
    "OPERADOR": "CAJERO",
    "ADMINISTRADOR": "ADMINISTRADOR",
    "NORMAL": "NORMAL",
    "GERENTE": "GERENTE",
    "(1) Solicitação! — FILALIVRE": "(1) ¡Solicitud! — FILALIVRE",
    "EM ATENDIMENTO": "EN ATENCIÓN",
    "LIVRE": "LIBRE",
    "AGUARDANDO": "EN ESPERA",
    "SOLICITAÇÃO EM ANÁLISE": "SOLICITUD EN ANÁLISIS",
    "APROVAÇÃO PENDENTE": "APROBACIÓN PENDIENTE",
    "Fechar": "Cerrar",
    "APROVADA": "APROBADA",
    "RECUSADA": "RECHAZADA",
    "pelo gestor": "por el gerente",
    "Você já pode concluir a operação.": "Ya puede completar la operación.",
    "A operação não será autorizada.": "La operación no será autorizada.",
    "Digite o ID fornecido pelo gestor para acessar um mercado.": "Introduzca el ID proporcionado por el gerente para acceder a un supermercado.",
    "Nova solicitação no Caixa": "Nueva solicitud en la caja",
    "Erro": "Error",
    "Sessão expirada. Faça login novamente.": "La sesión expiró. Inicie sesión nuevamente.",
    "Não foi possível iniciar uma sessão segura.": "No se pudo iniciar una sesión segura."
  };

  const traducoesIngles = {
    "FilaLivre — Acesso": "FilaLivre — Sign in",
    "FilaLivre — Central de Controle": "FilaLivre — Control Panel",
    "FilaLivre — Terminal do Caixa": "FilaLivre — Checkout Terminal",
    "FilaLivre — Administração": "FilaLivre — Administration",
    "Central de Controle": "Control Panel",
    "Terminal de Caixa": "Checkout Terminal",
    "Administração do sistema": "System Administration",
    "Sistema de gerenciamento remoto de caixas de supermercado. Solicitações de cancelamento analisadas pelo supervisor sem deslocamento físico.": "Remote supermarket checkout management system. Cancellation requests are reviewed by a supervisor without needing to be on site.",
    "Entrar": "Sign in",
    "Criar conta": "Create account",
    "E-mail": "Email",
    "Senha": "Password",
    "Nome completo": "Full name",
    "Senha (mínimo 12 caracteres)": "Password (at least 12 characters)",
    "Tipo de conta": "Account type",
    "Operador de caixa": "Checkout operator",
    "Supervisor / gestor do mercado": "Supervisor / store manager",
    "Supervisor / gestor": "Supervisor / manager",
    "Administrador": "Administrator",
    "Acesso remoto": "Remote access",
    "Conecte-se aos computadores dos caixas usando o AnyDesk instalado neste computador.": "Connect to checkout computers using AnyDesk installed on this computer.",
    "Verificando conexão": "Checking connection",
    "Mercado": "Store",
    "Cadastre o mercado para liberar seus caixas.": "Register your store to enable its checkouts.",
    "Nome do mercado": "Store name",
    "Cadastrar mercado": "Register store",
    "Visão geral": "Overview",
    "Senha inicial": "Initial password",
    "Preço (R$)": "Price (R$)",
    "Relatório de solicitações": "Request report",
    "Consulte decisões e pedidos registados no período selecionado.": "Review decisions and requests recorded during the selected period.",
    "Data inicial": "Start date",
    "Data final": "End date",
    "Estado": "Status",
    "Todos os estados": "All statuses",
    "Pendente": "Pending",
    "Aprovada": "Approved",
    "Recusada": "Rejected",
    "Gerar relatório": "Generate report",
    "Exportar CSV (compatível com Excel)": "Export CSV (Excel compatible)",
    "Imprimir / guardar PDF": "Print / save PDF",
    "Solicitações": "Requests",
    "Resumo executivo": "Executive summary",
    "Solicitações no período:": "Requests in period:",
    "Estado atual — pendentes:": "Current status — pending:",
    "aprovadas:": "approved:",
    "recusadas:": "rejected:",
    "Taxa de aprovação entre decisões concluídas:": "Approval rate among completed decisions:",
    "Aprovação": "Approval",
    "Tempo médio": "Average time",
    "Solicitações por dia": "Requests per day",
    "Nenhuma solicitação encontrada neste período.": "No requests found for this period.",
    "Decisão": "Decision",
    "Gerado em": "Generated on",
    "Período:": "Period:",
    "Pendentes": "Pending",
    "Aprovadas": "Approved",
    "Recusadas": "Rejected",
    "Tempo médio decisão": "Average decision time",
    "Caixas em atenção": "Checkouts needing attention",
    "Solicitações pendentes de decisão": "Requests awaiting a decision",
    "Painel de caixas": "Checkout dashboard",
    "Histórico de ações (auditoria)": "Activity log (audit)",
    "Momento": "Time",
    "Usuário": "User",
    "Caixa": "Checkout",
    "Ação": "Action",
    "Detalhes": "Details",
    "CONFIGURAÇÃO DE TERMINAL": "TERMINAL SETUP",
    "Cadastrar novo caixa": "Register new checkout",
    "Nome/localização do caixa": "Checkout name/location",
    "O número será gerado automaticamente pelo sistema.": "The number will be generated automatically.",
    "ID AnyDesk do computador do operador": "Operator computer AnyDesk ID",
    "opcional": "optional",
    "ID do computador do caixa": "Checkout computer ID",
    "Cancelar": "Cancel",
    "Cadastrar": "Register",
    "Novo caixa": "New checkout",
    "Sair": "Sign out",
    "Seu mercado": "Your store",
    "Nenhum mercado selecionado.": "No store selected.",
    "Código do mercado": "Store code",
    "Entrar no mercado": "Join store",
    "Salvar ID AnyDesk": "Save AnyDesk ID",
    "Computador do caixa selecionado": "Selected checkout computer",
    "Selecione um caixa": "Select a checkout",
    "Seu caixa": "Your checkout",
    "Carregando…": "Loading…",
    "Total da compra": "Purchase total",
    "Itens registrados": "Items scanned",
    "Tempo de atendimento": "Service time",
    "Iniciar atendimento": "Start service",
    "Colocar em espera / Retomar": "Put on hold / Resume",
    "Finalizar venda": "Complete sale",
    "Solicitar autorização": "Request approval",
    "Carrinho da compra (simulação)": "Shopping cart (simulation)",
    "Produto (ex.: Arroz 5kg)": "Product (e.g., rice 5 kg)",
    "Preço R$": "Price (R$)",
    "Qtd": "Qty",
    "Registrar": "Add",
    "Nenhum produto registrado ainda.": "No products added yet.",
    "Solicitar autorização ao gestor": "Request manager approval",
    "Tipo de solicitação": "Request type",
    "Cancelamento de item": "Item cancellation",
    "Produto": "Product",
    "Quantidade": "Quantity",
    "Valor (R$)": "Amount (R$)",
    "Motivo da solicitação": "Reason for request",
    "Enviar solicitação": "Send request",
    "Visão administrativa": "Admin overview",
    "Usuários ativos": "Active users",
    "Manutenção de usuários e perfis": "User and role management",
    "Perfil": "Role",
    "Status": "Status",
    "Caixas cadastrados": "Registered checkouts",
    "Auditoria do sistema": "System audit log",
    "Criar caixa": "Create checkout",
    "Ativo": "Active",
    "Inativo": "Inactive",
    "Editar local": "Edit location",
    "Desativar": "Deactivate",
    "Ativar": "Activate",
    "Localização": "Location",
    "Não informada": "Not provided",
    "Não identificado": "Not identified",
    "Valor da compra": "Purchase amount",
    "Itens": "Items",
    "Caixa em espera": "Checkout on hold",
    "Espera removida": "Checkout resumed",
    "Início de atendimento": "Service started",
    "Fim de atendimento": "Service ended",
    "Solicitação enviada": "Request sent",
    "Análise iniciada": "Review started",
    "Solicitação APROVADA": "Request APPROVED",
    "Solicitação RECUSADA": "Request REJECTED",
    "Usuário criado": "User created",
    "Usuário ativado": "User activated",
    "Usuário desativado": "User deactivated",
    "Login": "Sign in",
    "Solicitação criada": "Request created",
    "Solicitação aprovada": "Request approved",
    "Solicitação recusada": "Request rejected",
    "Nenhuma solicitação pendente. Tudo sob controle.": "No pending requests. Everything is under control.",
    "Analisar": "Review",
    "Aprovar": "Approve",
    "Recusar": "Reject",
    "Operador:": "Operator:",
    "Solicitado às": "Requested at",
    "Motivo:": "Reason:",
    "Conectado ao painel remoto": "Connected to remote panel",
    "Sem conexão com o servidor": "No connection to server",
    "Cancelamento": "Cancellation",
    "Fechar": "Close",
    "APROVADA": "APPROVED",
    "RECUSADA": "REJECTED",
    "pelo gestor": "by the manager",
    "Você já pode concluir a operação.": "You can now complete the transaction.",
    "A operação não será autorizada.": "The transaction will not be approved.",
    "Digite o ID fornecido pelo gestor para acessar um mercado.": "Enter the ID provided by the manager to access a store.",
    "Erro": "Error",
    "Sessão expirada. Faça login novamente.": "Your session has expired. Please sign in again.",
    "Não foi possível iniciar uma sessão segura.": "Could not start a secure session.",
    "A data inicial não pode ser posterior à data final": "The start date cannot be after the end date",
    "O período máximo do relatório é de 366 dias": "The maximum report period is 366 days",
    "Selecione um caixa primeiro": "Select a checkout first",
    "Acesso remoto não configurado": "Remote access is not configured",
    "+ Novo caixa": "+ New checkout",
    "SOLICITACAO": "REQUEST",
    "APROVACAO": "APPROVAL",
    "OPERADOR": "OPERATOR",
    "ADMINISTRADOR": "ADMINISTRATOR",
    "(1) Solicitação! — FILALIVRE": "(1) Request! — FILALIVRE",
    "EM ATENDIMENTO": "IN SERVICE",
    "LIVRE": "AVAILABLE",
    "AGUARDANDO": "WAITING",
    "SOLICITAÇÃO EM ANÁLISE": "REQUEST UNDER REVIEW",
    "APROVAÇÃO PENDENTE": "APPROVAL PENDING",
    "A consultar os registos...": "Loading records...",
    "O período usa a data de criação. O estado apresentado é o atual; a taxa considera apenas solicitações decididas e todos os indicadores respeitam o filtro de estado.": "The period uses the creation date. The displayed status is current; the rate includes only decided requests, and all metrics follow the selected status filter.",
    "Valor": "Amount",
    "solicitação(ões) encontradas.": "request(s) found.",
    "Não foi possível carregar o histórico.": "Could not load the activity log.",
    "Não foi possível carregar o resumo.": "Could not load the summary.",
    "Não foi possível carregar as caixas.": "Could not load the checkouts.",
    "Defina a identificação do caixa. Para configurar o acesso remoto, peça ao operador o ID mostrado no AnyDesk do computador do caixa.": "Set a checkout identifier. To configure remote access, ask the operator for the AnyDesk ID shown on the checkout computer.",
    "Ex.: Perto do banheiro": "e.g., Near the restroom",
    "Digite o ID que aparece no AnyDesk da pessoa que será acessada. Não use o ID do computador do gestor. Pode preencher depois.": "Enter the AnyDesk ID shown on the computer to be accessed. Do not use the manager's computer ID. You can fill this in later.",
    "ID AnyDesk deste computador": "This computer's AnyDesk ID",
    "Não foi possível carregar": "Could not load",
    "Análise registrada. O caixa está em aprovação.": "Review recorded. The checkout is awaiting approval.",
    "Solicitação aprovada e comunicada ao caixa.": "Request approved and the checkout has been notified.",
    "Solicitação recusada.": "Request rejected.",
    "Mercado cadastrado. Compartilhe o código com os operadores.": "Store registered. Share the code with the operators.",
    "Caixa cadastrado com sucesso": "Checkout registered successfully",
    "Caixa criado com numeração automática": "Checkout created with an automatic number",
    "Usuário criado com sucesso": "User created successfully",
    "Status do usuário atualizado": "User status updated",
    "Digite o código fornecido pelo gestor para acessar um mercado.": "Enter the code provided by the manager to access a store.",
    "Mercado conectado": "Connected to store",
    "ID AnyDesk atualizado": "AnyDesk ID updated",
    "Venda finalizada com sucesso": "Sale completed successfully",
    "Solicitação enviada ao gestor": "Request sent to the manager",
    "Inicie o atendimento antes de registrar produtos": "Start service before adding products",
    "Solicitação de": "Request from",
    "· Solicitado às": "· Requested at",
    "SUPERVISOR": "SUPERVISOR",
    "NORMAL": "NORMAL",
    "GERENTE": "MANAGER"
  };

  function traduzirTexto(texto) {
    const mapa = idioma === "es" ? traducoes : idioma === "en-US" ? traducoesIngles : null;
    if (!mapa) return texto;
    const inicio = texto.match(/^\s*/)[0];
    const fim = texto.match(/\s*$/)[0];
    const conteudo = texto.trim();
    if (mapa[conteudo]) return `${inicio}${mapa[conteudo]}${fim}`;
    const caixa = conteudo.match(/^Caixa (\d+)(.*)$/);
    if (caixa) {
      if (idioma === "en-US") return `${inicio}Checkout ${caixa[1]}${caixa[2]}${fim}`;
      const complemento = caixa[2]
        .replace(" — você", " — usted")
        .replace(" — livre", " — libre")
        .replace(" — Operador não identificado", " — Cajero no identificado");
      return `${inicio}Caja ${caixa[1]}${complemento}${fim}`;
    }
    const novoPedido = conteudo.match(/^Nova solicitação no Caixa (\d+): (.*)$/);
    if (idioma === "en-US" && novoPedido) {
      return `${inicio}New request at checkout ${novoPedido[1]}: ${novoPedido[2]}${fim}`;
    }
    if (novoPedido) return `${inicio}Nueva solicitud en la caja ${novoPedido[1]}: ${novoPedido[2]}${fim}`;
    if (conteudo.includes(" · código de acesso: ")) {
      if (idioma === "en-US") return `${inicio}${conteudo.replace(" · código de acesso: ", " · access code: ")}${fim}`;
      return `${inicio}${conteudo.replace(" · código de acesso: ", " · código de acceso: ")}${fim}`;
    }
    if (/^Você assumiu o Caixa \d+$/.test(conteudo)) {
      if (idioma === "en-US") return `${inicio}${conteudo.replace("Você assumiu o Caixa", "You are now assigned to checkout")}${fim}`;
      return `${inicio}${conteudo.replace("Você assumiu o Caixa", "Ahora está a cargo de la caja")}${fim}`;
    }
    return texto;
  }

  function traduzirElemento(elemento) {
    if (elemento.nodeType === Node.TEXT_NODE) {
      if (!textosOriginais.has(elemento)) textosOriginais.set(elemento, elemento.nodeValue);
      const original = textosOriginais.get(elemento);
      const traduzido = traduzirTexto(original);
      if (traduzido !== elemento.nodeValue) elemento.nodeValue = traduzido;
      return;
    }
    if (elemento.nodeType !== Node.ELEMENT_NODE && elemento !== document) return;
    const raiz = elemento.nodeType === Node.ELEMENT_NODE ? elemento : document.documentElement;
    const traduzirAtributo = (item, nome) => {
      let originais = atributosOriginais.get(item);
      if (!originais) {
        originais = new Map();
        atributosOriginais.set(item, originais);
      }
      if (!originais.has(nome)) originais.set(nome, item.getAttribute(nome));
      const original = originais.get(nome);
      const valor = idioma === "es" ? traduzirTexto(original) : original;
      if (item.getAttribute(nome) !== valor) item.setAttribute(nome, valor);
    };
    const arvore = document.createTreeWalker(raiz, NodeFilter.SHOW_TEXT);
    while (arvore.nextNode()) traduzirElemento(arvore.currentNode);
    if (raiz.matches?.("input[placeholder], textarea[placeholder]")) {
      traduzirAtributo(raiz, "placeholder");
    }
    raiz.querySelectorAll?.("input[placeholder], textarea[placeholder]").forEach(campo => {
      traduzirAtributo(campo, "placeholder");
    });
    if (raiz.matches?.("[title]")) traduzirAtributo(raiz, "title");
    raiz.querySelectorAll?.("[title]").forEach(item => traduzirAtributo(item, "title"));
    if (raiz.matches?.("[aria-label]")) traduzirAtributo(raiz, "aria-label");
    raiz.querySelectorAll?.("[aria-label]").forEach(item => {
      traduzirAtributo(item, "aria-label");
    });
  }

  window.idiomaAtual = idioma;
  window.aplicarIdioma = function (novoIdioma) {
    idioma = ["es", "en-US"].includes(novoIdioma) ? novoIdioma : "pt-BR";
    window.idiomaAtual = idioma;
    localStorage.setItem("filalivre-idioma", idioma);
    document.documentElement.lang = idioma;
    document.querySelectorAll(".idioma-opcao").forEach(botao => {
      const selecionado = botao.dataset.idioma === idioma;
      botao.classList.toggle("ativa", selecionado);
      botao.setAttribute("aria-pressed", String(selecionado));
    });
    document.querySelectorAll(".idioma-controle").forEach(controle => {
      controle.setAttribute("aria-label", idioma === "en-US" ? "Language" : idioma === "es" ? "Idioma" : "Idioma");
    });
    traduzirElemento(document);
    atualizarRotulosTema();
  };
  document.querySelectorAll(".idioma-opcao").forEach(botao => {
    botao.addEventListener("click", () => window.aplicarIdioma(botao.dataset.idioma));
  });
  window.aplicarIdioma(idioma);

  function atualizarRotulosTema() {
    const claro = document.documentElement.dataset.tema === "claro";
    document.querySelectorAll("[data-theme-toggle]").forEach(botao => {
      const rotulo = claro ? "Ativar modo escuro" : "Ativar modo claro";
      const rotuloIngles = claro ? "Switch to dark mode" : "Switch to light mode";
      const rotuloEspanhol = claro ? "Activar modo oscuro" : "Activar modo claro";
      const acessivel = idioma === "en-US" ? rotuloIngles : idioma === "es" ? rotuloEspanhol : rotulo;
      botao.setAttribute("aria-label", acessivel);
      botao.setAttribute("title", acessivel);
      botao.setAttribute("aria-pressed", String(claro));
    });
  }
  function aplicarTema(tema) {
    const claro = tema === "claro";
    document.documentElement.dataset.tema = claro ? "claro" : "escuro";
    localStorage.setItem("filalivre-tema", claro ? "claro" : "escuro");
    atualizarRotulosTema();
  }
  window.aplicarTema = aplicarTema;
  aplicarTema(localStorage.getItem("filalivre-tema") || "escuro");
  document.querySelectorAll("[data-theme-toggle]").forEach(botao => {
    botao.addEventListener("click", () => {
      aplicarTema(document.documentElement.dataset.tema === "claro" ? "escuro" : "claro");
    });
  });

  const observador = new MutationObserver(mudancas => {
    mudancas.forEach(mudanca => {
      if (mudanca.type === "characterData") traduzirElemento(mudanca.target);
      mudanca.addedNodes.forEach(no => traduzirElemento(no));
    });
  });
  observador.observe(document.documentElement, { childList: true, subtree: true, characterData: true });
})();