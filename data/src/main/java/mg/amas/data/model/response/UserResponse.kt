package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.UserDomainModel

@Serializable
data class UserResponse(
    val id: Int?,
    val username: String,
    val email: String,
    val name: String,
) {
    fun toDomainModel() =
        UserDomainModel(
            id = id,
            username = username,
            email = email,
            name = name,
        )
}
