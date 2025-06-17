package mg.amas.domain.repository

import mg.amas.domain.model.AddressDomainModel
import mg.amas.domain.model.OrdersListModel
import mg.amas.domain.network.ResultWrapper

interface OrderRepository {
    suspend fun placeOrder(addressDomainModel: AddressDomainModel): ResultWrapper<Long>

    suspend fun getOrderList(): ResultWrapper<OrdersListModel>
}
