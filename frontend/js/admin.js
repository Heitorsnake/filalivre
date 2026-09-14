const usuario = usuarioLogado();

if (!usuario) {
  window.location.href = "index.html";
} else if (usuario.perfil !== "ADMINISTRADOR") {
  window.location.href = destinoPorPerfil(usuario.perfil);
}

document.getElementById("nome-usuario").textContent = usuario?.nome || "";

const rotulosPerfil = {
  OPERADOR: "Operador",
  SUPERVISOR: "Supervisor",
  SUPERVISOR: "Supervisor / gestor",
  ADMINISTRADOR: "Administrador"
};

const rotulosAcao = {
  LOGIN: "Login",
  USUARIO_CRIADO: "Usuário criado",
  USUARIO_ATIVADO: "Usuário ativado",
  USUARIO_DESATIVADO: "Usuário desativado",
  SOLICITACAO_CRIADA: "Solicitação criada",
  SOLICITACAO_APROVADA: "Solicitação aprovada",
  SOLICITACAO_RECUSADA: "Solicitação recusada"
};

function escapar(texto) {
  const div = document.createElement("div");
  div.textContent = texto ?? "";
  return div.innerHTML;
}

function carregarCaixas() {
  return apiFetch("/caixas").then(caixas => {
    const grid = document.getElementById("grid-caixas");
    grid.innerHTML = caixas.map(caixa => `
      <div class="caixa-card borda-${caixa.status}">
        <div class="caixa-numero">Caixa ${String(caixa.numero).padStart(2, "0")}</div>
        <span class="status-chip status-${caixa.status}">${caixa.status}</span>
        <div class="caixa-detalhe"><span>Localização</span><strong>${escapar(caixa.localizacao || "Não informada")}</strong></div>
        <div class="caixa-detalhe"><span>Operador</span><strong>${escapar(caixa.operadorNome || "-")}</strong></div>
        <div class="dialog-acoes"><button class="btn btn-cinza btn-pequeno" data-editar-caixa="${caixa.id}">Editar local</button><button class="btn btn-vermelho btn-pequeno" data-desativar-caixa="${caixa.id}">Desativar</button></div>
      </div>`).join("");
    grid.querySelectorAll("[data-editar-caixa]").forEach(botao => botao.addEventListener("click", () => editarCaixa(botao.dataset.editarCaixa)));
    grid.querySelectorAll("[data-desativar-caixa]").forEach(botao => botao.addEventListener("click", () => desativarCaixa(botao.dataset.desativarCaixa)));
  });
}

async function editarCaixa(id) {
  const localizacao = prompt("Nova localização do caixa:");
  if (!localizacao?.trim()) return;
  try {
    await apiFetch(`/caixas/${id}`, { method: "PATCH", body: { localizacao: localizacao.trim() } });
    mostrarToast("Localização atualizada", "verde");
    carregarCaixas();
  } catch (erro) { mostrarToast(erro.message, "vermelho"); }
}

async function desativarCaixa(id) {
  if (!confirm("Desativar este caixa? O número será preservado no histórico.")) return;
  try {
    await apiFetch(`/caixas/${id}`, { method: "DELETE" });
    mostrarToast("Caixa desativado", "verde");
    carregarCaixas();
  } catch (erro) { mostrarToast(erro.message, "vermelho"); }
}

async function carregarUsuarios() {
  try {
    const usuarios = await apiFetch("/usuarios");
    const ativos = usuarios.filter(item => item.ativo).length;
    document.getElementById("res-ativos").textContent = ativos;
    document.getElementById("corpo-usuarios").innerHTML = usuarios.map(item => `
      <tr>
        <td>${escapar(item.nome)}</td>
        <td>${escapar(item.email)}</td>
        <td><span class="perfil-badge">${rotulosPerfil[item.perfil] || item.perfil}</span></td>
        <td>${item.ativo ? "Ativo" : "Inativo"}</td>
        <td><button class="btn btn-cinza btn-pequeno" data-usuario="${item.id}">${item.ativo ? "Desativar" : "Ativar"}</button></td>
      </tr>`).join("");

    document.querySelectorAll("[data-usuario]").forEach(botao => {
      botao.addEventListener("click", async () => {
        try {
          await apiFetch(`/usuarios/${botao.dataset.usuario}/ativo`, { method: "PATCH" });
          mostrarToast("Status do usuário atualizado", "verde");
          carregarUsuarios();
          carregarHistorico();
        } catch (erro) {
          mostrarToast(erro.message, "vermelho");
        }
      });
    });
  } catch (erro) {
    mostrarToast(erro.message, "vermelho");
  }
}

async function carregarResumo() {
  try {
    const resumo = await apiFetch("/relatorios/resumo");
    document.getElementById("res-pendentes").textContent = resumo.pendentes;
    document.getElementById("res-aprovadas").textContent = resumo.aprovadas;
    document.getElementById("res-recusadas").textContent = resumo.recusadas;
  } catch (erro) {
    mostrarToast(erro.message, "vermelho");
  }
}

async function carregarHistorico() {
  try {
    const registros = await apiFetch("/historico");
    document.getElementById("corpo-historico").innerHTML = registros.map(registro => `
      <tr>
        <td>${formatarDataHora(registro.momento)}</td>
        <td>${escapar(registro.usuarioNome)}</td>
        <td>${registro.caixaNumero ? `Caixa ${String(registro.caixaNumero).padStart(2, "0")}` : "-"}</td>
        <td>${rotulosAcao[registro.acao] || registro.acao}</td>
        <td>${escapar(registro.detalhes)}</td>
      </tr>`).join("");
  } catch (erro) {
    mostrarToast(erro.message, "vermelho");
  }
}

document.getElementById("form-usuario").addEventListener("submit", async evento => {
  evento.preventDefault();
  const erro = document.getElementById("erro-usuario");
  erro.style.display = "none";
  try {
    await apiFetch("/usuarios", {
      method: "POST",
      body: {
        nome: document.getElementById("novo-nome").value.trim(),
        email: document.getElementById("novo-email").value.trim(),
        senha: document.getElementById("nova-senha").value,
        perfil: document.getElementById("novo-perfil").value
      }
    });
    evento.target.reset();
    mostrarToast("Usuário criado com sucesso", "verde");
    carregarUsuarios();
    carregarHistorico();
  } catch (excecao) {
    erro.textContent = excecao.message;
    erro.style.display = "block";
  }
});

const dialogCaixa = document.getElementById("dialog-caixa");
document.getElementById("btn-novo-caixa").addEventListener("click", () => {
  document.getElementById("erro-caixa").style.display = "none";
  dialogCaixa.showModal();
});

document.getElementById("form-novo-caixa").addEventListener("submit", async evento => {
  evento.preventDefault();
  try {
    await apiFetch("/caixas", {
      method: "POST",
      body: { localizacao: document.getElementById("localizacao-caixa").value.trim() }
    });
    evento.target.reset();
    dialogCaixa.close();
    mostrarToast("Caixa criado com numeração automática", "verde");
    carregarCaixas();
  } catch (excecao) {
    const erro = document.getElementById("erro-caixa");
    erro.textContent = excecao.message;
    erro.style.display = "block";
  }
});

carregarUsuarios();
carregarResumo();
carregarHistorico();
carregarCaixas();
