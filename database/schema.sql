-- =====================================================================
-- FILALIVRE — Modelo de dados (referência)
-- =====================================================================
-- O backend usa Spring Boot + JPA/Hibernate com banco SQLite em modo arquivo.
-- As tabelas abaixo são criadas AUTOMATICAMENTE pelo Hibernate
-- (spring.jpa.hibernate.ddl-auto=update) ao iniciar a aplicação.
--
-- Localização do arquivo do banco: database/filalivre.db
-- Este script serve como documentação oficial do esquema.
-- =====================================================================

CREATE TABLE usuarios (
  id       INTEGER PRIMARY KEY AUTOINCREMENT,
  nome     TEXT NOT NULL,
  email    TEXT NOT NULL UNIQUE,
  senha    TEXT NOT NULL,                  -- hash BCrypt
  perfil   TEXT NOT NULL,                  -- OPERADOR | SUPERVISOR | GERENTE | ADMINISTRADOR
  ativo    INTEGER NOT NULL DEFAULT 1,
  mercado_id BIGINT
);

CREATE TABLE mercados (
  id BIGINT PRIMARY KEY,
  nome TEXT NOT NULL,
  codigo_acesso TEXT NOT NULL UNIQUE,
  gestor_id BIGINT NOT NULL
);

CREATE TABLE caixas (
  id                 INTEGER PRIMARY KEY AUTOINCREMENT,
  numero             INTEGER NOT NULL UNIQUE,
  localizacao        TEXT,
  ativo              INTEGER NOT NULL DEFAULT 1,
  status             TEXT NOT NULL,        -- NORMAL | AGUARDANDO | SOLICITACAO | APROVACAO
  valor_compra       NUMERIC NOT NULL DEFAULT 0,
  qtd_itens          INTEGER NOT NULL DEFAULT 0,
  operador_id        BIGINT,                -- FK usuarios.id (operador atual, opcional)
  inicio_atendimento TEXT,
  mercado_id BIGINT,
  CONSTRAINT fk_caixa_operador FOREIGN KEY (operador_id) REFERENCES usuarios (id)
);

CREATE TABLE solicitacoes (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  caixa_id      BIGINT NOT NULL,
  operador_id   BIGINT NOT NULL,
  tipo          TEXT NOT NULL,              -- CANCELAMENTO | DESCONTO | CUPOM
  produto       TEXT NOT NULL,
  quantidade    INTEGER,
  valor         NUMERIC,
  motivo        TEXT,
  status        TEXT NOT NULL DEFAULT 'PENDENTE', -- PENDENTE | APROVADA | RECUSADA
  criado_em     TEXT NOT NULL,
  decidido_em   TEXT,
  decidido_por  BIGINT,                     -- FK usuarios.id (gestor que decidiu)
  CONSTRAINT fk_sol_caixa    FOREIGN KEY (caixa_id)     REFERENCES caixas (id),
  CONSTRAINT fk_sol_operador FOREIGN KEY (operador_id)  REFERENCES usuarios (id),
  CONSTRAINT fk_sol_gestor   FOREIGN KEY (decidido_por) REFERENCES usuarios (id)
);

CREATE TABLE registros_acao (
  id           INTEGER PRIMARY KEY AUTOINCREMENT,
  usuario_id   BIGINT NOT NULL,
  caixa_numero INTEGER,
  acao         TEXT NOT NULL,               -- LOGIN, SOLICITACAO_CRIADA, SOLICITACAO_APROVADA, ...
  detalhes     TEXT,
  momento      TEXT NOT NULL,
  CONSTRAINT fk_reg_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

-- =====================================================================
-- Dados iniciais (criados automaticamente pelo DadosIniciais.java):
--   admin@filalivre.com    / admin123     (ADMINISTRADOR)
--   gerente@filalivre.com  / gerente123   (GERENTE)
--   operador@filalivre.com / operador123  (OPERADOR)
--   Caixas numerados de 1 a 6 com status NORMAL
-- =====================================================================
