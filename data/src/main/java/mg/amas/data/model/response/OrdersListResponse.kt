package mg.amas.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OrdersListResponse(
    val `data`: List<OrderListData>,
    val msg: String,
) {
    fun toDomainResponse(): mg.amas.domain.model.OrdersListModel =
        mg.amas.domain.model.OrdersListModel(
            `data` = `data`.map { it.toDomainResponse() },
            msg = msg,
        )
}
