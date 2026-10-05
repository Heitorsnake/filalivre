const erro = document.getElementById("erro");
const abaLogin = document.getElementById("aba-login");
const abaCadastro = document.getElementById("aba-cadastro");
const formLogin = document.getElementById("form-login");
const formCadastro = document.getElementById("form-cadastro");
const sucesso = document.getElementById("sucesso");
const confirmacaoEmail = document.getElementById("confirmacao-email");
const botaoConfirmarEmail = document.getElementById("btn-confirmar-email");

function mostrarErro(mensagem) {
  sucesso.style.display = "none";
  erro.textContent = mensagem;
  erro.style.display = "block";
}

function mostrarSucesso(mensagem) {
  erro.style.display = "none";
  sucesso.textContent = mensagem;
  sucesso.style.display = "block";
}

function limparErro() {
  erro.style.display = "none";
  sucesso.style.display = "none";
  confirmacaoEmail.style.display = "none";
}

function trocarAba(aba) {
  abaLogin.classList.toggle("ativa", aba === "login");
  abaCadastro.classList.toggle("ativa", aba === "cadastro");
  formLogin.classList.toggle("ativo", aba === "login");
  formCadastro.classList.toggle("ativo", aba === "cadastro");
  limparErro();
}

abaLogin.addEventListener("click", () => trocarAba("login"));
abaCadastro.addEventListener("click", () => trocarAba("cadastro"));

document.getElementById("btn-reenviar-verificacao").addEventListener("click", async () => {
  const email = document.getElementById("login-email").value.trim();
  if (!email) {
    mostrarErro("Informe seu e-mail Gmail para solicitar um novo link.");
    return;
  }
  try {
    const resultado = await apiFetch("/auth/reenviar-verificacao", {
      method: "POST",
      body: { email }
    });
    mostrarSucesso(resultado.mensagem);
  } catch (e) {
    mostrarErro(e.message);
  }
});

formLogin.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  limparErro();
  try {
    const usuario = await apiFetch("/auth/login", {
      method: "POST",
      body: {
        email: document.getElementById("login-email").value.trim(),
        senha: document.getElementById("login-senha").value
      }
    });
    salvarUsuario(usuario);
    window.location.href = destinoPorPerfil(usuario.perfil);
  } catch (e) {
    mostrarErro(e.message);
  }
});

formCadastro.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  limparErro();
  try {
    const resultado = await apiFetch("/auth/cadastro", {
      method: "POST",
      body: {
        nome: document.getElementById("cad-nome").value.trim(),
        email: document.getElementById("cad-email").value.trim(),
        senha: document.getElementById("cad-senha").value,
        perfil: document.getElementById("cad-perfil").value
      }
    });
    if (resultado.verificacaoNecessaria) {
      trocarAba("login");
      mostrarSucesso(resultado.mensagem);
      return;
    }
    const usuario = resultado.usuario;
    salvarUsuario(usuario);
    window.location.href = destinoPorPerfil(usuario.perfil);
  } catch (e) {
    mostrarErro(e.message);
  }
});

async function confirmarEmailPeloLink() {
  const parametros = new URLSearchParams(window.location.hash.slice(1));
  const token = parametros.get("verificar-email");
  if (!token) return;

  window.history.replaceState({}, "", window.location.pathname + window.location.search);
  trocarAba("login");
  formLogin.classList.remove("ativo");
  formCadastro.classList.remove("ativo");
  confirmacaoEmail.style.display = "block";

  botaoConfirmarEmail.addEventListener("click", async () => {
    botaoConfirmarEmail.disabled = true;
    try {
      const resultado = await apiFetch("/auth/verificar-email", {
        method: "POST",
        body: { token }
      });
      confirmacaoEmail.style.display = "none";
      mostrarSucesso(resultado.mensagem);
    } catch (e) {
      mostrarErro(e.message);
      botaoConfirmarEmail.disabled = false;
      formLogin.classList.add("ativo");
    }
  });
}

confirmarEmailPeloLink();
