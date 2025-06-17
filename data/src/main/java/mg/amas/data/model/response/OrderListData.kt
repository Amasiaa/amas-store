package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.OrdersData

@Serializable
data class OrderListData(
    val id: Int,
    val items: List<OrderItem>,
    val orderDate: String,
    val status: String,
    val totalAmount: Double,
    val userId: Int,
) {
    fun toDomainResponse(): OrdersData =
        OrdersData(
            id = id,
            items = items.map { it.toDomainResponse() },
            orderDate = orderDate,
            status = status,
            totalAmount = totalAmount,
            userId = userId,
        )
}
