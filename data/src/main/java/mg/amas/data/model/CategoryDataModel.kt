package mg.amas.data.model

import kotlinx.serialization.Serializable
import mg.amas.domain.model.Category

@Serializable
data class CategoryDataModel(
    val id: Int,
    val image: String,
    val title: String,
) {
    fun toCategory() = Category(id = id, image = image, title = title)
}
