package mg.amas.domain.usecase

import mg.amas.domain.repository.CartRepository

class CartSummaryUseCase(
    private val repository: CartRepository,
) {
    suspend fun execute(userId: Long) = repository.getCartSummary(userId)
}
