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
    override suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> =
        networkService.addProductToCart(request = request)

    override suspend fun getCart(): ResultWrapper<CartModel> = networkService.getCart()

    override suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel> =
        networkService.updateQuantity(cartItemModel)

    override suspend fun deleteItem(
        cartItemId: Int,
        userId: Int,
    ): ResultWrapper<CartModel> = networkService.deleteItem(cartItemId, userId)

    override suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary> = networkService.getCartSummary(userId)
}
