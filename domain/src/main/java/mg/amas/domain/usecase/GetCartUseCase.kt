package mg.amas.domain.usecase

import mg.amas.domain.repository.CartRepository

class GetCartUseCase(
    private val cartRepository: CartRepository,
) {
    suspend fun execute(userId: Long) = cartRepository.getCart(userId)
}
