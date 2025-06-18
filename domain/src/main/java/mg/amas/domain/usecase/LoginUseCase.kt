package mg.amas.domain.usecase

import mg.amas.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun execute(
        username: String,
        password: String,
    ) = userRepository.login(username, password)
}
