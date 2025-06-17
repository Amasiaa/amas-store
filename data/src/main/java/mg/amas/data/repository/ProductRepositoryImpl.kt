package mg.amas.data.repository

import mg.amas.domain.model.ProductListModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val networkService: NetworkService,
) : ProductRepository {
    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> = networkService.getProducts(category)
}
