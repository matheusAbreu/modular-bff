package edu.uerj.domain.enums

enum class OrderStatus(val id: Long, val description: String){
    NEW_ORDER( 0, "Novo Pedido"),
    WAITING_PAYMENT(1, "Aguardando Pagamento"),
    UNDER_FRAUD_ANALYSIS(2, "Em análise de fraude"),
    PAID(3, "Pagamento efetuado"),
    INVOICE_BEGINNING(4, "Faturamento iniciado"),
    INVOICED(5, "Faturamento realizado"),
    SEPARATION_BEGINNING(6, "Separação iniciada"),
    SEPARATION_FINISHED(7, "Separação realizada"),
    DELIVERED(8, "Produto Entregue");
    
    companion object {
        @JvmStatic
        fun getByDescription(description: String) =
            entries.firstOrNull { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("No OrderStatus enumeration constant with a description '$description'")

        @JvmStatic
        fun getById(id: Long) = entries.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("No OrderStatus enumeration constant with a id '$id'")
    }
}