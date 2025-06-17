package mg.amas.domain.repository

import mg.amas.domain.model.ProductListModel
import mg.amas.domain.network.ResultWrapper

interface ProductRepository {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
}
