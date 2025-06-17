package mg.amas.amasstore.navigation

import kotlinx.serialization.Serializable
import mg.amas.amasstore.model.UIProductModel

@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object OrdersScreen

@Serializable
object ProfileScreen

@Serializable
object SearchScreen

@Serializable
object CartSummaryScreen

@Serializable
data class UserAddressRoute(
    val userAddressRouteWrapper: UserAddressRouteWrapper,
)

@Serializable
data class ProductDetails(
    val product: UIProductModel,
)
