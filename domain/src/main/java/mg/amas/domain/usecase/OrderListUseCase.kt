package mg.amas.domain.usecase

import mg.amas.domain.repository.OrderRepository

class OrderListUseCase(
    private val orderRepository: OrderRepository,
) {
    suspend fun execute(userId: Long) = orderRepository.getOrderList(userId)
}
