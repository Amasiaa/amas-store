package mg.amas.amasstore.ui.features.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.AmasStoreSession
import mg.amas.domain.model.OrdersData
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.OrderListUseCase

class OrdersViewModel(
    private val orderListUseCase: OrderListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrdersEvent>(OrdersEvent.Loading)
    val uiState = _uiState.asStateFlow()
    val userDomainModel = AmasStoreSession.getUser()

    init {
        getOrderList()
    }

    private fun getOrderList() {
        viewModelScope.launch {
            _uiState.value = OrdersEvent.Loading
            orderListUseCase.execute(userDomainModel!!.id!!.toLong()).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiState.value = OrdersEvent.Success(result.value.data)
                    }
                    is ResultWrapper.Failure -> {
                        _uiState.value = OrdersEvent.Error(message = "Something went wrong")
                    }
                }
            }
        }
    }

    fun filterOrders(
        list: List<OrdersData>,
        filter: String,
    ): List<OrdersData> {
        val filteredList = list.filter { it.status == filter }
        return filteredList
    }
}

sealed class OrdersEvent {
    data object Loading : OrdersEvent()

    data class Success(
        val orders: List<OrdersData>,
    ) : OrdersEvent()

    data class Error(
        val message: String,
    ) : OrdersEvent()
}
