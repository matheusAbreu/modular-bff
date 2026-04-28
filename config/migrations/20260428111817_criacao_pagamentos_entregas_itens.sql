-- +goose Up
-- +goose StatementBegin
CREATE TABLE pagamentos (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER REFERENCES pedidos(id),
    metodo varchar,
    quantidade_parcelas int,
    valor_parcela decimal,
    valor_total decimal,
    status varchar,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);


CREATE TABLE notas_fiscais (
    danfe varchar PRIMARY KEY,
    serie varchar,
    chave varchar,
    arquivo text,
    valor_em_nota decimal,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);

CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER REFERENCES pedidos(id),
    endereco_id INTEGER REFERENCES enderecos_pedidos(id),
    nota_danfe varchar REFERENCES notas_fiscais(danfe),
    telefone_contato varchar,
    nome_contato varchar,
    frete decimal,
    status varchar,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);

CREATE TABLE itens_pedido (
    id SERIAL PRIMARY KEY,
    produto_id varchar REFERENCES produtos(sku),
    pedido_id INTEGER REFERENCES pedidos(id),
    entrega_id INTEGER REFERENCES entregas(id),
    quantidade int,
    valor_unitario decimal,
    valor_total decimal,
    criado_em timestamp with time zone DEFAULT now(),
    atualizado_em timestamp with time zone DEFAULT now()
);


-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE entregas;
DROP TABLE notas_fiscais;
DROP TABLE pagamentos;
DROP TABLE itens_pedido;
-- +goose StatementEnd
