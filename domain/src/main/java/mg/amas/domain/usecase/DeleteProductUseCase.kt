package mg.amas.domain.usecase

import mg.amas.domain.repository.CartRepository

class DeleteProductUseCase(
    private val cartRepository: CartRepository,
) {
    suspend fun execute(
        cartItemId: Int,
        userId: Int,
    ) = cartRepository.deleteItem(cartItemId, userId)
}
