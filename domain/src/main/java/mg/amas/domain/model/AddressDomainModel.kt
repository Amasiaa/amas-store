package mg.amas.domain.model

data class AddressDomainModel(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
)
