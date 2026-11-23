const formRelatorio = document.getElementById("form-relatorio");
let relatorioAtual = null;

function dataLocalISO(data) {
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, "0");
  const dia = String(data.getDate()).padStart(2, "0");
  return `${ano}-${mes}-${dia}`;
}

function formatarDataRelatorio(valor) {
  if (!valor) return "—";
  const data = new Date(`${valor}T12:00:00`);
  const locale = window.idiomaAtual === "es" ? "es-ES" : "pt-BR";
  return new Intl.DateTimeFormat(locale).format(data);
}

function formatarDataHoraRelatorio(valor) {
  if (!valor) return "—";
  const locale = window.idiomaAtual === "es" ? "es-ES" : "pt-BR";
  return new Intl.DateTimeFormat(locale, {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  }).format(new Date(valor));
}

function textoEstadoSolicitacao(status) {
  return {
    PENDENTE: "Pendente",
    APROVADA: "Aprovada",
    RECUSADA: "Recusada"
  }[status] || status;
}

function escaparRelatorio(valor) {
  const elemento = document.createElement("span");
  elemento.textContent = valor ?? "";
  return elemento.innerHTML;
}

function montarGrafico(dias) {
  const largura = 900;
  const altura = 220;
  const base = 178;
  const alturaGrafico = 138;
  const maximo = Math.max(1, ...dias.map(dia => dia.total));
  const intervalo = (largura - 36) / Math.max(dias.length, 1);
  const larguraBarra = Math.max(1, Math.min(14, intervalo * 0.68));
  const elementos = [];

  for (let linha = 0; linha <= 3; linha++) {
    const y = 30 + linha * (alturaGrafico / 3);
    elementos.push(`<line x1="18" y1="${y}" x2="882" y2="${y}" class="linha-grafico"/>`);
  }
  elementos.push(`<line x1="18" y1="${base}" x2="882" y2="${base}" class="eixo-grafico"/>`);

  dias.forEach((dia, indice) => {
    const x = 18 + indice * intervalo + (intervalo - larguraBarra) / 2;
    let y = base;
    const series = [
      { valor: dia.pendentes, classe: "barra-pendente" },
      { valor: dia.aprovadas, classe: "barra-aprovada" },
      { valor: dia.recusadas, classe: "barra-recusada" }
    ];
    for (const serie of series) {
      if (!serie.valor) continue;
      const alturaBarra = serie.valor / maximo * alturaGrafico;
      y -= alturaBarra;
      elementos.push(
        `<rect x="${x.toFixed(2)}" y="${y.toFixed(2)}" width="${larguraBarra.toFixed(2)}" ` +
        `height="${alturaBarra.toFixed(2)}" rx="1" class="${serie.classe}">` +
        `<title>${escaparRelatorio(formatarDataRelatorio(dia.data))}: ${dia.total} ` +
        `(${dia.pendentes} pendentes, ${dia.aprovadas} aprovadas, ${dia.recusadas} recusadas)</title></rect>`
      );
    }
  });

  const mobile = typeof window.matchMedia === "function"
    && window.matchMedia("(max-width: 600px)").matches;
  const limiteRotulos = mobile ? 2 : 5;
  const rotulos = Math.min(limiteRotulos, dias.length);
  for (let indice = 0; indice < rotulos; indice++) {
    const posicao = rotulos === 1 ? 0 : Math.round(indice * (dias.length - 1) / (rotulos - 1));
    const x = 18 + posicao * intervalo + intervalo / 2;
    elementos.push(
      `<text x="${x.toFixed(2)}" y="207" text-anchor="middle" class="rotulo-grafico">` +
      `${escaparRelatorio(formatarDataRelatorio(dias[posicao].data))}</text>`
    );
  }

  const total = dias.reduce((soma, dia) => soma + dia.total, 0);
  const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
  svg.setAttribute("viewBox", `0 0 ${largura} ${altura}`);
  svg.setAttribute("role", "img");
  svg.setAttribute("aria-label", `Solicitações por dia no período. Total: ${total}.`);
  svg.innerHTML = `<title>Solicitações por dia</title>${elementos.join("")}`;
  const container = document.getElementById("relatorio-grafico");
  container.replaceChildren(svg);
}

function renderizarRelatorio(relatorio) {
  const locale = window.idiomaAtual === "es" ? "es-ES" : "pt-BR";
  const numero = new Intl.NumberFormat(locale, { maximumFractionDigits: 1 });
  document.getElementById("metadados-relatorio").textContent =
    `Período: ${formatarDataRelatorio(relatorio.inicio)} a ${formatarDataRelatorio(relatorio.fim)} · ` +
    `Gerado em ${formatarDataHoraRelatorio(relatorio.geradoEm)}`;
  document.getElementById("relatorio-total").textContent = relatorio.total;
  document.getElementById("relatorio-pendentes").textContent = relatorio.pendentes;
  document.getElementById("relatorio-aprovadas").textContent = relatorio.aprovadas;
  document.getElementById("relatorio-recusadas").textContent = relatorio.recusadas;
  document.getElementById("relatorio-percentual").textContent =
    `${numero.format(relatorio.percentualAprovacao)}%`;
  document.getElementById("relatorio-tempo").textContent =
    relatorio.aprovadas + relatorio.recusadas > 0
      ? `${numero.format(relatorio.tempoMedioDecisaoMinutos)} min`
      : "—";
  document.getElementById("resumo-total").textContent = numero.format(relatorio.total);
  document.getElementById("resumo-pendentes").textContent = numero.format(relatorio.pendentes);
  document.getElementById("resumo-aprovadas").textContent = numero.format(relatorio.aprovadas);
  document.getElementById("resumo-recusadas").textContent = numero.format(relatorio.recusadas);
  document.getElementById("resumo-percentual").textContent =
    `${numero.format(relatorio.percentualAprovacao)}%`;
  montarGrafico(relatorio.porDia);

  const corpo = document.getElementById("corpo-relatorio");
  if (relatorio.solicitacoes.length === 0) {
    corpo.innerHTML = '<tr><td class="sem-resultados" colspan="7">Nenhuma solicitação encontrada neste período.</td></tr>';
    return;
  }
  corpo.innerHTML = relatorio.solicitacoes.map(item => `
    <tr>
      <td data-label="Data">${escaparRelatorio(formatarDataHoraRelatorio(item.criadoEm))}</td>
      <td data-label="Caixa">${item.caixaNumero ? `Caixa ${String(item.caixaNumero).padStart(2, "0")}` : "—"}</td>
      <td data-label="Operador">${escaparRelatorio(item.operadorNome)}</td>
      <td data-label="Solicitação">${escaparRelatorio(item.produto)} · ${item.quantidade ?? 0} un.</td>
      <td data-label="Valor">${formatarMoeda(item.valor ?? 0)}</td>
      <td data-label="Estado"><span class="tag-${item.status.toLowerCase()}">${textoEstadoSolicitacao(item.status)}</span></td>
      <td data-label="Decisão">${escaparRelatorio(formatarDataHoraRelatorio(item.decididoEm))}</td>
    </tr>`).join("");
}

async function carregarRelatorio(evento) {
  evento?.preventDefault();
  const inicio = document.getElementById("relatorio-inicio").value;
  const fim = document.getElementById("relatorio-fim").value;
  const estado = document.getElementById("estado-relatorio");
  const erro = document.getElementById("erro-relatorio");
  const conteudo = document.getElementById("conteudo-relatorio");
  const botaoGerar = formRelatorio.querySelector('[type="submit"]');
  const botaoExcel = document.getElementById("btn-relatorio-excel");
  const botaoPdf = document.getElementById("btn-relatorio-pdf");

  erro.style.display = "none";
  estado.textContent = "A consultar os registos...";
  estado.classList.add("carregando");
  formRelatorio.setAttribute("aria-busy", "true");
  botaoGerar.disabled = true;
  botaoExcel.disabled = true;
  botaoPdf.disabled = true;
  conteudo.hidden = true;
  relatorioAtual = null;

  try {
    const parametros = new URLSearchParams({ inicio, fim });
    const status = document.getElementById("relatorio-status").value;
    if (status) parametros.set("status", status);
    relatorioAtual = await apiFetch(`/relatorios/solicitacoes?${parametros}`);
    renderizarRelatorio(relatorioAtual);
    conteudo.hidden = false;
    botaoExcel.disabled = false;
    botaoPdf.disabled = false;
    estado.textContent = `${relatorioAtual.total} solicitação(ões) encontradas.`;
  } catch (excecao) {
    erro.textContent = excecao.message;
    erro.style.display = "block";
    estado.textContent = "";
  } finally {
    estado.classList.remove("carregando");
    formRelatorio.removeAttribute("aria-busy");
    botaoGerar.disabled = false;
  }
}

function protegerFormulaCsv(valor) {
  const texto = String(valor ?? "");
  return /^[=+\-@]/.test(texto) ? `'${texto}` : texto;
}

function valorCsv(valor) {
  return `"${protegerFormulaCsv(valor).replaceAll('"', '""')}"`;
}

function exportarCsv() {
  if (!relatorioAtual) return;
  const relatorio = relatorioAtual;
  const linhas = [
    ["Relatório de solicitações — FilaLivre"],
    ["Período", relatorio.inicio, relatorio.fim],
    ["Gerado em", relatorio.geradoEm],
    [],
    ["Indicador", "Valor"],
    ["Solicitações", relatorio.total],
    ["Pendentes", relatorio.pendentes],
    ["Aprovadas", relatorio.aprovadas],
    ["Recusadas", relatorio.recusadas],
    ["Percentual de aprovação", `${relatorio.percentualAprovacao}%`],
    ["Tempo médio de decisão (min)", relatorio.tempoMedioDecisaoMinutos],
    [],
    ["Data", "Caixa", "Operador", "Tipo", "Produto", "Quantidade", "Valor (R$)", "Estado", "Data da decisão"],
    ...relatorio.solicitacoes.map(item => [
      item.criadoEm,
      item.caixaNumero,
      item.operadorNome,
      item.tipo,
      item.produto,
      item.quantidade,
      item.valor,
      item.status,
      item.decididoEm
    ])
  ];
  const conteudo = "\uFEFF" + linhas.map(linha => linha.map(valorCsv).join(";")).join("\r\n");
  const arquivo = new Blob([conteudo], { type: "text/csv;charset=utf-8" });
  const url = URL.createObjectURL(arquivo);
  const link = document.createElement("a");
  link.href = url;
  link.download = `filalivre-relatorio-${relatorio.inicio}-${relatorio.fim}.csv`;
  link.click();
  URL.revokeObjectURL(url);
}

function imprimirRelatorio() {
  if (relatorioAtual) window.print();
}

const hoje = new Date();
const inicioPadrao = new Date(hoje);
inicioPadrao.setDate(hoje.getDate() - 29);
document.getElementById("relatorio-inicio").value = dataLocalISO(inicioPadrao);
document.getElementById("relatorio-fim").value = dataLocalISO(hoje);
formRelatorio.addEventListener("submit", carregarRelatorio);
document.getElementById("btn-relatorio-excel").addEventListener("click", exportarCsv);
document.getElementById("btn-relatorio-pdf").addEventListener("click", imprimirRelatorio);
carregarRelatorio();
