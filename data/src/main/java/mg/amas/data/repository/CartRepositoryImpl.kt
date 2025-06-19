package mg.amas.data.repository

import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartModel
import mg.amas.domain.model.CartSummary
import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.repository.CartRepository

class CartRepositoryImpl(
    private val networkService: NetworkService,
) : CartRepository {
    override suspend fun addProductToCart(
        request: AddCartRequestModel,
        userId: Long,
    ): ResultWrapper<CartModel> = networkService.addProductToCart(request = request, userId = userId)

    override suspend fun getCart(userId: Long): ResultWrapper<CartModel> = networkService.getCart(userId)

    override suspend fun updateQuantity(
        cartItemModel: CartItemModel,
        userId: Long,
    ): ResultWrapper<CartModel> = networkService.updateQuantity(cartItemModel, userId)

    override suspend fun deleteItem(
        cartItemId: Int,
        userId: Long,
    ): ResultWrapper<CartModel> = networkService.deleteItem(cartItemId, userId)

    override suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary> = networkService.getCartSummary(userId)
}
