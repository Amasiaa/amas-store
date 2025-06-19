package mg.amas.domain.repository

import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartModel
import mg.amas.domain.model.CartSummary
import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(
        request: AddCartRequestModel,
        userId: Long,
    ): ResultWrapper<CartModel>

    suspend fun getCart(userId: Long): ResultWrapper<CartModel>

    suspend fun updateQuantity(
        cartItemModel: CartItemModel,
        userId: Long,
    ): ResultWrapper<CartModel>

    suspend fun deleteItem(
        cartItemId: Int,
        userId: Long,
    ): ResultWrapper<CartModel>

    suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary>
}
