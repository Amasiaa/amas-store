package mg.amas.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import mg.amas.data.model.request.AddToCartRequest
import mg.amas.data.model.request.AddressDataModel
import mg.amas.data.model.request.LoginRequest
import mg.amas.data.model.request.RegisterRequest
import mg.amas.data.model.response.CartResponse
import mg.amas.data.model.response.CartSummaryResponse
import mg.amas.data.model.response.CategoriesListResponse
import mg.amas.data.model.response.OrdersListResponse
import mg.amas.data.model.response.PlaceOrderResponse
import mg.amas.data.model.response.ProductListResponse
import mg.amas.data.model.response.UserAuthResponse
import mg.amas.domain.model.AddressDomainModel
import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartModel
import mg.amas.domain.model.CartSummary
import mg.amas.domain.model.CategoriesListModel
import mg.amas.domain.model.OrdersListModel
import mg.amas.domain.model.ProductListModel
import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.model.request.AddCartRequestModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper

class NetworkServiceImpl(
    val client: HttpClient,
) : NetworkService {
    // private val baseUrl = "https://fakestoreapi.com"
    private val baseUrl = "https://ecommerce-ktor-4641e7ff1b63.herokuapp.com/v2"

    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        val url = if (category != null) "$baseUrl/products/category/$category" else "$baseUrl/products"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { dataModels: ProductListResponse ->
                dataModels.toProductList()
            },
        )
    }

    override suspend fun getCategories(): ResultWrapper<CategoriesListModel> {
        val url = "$baseUrl/categories"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { categories: CategoriesListResponse ->
                categories.toCategoriesList()
            },
        )
    }

    override suspend fun addProductToCart(
        request: AddCartRequestModel,
        userId: Long,
    ): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = AddToCartRequest.fromCartRequestModel(request),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            },
        )
    }

    override suspend fun getCart(userId: Long): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            },
        )
    }

    override suspend fun updateQuantity(
        cartItemModel: CartItemModel,
        userId: Long,
    ): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId/${cartItemModel.id}"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Put,
            body =
                AddToCartRequest(
                    productId = cartItemModel.id,
                    quantity = cartItemModel.quantity,
                    price = cartItemModel.price,
                    productName = cartItemModel.productName,
                ),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            },
        )
    }

    override suspend fun deleteItem(
        cartItemId: Int,
        userId: Long,
    ): ResultWrapper<CartModel> {
        val url = "$baseUrl/cart/$userId/$cartItemId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Delete,
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            },
        )
    }

    override suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary> {
        val url = "$baseUrl/checkout/$userId/summary"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { cartSummary: CartSummaryResponse ->
                cartSummary.toCartSummary()
            },
        )
    }

    override suspend fun placeOrder(
        addressDomainModel: AddressDomainModel,
        userId: Long,
    ): ResultWrapper<Long> {
        val dataModel = AddressDataModel.fromDomainAddress(addressDomainModel)
        val url = "$baseUrl/orders/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = dataModel,
            mapper = { orderResponse: PlaceOrderResponse ->
                orderResponse.data.id
            },
        )
    }

    override suspend fun getOrderList(userId: Long): ResultWrapper<OrdersListModel> {
        val url = "$baseUrl/orders/$userId"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { ordersResponse: OrdersListResponse ->
                ordersResponse.toDomainResponse()
            },
        )
    }

    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/login"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = LoginRequest(email, password),
            mapper = { user: UserAuthResponse ->
                user.`data`.toDomainModel()
            },
        )
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
    ): ResultWrapper<UserDomainModel> {
        val url = "$baseUrl/signup"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = RegisterRequest(email, password, name),
            mapper = { user: UserAuthResponse ->
                user.`data`.toDomainModel()
            },
        )
    }

    suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, String> = emptyMap(),
        noinline mapper: ((T) -> R)? = null,
    ): ResultWrapper<R> =
        try {
            val response =
                client
                    .request(url) {
                        this.method = method
                        // Apply query parameters
                        url {
                            this.parameters.appendAll(
                                Parameters.build {
                                    parameters.forEach { (key, value) ->
                                        append(key, value)
                                    }
                                },
                            )
                        }
                        // Apply headers
                        headers.forEach { (key, value) ->
                            header(key, value)
                        }
                        // Set body for POST, PUT, etc.
                        if (body != null) {
                            setBody(body)
                        }
                        // Set content type
                        contentType(ContentType.Application.Json)
                    }.body<T>()
            val result: R = (mapper?.invoke(response) ?: response) as R
            ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            ResultWrapper.Failure(e)
        } catch (e: ServerResponseException) {
            ResultWrapper.Failure(e)
        } catch (e: IOException) {
            ResultWrapper.Failure(e)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
}
