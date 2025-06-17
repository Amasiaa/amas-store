package mg.amas.domain.repository

import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartModel
import mg.amas.domain.model.CartSummary
import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>

    suspend fun getCart(): ResultWrapper<CartModel>

    suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel>

    suspend fun deleteItem(
        cartItemId: Int,
        userId: Int,
    ): ResultWrapper<CartModel>

    suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary>
}
