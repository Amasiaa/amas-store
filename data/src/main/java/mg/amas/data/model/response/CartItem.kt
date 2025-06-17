package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.CartItemModel

@Serializable
data class CartItem(
    val id: Int,
    val productId: Int,
    val price: Double,
    val imageUrl: String? = null,
    val quantity: Int,
    val productName: String,
) {
    fun toCartItemModel(): CartItemModel =
        CartItemModel(
            id = id,
            productId = productId,
            productName = productName,
            price = price,
            imageUrl = imageUrl,
            quantity = quantity,
        )
}
