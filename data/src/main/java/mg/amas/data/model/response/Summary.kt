package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.SummaryData

@Serializable
data class Summary(
    val discount: Double,
    val items: List<CartItem>,
    val shipping: Double,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
) {
    fun toSummaryData() =
        SummaryData(
            discount = discount,
            items = items.map { it.toCartItemModel() },
            shipping = shipping,
            subtotal = subtotal,
            tax = tax,
            total = total,
        )
}
