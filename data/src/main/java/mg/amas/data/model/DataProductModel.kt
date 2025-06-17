package mg.amas.data.model

import kotlinx.serialization.Serializable
import mg.amas.domain.model.Product

@Serializable
class DataProductModel(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val title: String,
) {
    fun toProduct() =
        Product(
            id = id,
            title = title,
            price = price,
            categoryId = categoryId,
            description = description,
            image = image,
        )
}
