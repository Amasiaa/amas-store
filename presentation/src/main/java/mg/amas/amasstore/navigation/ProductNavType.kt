package mg.amas.amasstore.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json
import mg.amas.amasstore.model.UIProductModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64

val productNavType =
    object : NavType<UIProductModel>(isNullableAllowed = false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: UIProductModel,
        ) {
            bundle.putParcelable(key, value)
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): UIProductModel? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, UIProductModel::class.java)
            } else {
                bundle.getParcelable(key) as? UIProductModel
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun parseValue(value: String): UIProductModel {
            val item = Json.decodeFromString<UIProductModel>(value)
            return item.copy(
                image = URLDecoder.decode(item.image, "UTF-8"),
                description = String(Base64.getDecoder().decode(item.description.replace("_", "/"))),
                title = String(Base64.getDecoder().decode(item.title.replace("_", "/"))),
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun serializeAsValue(value: UIProductModel): String =
            Json.encodeToString(
                serializer = UIProductModel.serializer(),
                value.copy(
                    image = URLEncoder.encode(value.image, "UTF-8"),
                    description = String(Base64.getEncoder().encode(value.description.toByteArray())).replace("/", "_"),
                    title = String(Base64.getEncoder().encode(value.title.toByteArray())).replace("/", "_"),
                ),
            )
    }
