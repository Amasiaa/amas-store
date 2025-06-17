package mg.amas.amasstore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import mg.amas.domain.model.AddressDomainModel

@Serializable
@Parcelize
data class UserAddress(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
) : Parcelable {
    override fun toString(): String = "$addressLine, $city, $state, $postalCode, $country"

    fun toAddressDomainModel() =
        AddressDomainModel(
            addressLine,
            city,
            state,
            postalCode,
            country,
        )
}
