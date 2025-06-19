package mg.amas.domain.usecase

import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.repository.CartRepository

class AddToCartUseCase(
    private val repository: CartRepository,
) {
    suspend fun execute(
        request: AddCartRequestModel,
        userId: Long,
    ) = repository.addProductToCart(request = request, userId = userId)
}
