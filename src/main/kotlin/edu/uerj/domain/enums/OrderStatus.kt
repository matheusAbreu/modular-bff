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
    CANCELED(8, "Pedido Cancelado"),
    DELIVERED(9, "Produto Entregue");
    
    companion object {
        @JvmStatic
        fun getByDescription(description: String): OrderStatus =
            entries.firstOrNull { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("No OrderStatus enumeration constant with a description '$description'")
    }
}