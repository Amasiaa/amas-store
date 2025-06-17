package mg.amas.domain.usecase

import mg.amas.domain.model.CartItemModel
import mg.amas.domain.repository.CartRepository

class UpdateQuantityUseCase(
    private val cartRepository: CartRepository,
) {
    suspend fun execute(cartItemModel: CartItemModel) = cartRepository.updateQuantity(cartItemModel)
}
