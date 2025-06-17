package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.data.model.DataProductModel
import mg.amas.domain.model.ProductListModel

@Serializable
data class ProductListResponse(
    val `data`: List<DataProductModel>,
    val msg: String,
) {
    fun toProductList() = ProductListModel(products = `data`.map { it.toProduct() }, msg = msg)
}
