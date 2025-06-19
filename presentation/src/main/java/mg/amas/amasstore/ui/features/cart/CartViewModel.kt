package mg.amas.amasstore.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.AmasStoreSession
import mg.amas.domain.model.CartItemModel
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.DeleteProductUseCase
import mg.amas.domain.usecase.GetCartUseCase
import mg.amas.domain.usecase.UpdateQuantityUseCase

class CartViewModel(
    private val cartUseCase: GetCartUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase,
    private val deleteItemUseCase: DeleteProductUseCase,
) : ViewModel() {
    private val _uiEvent = MutableStateFlow<CartEvent>(CartEvent.Loading)
    val uiEvent = _uiEvent.asStateFlow()
    val userDomainModel = AmasStoreSession.getUser()

    init {
        getCart()
    }

    fun getCart() {
        viewModelScope.launch {
            _uiEvent.value = CartEvent.Loading
            cartUseCase.execute(userDomainModel!!.id!!.toLong()).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiEvent.value = CartEvent.Success(cartItems = result.value.data)
                    }
                    is ResultWrapper.Failure -> {
                        _uiEvent.value = CartEvent.Error(message = "Something went wrong!")
                    }
                }
            }
        }
    }

    fun incrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 10) return
        updateQuantity(cartItem.copy(quantity = cartItem.quantity + 1))
    }

    fun decrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 1) return
        updateQuantity(cartItem.copy(quantity = cartItem.quantity - 1))
    }

    private fun updateQuantity(cartItem: CartItemModel) {
        viewModelScope.launch {
            _uiEvent.value = CartEvent.Loading
            updateQuantityUseCase.execute(cartItem, userDomainModel!!.id!!.toLong()).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiEvent.value = CartEvent.Success(cartItems = result.value.data)
                    }
                    is ResultWrapper.Failure -> {
                        _uiEvent.value = CartEvent.Error(message = "Something went wrong!")
                    }
                }
            }
        }
    }

    fun removeItem(cartItem: CartItemModel) {
        viewModelScope.launch {
            _uiEvent.value = CartEvent.Loading
            deleteItemUseCase.execute(cartItem.id, 1).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _uiEvent.value = CartEvent.Success(cartItems = result.value.data)
                    }
                    is ResultWrapper.Failure -> {
                        _uiEvent.value = CartEvent.Error(message = "Something went wrong!")
                    }
                }
            }
        }
    }
}

sealed class CartEvent {
    object Loading : CartEvent()

    data class Success(
        val cartItems: List<CartItemModel>,
    ) : CartEvent()

    data class Error(
        val message: String,
    ) : CartEvent()
}
