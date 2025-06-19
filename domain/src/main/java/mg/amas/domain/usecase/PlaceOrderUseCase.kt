package mg.amas.domain.usecase

import mg.amas.domain.model.AddressDomainModel
import mg.amas.domain.repository.OrderRepository

class PlaceOrderUseCase(
    private val repository: OrderRepository,
) {
    suspend fun execute(
        addressDomainModel: AddressDomainModel,
        userId: Long,
    ) = repository.placeOrder(addressDomainModel, userId)
}
