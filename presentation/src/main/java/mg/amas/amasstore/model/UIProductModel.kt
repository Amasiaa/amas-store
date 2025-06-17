package mg.amas.amasstore.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import mg.amas.domain.model.Product

@SuppressLint("UnsafeOptInUsageError")
@Serializable
@Parcelize
data class UIProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val categoryId: Int,
    val description: String,
    val image: String,
) : Parcelable {
    companion object {
        fun fromProduct(product: Product) =
            UIProductModel(
                id = product.id,
                title = product.title,
                price = product.price,
                categoryId = product.categoryId,
                description = product.description,
                image = product.image,
            )
    }
}
