package mg.amas.data.model.request

import kotlinx.serialization.Serializable
import mg.amas.domain.model.request.AddCartRequestModel

@Serializable
data class AddToCartRequest(
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
) {
    companion object {
        fun fromCartRequestModel(addCartRequestModel: AddCartRequestModel) =
            AddToCartRequest(
                productId = addCartRequestModel.productId,
                productName = addCartRequestModel.productName,
                price = addCartRequestModel.price,
                quantity = addCartRequestModel.quantity,
            )
    }
}
