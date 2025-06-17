package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.CartModel

@Serializable
data class CartResponse(
    val data: List<CartItem>,
    val msg: String,
) {
    fun toCartModel(): CartModel =
        CartModel(
            data = data.map { it.toCartItemModel() },
            msg = msg,
        )
}
