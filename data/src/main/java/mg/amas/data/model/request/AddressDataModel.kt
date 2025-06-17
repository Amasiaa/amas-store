package mg.amas.data.model.request

import kotlinx.serialization.Serializable
import mg.amas.domain.model.AddressDomainModel

@Serializable
data class AddressDataModel(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
) {
    companion object {
        fun fromDomainAddress(addressDomainModel: AddressDomainModel) =
            AddressDataModel(
                addressLine = addressDomainModel.addressLine,
                city = addressDomainModel.city,
                state = addressDomainModel.state,
                postalCode = addressDomainModel.postalCode,
                country = addressDomainModel.country,
            )
    }
}
