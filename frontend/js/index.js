const erro = document.getElementById("erro");
const abaLogin = document.getElementById("aba-login");
const abaCadastro = document.getElementById("aba-cadastro");
const formLogin = document.getElementById("form-login");
const formCadastro = document.getElementById("form-cadastro");
const formVerificacao = document.getElementById("form-verificacao");
const abas = document.querySelector(".abas");
const sucesso = document.getElementById("sucesso");
let emailPendente = "";

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
}

function trocarAba(aba) {
  abaLogin.classList.toggle("ativa", aba === "login");
  abaCadastro.classList.toggle("ativa", aba === "cadastro");
  formLogin.classList.toggle("ativo", aba === "login");
  formCadastro.classList.toggle("ativo", aba === "cadastro");
  formVerificacao.classList.remove("ativo");
  abas.style.display = "flex";
  limparErro();
}

abaLogin.addEventListener("click", () => trocarAba("login"));
abaCadastro.addEventListener("click", () => trocarAba("cadastro"));

document.getElementById("btn-reenviar-codigo").addEventListener("click", async () => {
  try {
    const resultado = await apiFetch("/auth/reenviar-verificacao", {
      method: "POST",
      body: { email: emailPendente }
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
      emailPendente = document.getElementById("cad-email").value.trim();
      document.querySelector(".abas").style.display = "none";
      formLogin.classList.remove("ativo");
      formCadastro.classList.remove("ativo");
      formVerificacao.classList.add("ativo");
      document.getElementById("texto-verificacao").textContent =
        `Enviamos um código de 6 dígitos para ${emailPendente}. O código expira em 10 minutos.`;
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

formVerificacao.addEventListener("submit", async evento => {
  evento.preventDefault();
  limparErro();
  try {
    const resultado = await apiFetch("/auth/verificar-email", {
      method: "POST",
      body: {
        email: emailPendente,
        codigo: document.getElementById("codigo-verificacao").value.trim()
      }
    });
    if (!resultado.verificado) {
      mostrarErro(resultado.mensagem);
      return;
    }
    formVerificacao.classList.remove("ativo");
    abas.style.display = "flex";
    trocarAba("login");
    document.getElementById("login-email").value = emailPendente;
    mostrarSucesso(resultado.mensagem);
  } catch (e) {
    mostrarErro(e.message);
  }
});
