-- +goose Up
-- +goose StatementBegin
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome varchar NOT NULL,
    email varchar NOT NULL UNIQUE,
    documento varchar NOT NULL UNIQUE,
    tipo_documento varchar NOT NULL,
    data_nascimento timestamp NOT NULL,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);

CREATE TABLE produtos (
    sku varchar PRIMARY KEY,
    nome varchar NOT NULL,
    descricao varchar NOT NULL,
    preco decimal NOT NULL,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);

-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE clientes;
DROP TABLE produtos;
-- +goose StatementEnd
