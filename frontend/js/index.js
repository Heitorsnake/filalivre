const erro = document.getElementById("erro");
const abaLogin = document.getElementById("aba-login");
const abaCadastro = document.getElementById("aba-cadastro");
const formLogin = document.getElementById("form-login");
const formCadastro = document.getElementById("form-cadastro");
const sucesso = document.getElementById("sucesso");

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
