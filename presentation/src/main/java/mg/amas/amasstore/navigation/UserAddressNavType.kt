package mg.amas.amasstore.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json

val userAddressNavType =
    object : NavType<UserAddressRouteWrapper>(isNullableAllowed = false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: UserAddressRouteWrapper,
        ) {
            bundle.putParcelable(key, value)
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): UserAddressRouteWrapper? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, UserAddressRouteWrapper::class.java)
            } else {
                bundle.getParcelable(key) as? UserAddressRouteWrapper
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun parseValue(value: String): UserAddressRouteWrapper {
            val item = Json.decodeFromString<UserAddressRouteWrapper>(value)
            return item
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun serializeAsValue(value: UserAddressRouteWrapper): String =
            Json.encodeToString(
                serializer = UserAddressRouteWrapper.serializer(),
                value,
            )
    }
