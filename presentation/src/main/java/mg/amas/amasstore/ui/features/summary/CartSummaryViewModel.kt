package mg.amas.amasstore.ui.features.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.model.UserAddress
import mg.amas.domain.model.CartSummary
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.CartSummaryUseCase
import mg.amas.domain.usecase.PlaceOrderUseCase

class CartSummaryViewModel(
    private val cartSummaryUseCase: CartSummaryUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CartSummaryEvent>(CartSummaryEvent.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getCartSummary(1)
    }

    private fun getCartSummary(userId: Int) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            cartSummaryUseCase.execute(userId).let { summary ->
                when (summary) {
                    is ResultWrapper.Success -> {
                        _uiState.value = CartSummaryEvent.Success(summary.value)
                    }

                    is ResultWrapper.Failure -> {
                        _uiState.value = CartSummaryEvent.Error("Something went wrong")
                    }
                }
            }
        }
    }

    fun placeOrder(userAddress: UserAddress) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            placeOrderUseCase.execute(userAddress.toAddressDomainModel()).let { orderId ->
                when (orderId) {
                    is ResultWrapper.Success -> {
                        _uiState.value = CartSummaryEvent.PlaceOrder(orderId.value)
                    }
                    is ResultWrapper.Failure -> {
                        _uiState.value = CartSummaryEvent.Error("Something went wrong")
                    }
                }
            }
        }
    }
}

sealed class CartSummaryEvent {
    data object Loading : CartSummaryEvent()

    data class Success(
        val summary: CartSummary,
    ) : CartSummaryEvent()

    data class Error(
        val message: String,
    ) : CartSummaryEvent()

    data class PlaceOrder(
        val orderId: Long,
    ) : CartSummaryEvent()
}
