package mg.amas.domain.model.request

data class AddCartRequestModel(
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val userId: Long, // Link cart item to the user
)
