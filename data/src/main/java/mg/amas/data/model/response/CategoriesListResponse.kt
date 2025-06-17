package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.data.model.CategoryDataModel

@Serializable
data class CategoriesListResponse(
    val `data`: List<CategoryDataModel>,
    val msg: String,
) {
    fun toCategoriesList() =
        mg.amas.domain.model.CategoriesListModel(
            categories = `data`.map { it.toCategory() },
            msg = msg,
        )
}
