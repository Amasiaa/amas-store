package mg.amas.amasstore.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import mg.amas.amasstore.model.UserAddress

@Serializable
@Parcelize
data class UserAddressRouteWrapper(
    val userAddress: UserAddress?,
) : Parcelable
