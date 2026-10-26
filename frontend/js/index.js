const erro = document.getElementById("erro");
const abaLogin = document.getElementById("aba-login");
const abaCadastro = document.getElementById("aba-cadastro");
const formLogin = document.getElementById("form-login");
const formCadastro = document.getElementById("form-cadastro");
const sucesso = document.getElementById("sucesso");
const botoesIdioma = document.querySelectorAll(".idioma-opcao");

const traducoes = {
  "pt-BR": {
    titulo: "FilaLivre — Acesso",
    idioma: "Idioma",
    descricao: "Sistema de gerenciamento remoto de caixas de supermercado. Solicitações de cancelamento analisadas pelo supervisor sem deslocamento físico.",
    abaLogin: "Entrar",
    abaCadastro: "Criar conta",
    email: "E-mail",
    senha: "Senha",
    entrar: "Entrar",
    nomeCompleto: "Nome completo",
    senhaCadastro: "Senha (mínimo 12 caracteres)",
    tipoConta: "Tipo de conta",
    operador: "Operador de caixa",
    supervisor: "Supervisor / gestor do mercado",
    criarConta: "Criar conta",
    placeholderEmail: "voce@filalivre.com",
    placeholderSenha: "••••••••",
    placeholderNome: "Seu nome",
    nomeIdioma: "Português"
  },
  es: {
    titulo: "FilaLivre — Acceso",
    idioma: "Idioma",
    descricao: "Sistema de gestión remota de cajas de supermercado. Las solicitudes de cancelación son analizadas por el supervisor sin necesidad de desplazamiento.",
    abaLogin: "Iniciar sesión",
    abaCadastro: "Crear cuenta",
    email: "Correo electrónico",
    senha: "Contraseña",
    entrar: "Iniciar sesión",
    nomeCompleto: "Nombre completo",
    senhaCadastro: "Contraseña (mínimo 12 caracteres)",
    tipoConta: "Tipo de cuenta",
    operador: "Cajero",
    supervisor: "Supervisor / gerente del mercado",
    criarConta: "Crear cuenta",
    placeholderEmail: "tu@filalivre.com",
    placeholderSenha: "••••••••",
    placeholderNome: "Tu nombre",
    nomeIdioma: "Español"
  }
};

function mudarIdioma(idioma) {
  const textos = traducoes[idioma] || traducoes["pt-BR"];
  window.idiomaAtual = idioma;
  document.querySelectorAll("[data-i18n]").forEach((elemento) => {
    elemento.textContent = textos[elemento.dataset.i18n];
  });
  document.querySelectorAll("[data-i18n-placeholder]").forEach((elemento) => {
    elemento.placeholder = textos[elemento.dataset.i18nPlaceholder];
  });
  document.querySelector(".idioma-controle").setAttribute("aria-label", textos.idioma);
  botoesIdioma.forEach((botao) => {
    const selecionado = botao.dataset.idioma === idioma;
    botao.classList.toggle("ativa", selecionado);
    botao.setAttribute("aria-pressed", String(selecionado));
    botao.title = traducoes[botao.dataset.idioma].nomeIdioma;
    botao.setAttribute("aria-label", traducoes[botao.dataset.idioma].nomeIdioma);
  });
  localStorage.setItem("filalivre-idioma", idioma);
  window.aplicarIdioma?.(idioma);
  document.documentElement.lang = idioma;
  document.title = textos.titulo;
}

botoesIdioma.forEach((botao) => {
  botao.addEventListener("click", () => mudarIdioma(botao.dataset.idioma));
});
mudarIdioma(localStorage.getItem("filalivre-idioma") || "pt-BR");

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
  limparErro();
}

abaLogin.addEventListener("click", () => trocarAba("login"));
abaCadastro.addEventListener("click", () => trocarAba("cadastro"));

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
    const usuario = resultado.usuario;
    salvarUsuario(usuario);
    window.location.href = destinoPorPerfil(usuario.perfil);
  } catch (e) {
    mostrarErro(e.message);
  }
});
