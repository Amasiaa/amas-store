package mg.amas.data.repository

import mg.amas.domain.model.AddressDomainModel
import mg.amas.domain.model.OrdersListModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val networkService: NetworkService,
) : OrderRepository {
    override suspend fun placeOrder(addressDomainModel: AddressDomainModel): ResultWrapper<Long> =
        networkService.placeOrder(addressDomainModel, 1)

    override suspend fun getOrderList(): ResultWrapper<OrdersListModel> = networkService.getOrderList()
}
