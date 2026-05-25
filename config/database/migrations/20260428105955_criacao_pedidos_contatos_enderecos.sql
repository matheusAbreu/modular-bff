-- +goose Up
-- +goose StatementBegin
CREATE TABLE pedidos (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES clientes(id),
    status varchar,
    total DECIMAL,
    desconto DECIMAL,
    frete DECIMAL,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);


CREATE TABLE contatos (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES clientes(id),
    principal boolean,
    tipo_contato varchar,
    valor varchar,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);


CREATE TABLE enderecos_clientes (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES clientes(id),
    principal boolean,
    cep varchar,
    logradouro varchar,
    numero varchar,
    complemento varchar,
    cidade varchar,
    estado varchar,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);


CREATE TABLE enderecos_pedidos (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES clientes(id),
    pedido_id INTEGER REFERENCES pedidos(id),
    cep varchar,
    logradouro varchar,
    numero varchar,
    complemento varchar,
    cidade varchar,
    estado varchar,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);

CREATE TABLE imagens_produtos (
    id SERIAL PRIMARY KEY,
	produto_sku varchar REFERENCES produtos(sku),
	base_64 varchar,
	criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE enderecos_pedidos;
DROP TABLE contatos;
DROP TABLE enderecos_clientes;
DROP TABLE imagens_produtos;
DROP TABLE pedidos;
-- +goose StatementEnd
