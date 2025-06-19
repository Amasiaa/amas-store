package mg.amas.amasstore.ui.features.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.AmasStoreSession
import mg.amas.amasstore.model.UIProductModel
import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.AddToCartUseCase

class ProductDetailsViewModel(
    val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {
    private val _uiEvent = MutableStateFlow<ProductDetailsEvent>(ProductDetailsEvent.Nothing)
    val uiEvent = _uiEvent.asStateFlow()
    val userDomainModel = AmasStoreSession.getUser()

    fun addProductToCart(product: UIProductModel) {
        viewModelScope.launch {
            _uiEvent.value = ProductDetailsEvent.Loading
            val result =
                addToCartUseCase.execute(
                    AddCartRequestModel(
                        product.id,
                        product.title,
                        product.price,
                        1,
                        userDomainModel!!.id!!.toLong(),
                    ),
                    userDomainModel.id!!.toLong(),
                )
            when (result) {
                is ResultWrapper.Success -> {
                    _uiEvent.value = ProductDetailsEvent.Success("Product added to cart")
                }
                is ResultWrapper.Failure -> {
                    _uiEvent.value = ProductDetailsEvent.Error("Something went wrong!")
                }
            }
        }
    }
}

sealed class ProductDetailsEvent {
    data object Loading : ProductDetailsEvent()

    data object Nothing : ProductDetailsEvent()

    data class Success(
        val message: String,
    ) : ProductDetailsEvent()

    data class Error(
        val message: String,
    ) : ProductDetailsEvent()
}
