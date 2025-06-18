package mg.amas.data.repository

import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.repository.UserRepository

class UserRepositoryImpl(
    private val networkService: NetworkService,
) : UserRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<UserDomainModel> = networkService.login(email = email, password = password)

    override suspend fun register(
        email: String,
        password: String,
        name: String,
    ): ResultWrapper<UserDomainModel> = networkService.register(name = name, email = email, password = password)
}
