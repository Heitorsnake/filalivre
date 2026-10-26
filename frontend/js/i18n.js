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

  function traduzirTexto(texto) {
    if (idioma !== "es") return texto;
    const inicio = texto.match(/^\s*/)[0];
    const fim = texto.match(/\s*$/)[0];
    const conteudo = texto.trim();
    if (traducoes[conteudo]) return `${inicio}${traducoes[conteudo]}${fim}`;
    const caixa = conteudo.match(/^Caixa (\d+)(.*)$/);
    if (caixa) {
      const complemento = caixa[2]
        .replace(" — você", " — usted")
        .replace(" — livre", " — libre")
        .replace(" — Operador não identificado", " — Cajero no identificado");
      return `${inicio}Caja ${caixa[1]}${complemento}${fim}`;
    }
    const novoPedido = conteudo.match(/^Nova solicitação no Caixa (\d+): (.*)$/);
    if (novoPedido) return `${inicio}Nueva solicitud en la caja ${novoPedido[1]}: ${novoPedido[2]}${fim}`;
    if (conteudo.includes(" · código de acesso: ")) {
      return `${inicio}${conteudo.replace(" · código de acesso: ", " · código de acceso: ")}${fim}`;
    }
    if (/^Você assumiu o Caixa \d+$/.test(conteudo)) {
      return `${inicio}${conteudo.replace("Você assumiu o Caixa", "Ahora está a cargo de la caja")}${fim}`;
    }
    return texto;
  }

  function traduzirElemento(elemento) {
    if (elemento.nodeType === Node.TEXT_NODE) {
      if (!textosOriginais.has(elemento)) textosOriginais.set(elemento, elemento.nodeValue);
      const original = textosOriginais.get(elemento);
      const traduzido = idioma === "es" ? traduzirTexto(original) : original;
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
    idioma = novoIdioma === "es" ? "es" : "pt-BR";
    window.idiomaAtual = idioma;
    localStorage.setItem("filalivre-idioma", idioma);
    document.documentElement.lang = idioma;
    document.querySelectorAll(".idioma-opcao").forEach(botao => {
      const selecionado = botao.dataset.idioma === idioma;
      botao.classList.toggle("ativa", selecionado);
      botao.setAttribute("aria-pressed", String(selecionado));
    });
    traduzirElemento(document);
  };
  document.querySelectorAll(".idioma-opcao").forEach(botao => {
    botao.addEventListener("click", () => window.aplicarIdioma(botao.dataset.idioma));
  });
  window.aplicarIdioma(idioma);
  const observador = new MutationObserver(mudancas => {
    mudancas.forEach(mudanca => {
      if (mudanca.type === "characterData") traduzirElemento(mudanca.target);
      mudanca.addedNodes.forEach(no => traduzirElemento(no));
    });
  });
  observador.observe(document.documentElement, { childList: true, subtree: true, characterData: true });
})();