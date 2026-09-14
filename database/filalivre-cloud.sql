-- Exportacao SQLite para visualizar no SQLiteCloud.
-- Este arquivo e um dump de demonstracao; o arquivo .mv.db e H2 e nao e SQLite.

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS registros_acao;
DROP TABLE IF EXISTS solicitacoes;
DROP TABLE IF EXISTS caixas;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS mercados;

CREATE TABLE mercados (
  id INTEGER PRIMARY KEY,
  nome TEXT NOT NULL,
  codigo_acesso TEXT NOT NULL UNIQUE,
  gestor_id INTEGER NOT NULL
);

CREATE TABLE usuarios (
  id INTEGER PRIMARY KEY,
  nome TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  senha TEXT NOT NULL,
  perfil TEXT NOT NULL,
  ativo INTEGER NOT NULL DEFAULT 1,
  mercado_id INTEGER,
  FOREIGN KEY (mercado_id) REFERENCES mercados(id)
);

CREATE TABLE caixas (
  id INTEGER PRIMARY KEY,
  numero INTEGER NOT NULL UNIQUE,
  localizacao TEXT,
  ativo INTEGER NOT NULL DEFAULT 1,
  status TEXT NOT NULL,
  valor_compra NUMERIC NOT NULL DEFAULT 0,
  qtd_itens INTEGER NOT NULL DEFAULT 0,
  operador_id INTEGER,
  inicio_atendimento TEXT,
  mercado_id INTEGER,
  FOREIGN KEY (operador_id) REFERENCES usuarios(id),
  FOREIGN KEY (mercado_id) REFERENCES mercados(id)
);

CREATE TABLE solicitacoes (
  id INTEGER PRIMARY KEY,
  caixa_id INTEGER NOT NULL,
  operador_id INTEGER NOT NULL,
  tipo TEXT NOT NULL,
  produto TEXT NOT NULL,
  quantidade INTEGER,
  valor NUMERIC,
  motivo TEXT,
  status TEXT NOT NULL DEFAULT 'PENDENTE',
  criado_em TEXT NOT NULL,
  decidido_em TEXT,
  decidido_por INTEGER,
  FOREIGN KEY (caixa_id) REFERENCES caixas(id),
  FOREIGN KEY (operador_id) REFERENCES usuarios(id),
  FOREIGN KEY (decidido_por) REFERENCES usuarios(id)
);

CREATE TABLE registros_acao (
  id INTEGER PRIMARY KEY,
  usuario_id INTEGER NOT NULL,
  caixa_numero INTEGER,
  acao TEXT NOT NULL,
  detalhes TEXT,
  momento TEXT NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

INSERT INTO mercados (id, nome, codigo_acesso, gestor_id)
VALUES (1, 'Mercado Demonstracao', 'MERCADO1', 2);

INSERT INTO usuarios (id, nome, email, senha, perfil, ativo, mercado_id) VALUES
  (1, 'Administrador', 'admin@filalivre.com', 'admin123', 'ADMINISTRADOR', 1, NULL),
  (2, 'Supervisor Demonstracao', 'supervisor@filalivre.com', 'supervisor123', 'SUPERVISOR', 1, 1),
  (3, 'Operador Demonstracao', 'operador@filalivre.com', 'operador123', 'OPERADOR', 1, 1);

INSERT INTO caixas (id, numero, localizacao, ativo, status, valor_compra, qtd_itens, mercado_id)
VALUES
  (1, 1, 'Entrada principal', 1, 'NORMAL', 0, 0, 1),
  (2, 2, 'Corredor central', 1, 'NORMAL', 0, 0, 1),
  (3, 3, 'Lateral direita', 1, 'NORMAL', 0, 0, 1),
  (4, 4, 'Lateral esquerda', 1, 'NORMAL', 0, 0, 1),
  (5, 5, 'Fundo do mercado', 1, 'NORMAL', 0, 0, 1),
  (6, 6, 'Proximo a saida', 1, 'NORMAL', 0, 0, 1);