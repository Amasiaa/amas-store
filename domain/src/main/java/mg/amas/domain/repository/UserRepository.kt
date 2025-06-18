package mg.amas.domain.repository

import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.network.ResultWrapper

interface UserRepository {
    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<UserDomainModel>

    suspend fun register(
        email: String,
        password: String,
        name: String,
    ): ResultWrapper<UserDomainModel>
}
