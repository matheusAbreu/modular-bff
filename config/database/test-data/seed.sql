-- Script para popular o banco de dados conforme especificações:
-- 3 Clientes, cada um com 1 endereço, 2 pedidos e 2-5 contatos.
-- Cada pedido com 1 a 3 itens (produtos não repetidos no mesmo pedido).
-- Cada item associado a um pacote de entrega (entregas).
-- Pagamentos e Notas Fiscais para pedidos ENTREGUE, PROCESSANDO ou ENVIADO.

-- Limpar dados existentes para evitar conflitos de FK (Opcional)
-- TRUNCATE TABLE itens_pedido, entregas, pagamentos, enderecos_pedidos, enderecos_clientes, contatos, pedidos, produtos, clientes, notas_fiscais CASCADE;

-- 1. Inserir Produtos (Skus únicos)
INSERT INTO produtos (sku, nome, descricao, preco) VALUES
('SKU-001', 'Smartphone Alpha', 'Smartphone de última geração com tela OLED', 2500.00),
('SKU-002', 'Notebook Beta', 'Notebook potente para trabalho e jogos', 5500.00),
('SKU-003', 'Monitor Gamma', 'Monitor 4K de 27 polegadas', 1800.00),
('SKU-004', 'Teclado Mecânico Delta', 'Teclado RGB com switches silenciosos', 450.00),
('SKU-005', 'Mouse Gamer Epsilon', 'Mouse ergonômico com 12000 DPI', 250.00)
ON CONFLICT (sku) DO NOTHING;

-- 2. Inserir 3 Clientes
INSERT INTO clientes (nome, email, documento, tipo_documento, data_nascimento) VALUES
('Carlos Alberto', 'carlos.alberto@email.com', '12345678901', 'CPF', '1985-03-12'),
('Ana Maria Souza', 'ana.maria@email.com', '98765432100', 'CPF', '1992-07-25'),
('Roberto Fernandes', 'roberto.f@email.com', '11122233344', 'CPF', '1980-11-30')
ON CONFLICT (email) DO NOTHING;

-- 3. Inserir Contatos (2 a 5 por cliente)
INSERT INTO contatos (cliente_id, principal, tipo_contato, valor)
SELECT id, true, 'CELULAR', '11988887777' FROM clientes WHERE email = 'carlos.alberto@email.com'
UNION ALL SELECT id, false, 'EMAIL_SECUNDARIO', 'carlos.work@email.com' FROM clientes WHERE email = 'carlos.alberto@email.com';

INSERT INTO contatos (cliente_id, principal, tipo_contato, valor)
SELECT id, true, 'CELULAR', '21977776666' FROM clientes WHERE email = 'ana.maria@email.com'
UNION ALL SELECT id, false, 'RESIDENCIAL', '2133334444' FROM clientes WHERE email = 'ana.maria@email.com'
UNION ALL SELECT id, false, 'TRABALHO', '2122221111' FROM clientes WHERE email = 'ana.maria@email.com';

INSERT INTO contatos (cliente_id, principal, tipo_contato, valor)
SELECT id, true, 'EMAIL', 'roberto.f@email.com' FROM clientes WHERE email = 'roberto.f@email.com'
UNION ALL SELECT id, false, 'CELULAR', '31966665555' FROM clientes WHERE email = 'roberto.f@email.com'
UNION ALL SELECT id, false, 'SKYPE', 'roberto.fernandes.skype' FROM clientes WHERE email = 'roberto.f@email.com'
UNION ALL SELECT id, false, 'WHATSAPP', '31966665555' FROM clientes WHERE email = 'roberto.f@email.com';

-- 4. Inserir 1 Endereço para cada Cliente (Endereço do Cadastro)
INSERT INTO enderecos_clientes (cliente_id, principal, cep, logradouro, numero, complemento, cidade, estado)
SELECT id, true, '01001-000', 'Rua das Flores', '100', 'Apt 1', 'São Paulo', 'SP' FROM clientes WHERE email = 'carlos.alberto@email.com';

INSERT INTO enderecos_clientes (cliente_id, principal, cep, logradouro, numero, complemento, cidade, estado)
SELECT id, true, '20040-002', 'Avenida Central', '500', 'Sala 202', 'Rio de Janeiro', 'RJ' FROM clientes WHERE email = 'ana.maria@email.com';

INSERT INTO enderecos_clientes (cliente_id, principal, cep, logradouro, numero, complemento, cidade, estado)
SELECT id, true, '30140-001', 'Praça da Liberdade', 'S/N', NULL, 'Belo Horizonte', 'MG' FROM clientes WHERE email = 'roberto.f@email.com';

-- 5. Inserir Pedidos, Endereços de Entrega, Notas Fiscais, Pacotes de Entrega, Pagamentos e Itens

-- PEDIDO 1 - Carlos Alberto (Status: PROCESSANDO -> Precisa NF e Pagamento)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'PROCESSANDO', 2750.00, 0.00, 250.00 FROM clientes WHERE email = 'carlos.alberto@email.com'
    RETURNING id AS pedido_id, cliente_id, total
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '01001-000', 'Rua das Flores', '100', 'Apt 1', 'São Paulo', 'SP' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
nf AS (
    INSERT INTO notas_fiscais (danfe, serie, chave, arquivo, valor_em_nota)
    SELECT 'DANFE-001', '1', 'CHAVE-PEDIDO-001-XYZ', 'arquivo_nf_001.pdf', 2750.00 FROM ped
    RETURNING danfe
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, nota_danfe, telefone_contato, nome_contato, frete, status)
    SELECT ep.pedido_id, ep.endereco_id, nf.danfe, '11988887777', 'Carlos Alberto', 250.00, 'PENDENTE' 
    FROM end_ped ep, nf
    RETURNING id AS entrega_id, pedido_id
),
pag AS (
    INSERT INTO pagamentos (pedido_id, metodo, quantidade_parcelas, valor_parcela, valor_total, status)
    SELECT pedido_id, 'CARTAO_CREDITO', 1, total, total, 'PAGO' FROM ped
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-001', pedido_id, entrega_id, 1, 2500.00, 2500.00 FROM ent
UNION ALL
SELECT 'SKU-005', pedido_id, entrega_id, 1, 250.00, 250.00 FROM ent;

-- PEDIDO 2 - Carlos Alberto (Status: AGUARDANDO_PAGAMENTO -> Sem NF/Pagamento obrigatório)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'AGUARDANDO_PAGAMENTO', 450.00, 0.00, 0.00 FROM clientes WHERE email = 'carlos.alberto@email.com'
    RETURNING id AS pedido_id, cliente_id
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '01001-000', 'Rua das Flores', '100', 'Apt 1', 'São Paulo', 'SP' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, telefone_contato, nome_contato, frete, status)
    SELECT pedido_id, endereco_id, '11988887777', 'Carlos Alberto', 0.00, 'AGUARDANDO' FROM end_ped
    RETURNING id AS entrega_id, pedido_id
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-004', pedido_id, entrega_id, 1, 450.00, 450.00 FROM ent;

-- PEDIDO 3 - Ana Maria (Status: ENTREGUE -> Precisa NF e Pagamento)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'ENTREGUE', 7300.00, 100.00, 50.00 FROM clientes WHERE email = 'ana.maria@email.com'
    RETURNING id AS pedido_id, cliente_id, total
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '20040-002', 'Avenida Central', '500', 'Sala 202', 'Rio de Janeiro', 'RJ' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
nf AS (
    INSERT INTO notas_fiscais (danfe, serie, chave, arquivo, valor_em_nota)
    SELECT 'DANFE-002', '1', 'CHAVE-PEDIDO-002-ABC', 'arquivo_nf_002.pdf', 7300.00 FROM ped
    RETURNING danfe
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, nota_danfe, telefone_contato, nome_contato, frete, status)
    SELECT ep.pedido_id, ep.endereco_id, nf.danfe, '21977776666', 'Ana Maria', 50.00, 'ENTREGUE' 
    FROM end_ped ep, nf
    RETURNING id AS entrega_id, pedido_id
),
pag AS (
    INSERT INTO pagamentos (pedido_id, metodo, quantidade_parcelas, valor_parcela, valor_total, status)
    SELECT pedido_id, 'PIX', 1, total, total, 'PAGO' FROM ped
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-002', pedido_id, entrega_id, 1, 5500.00, 5500.00 FROM ent
UNION ALL
SELECT 'SKU-003', pedido_id, entrega_id, 1, 1800.00, 1800.00 FROM ent
UNION ALL
SELECT 'SKU-004', pedido_id, entrega_id, 1, 450.00, 450.00 FROM ent;

-- PEDIDO 4 - Ana Maria (Status: CANCELADO)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'CANCELADO', 250.00, 0.00, 15.00 FROM clientes WHERE email = 'ana.maria@email.com'
    RETURNING id AS pedido_id, cliente_id
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '20040-002', 'Avenida Central', '500', 'Sala 202', 'Rio de Janeiro', 'RJ' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, telefone_contato, nome_contato, frete, status)
    SELECT pedido_id, endereco_id, '21977776666', 'Ana Maria', 15.00, 'CANCELADO' FROM end_ped
    RETURNING id AS entrega_id, pedido_id
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-005', pedido_id, entrega_id, 1, 250.00, 250.00 FROM ent;

-- PEDIDO 5 - Roberto Fernandes (Status: ENVIADO -> Precisa NF e Pagamento)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'ENVIADO', 2250.00, 0.00, 0.00 FROM clientes WHERE email = 'roberto.f@email.com'
    RETURNING id AS pedido_id, cliente_id, total
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '30140-001', 'Praça da Liberdade', 'S/N', NULL, 'Belo Horizonte', 'MG' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
nf AS (
    INSERT INTO notas_fiscais (danfe, serie, chave, arquivo, valor_em_nota)
    SELECT 'DANFE-003', '1', 'CHAVE-PEDIDO-003-LMN', 'arquivo_nf_003.pdf', 2250.00 FROM ped
    RETURNING danfe
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, nota_danfe, telefone_contato, nome_contato, frete, status)
    SELECT ep.pedido_id, ep.endereco_id, nf.danfe, '31966665555', 'Roberto Fernandes', 0.00, 'EM_TRANSITO' 
    FROM end_ped ep, nf
    RETURNING id AS entrega_id, pedido_id
),
pag AS (
    INSERT INTO pagamentos (pedido_id, metodo, quantidade_parcelas, valor_parcela, valor_total, status)
    SELECT pedido_id, 'BOLETO', 1, total, total, 'PAGO' FROM ped
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-003', pedido_id, entrega_id, 1, 1800.00, 1800.00 FROM ent
UNION ALL
SELECT 'SKU-004', pedido_id, entrega_id, 1, 450.00, 450.00 FROM ent;

-- PEDIDO 6 - Roberto Fernandes (Status: ABERTO)
WITH ped AS (
    INSERT INTO pedidos (cliente_id, status, total, desconto, frete) 
    SELECT id, 'ABERTO', 1800.00, 50.00, 20.00 FROM clientes WHERE email = 'roberto.f@email.com'
    RETURNING id AS pedido_id, cliente_id
),
end_ped AS (
    INSERT INTO enderecos_pedidos (cliente_id, pedido_id, cep, logradouro, numero, complemento, cidade, estado)
    SELECT cliente_id, pedido_id, '30140-001', 'Praça da Liberdade', 'S/N', NULL, 'Belo Horizonte', 'MG' FROM ped
    RETURNING id AS endereco_id, pedido_id
),
ent AS (
    INSERT INTO entregas (pedido_id, endereco_id, telefone_contato, nome_contato, frete, status)
    SELECT pedido_id, endereco_id, '31966665555', 'Roberto Fernandes', 20.00, 'PENDENTE' FROM end_ped
    RETURNING id AS entrega_id, pedido_id
)
INSERT INTO itens_pedido (produto_sku, pedido_id, entrega_id, quantidade, valor_unitario, valor_total) 
SELECT 'SKU-003', pedido_id, entrega_id, 1, 1800.00, 1800.00 FROM ent;
