package mg.amas.domain.network

import mg.amas.domain.model.AddressDomainModel
import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartModel
import mg.amas.domain.model.CartSummary
import mg.amas.domain.model.CategoriesListModel
import mg.amas.domain.model.OrdersListModel
import mg.amas.domain.model.ProductListModel
import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>

    suspend fun getCategories(): ResultWrapper<CategoriesListModel>

    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>

    suspend fun getCart(): ResultWrapper<CartModel>

    suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel>

    suspend fun deleteItem(
        cartItemId: Int,
        userId: Int,
    ): ResultWrapper<CartModel>

    suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary>

    suspend fun placeOrder(
        addressDomainModel: AddressDomainModel,
        userId: Int,
    ): ResultWrapper<Long>

    suspend fun getOrderList(): ResultWrapper<OrdersListModel>

    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<UserDomainModel>

    suspend fun register(
        email: String,
        password: String,
        name: String,
    ): ResultWrapper<UserDomainModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(
        val value: T,
    ) : ResultWrapper<T>()

    data class Failure(
        val exception: Exception,
    ) : ResultWrapper<Nothing>()
}
