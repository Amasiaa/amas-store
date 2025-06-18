package mg.amas.amasstore.ui.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.AmasStoreSession
import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginEvent>(LoginEvent.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(
        email: String,
        password: String,
    ) {
        _loginState.value = LoginEvent.Loading
        viewModelScope.launch {
            loginUseCase.execute(email, password).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        AmasStoreSession.storeUser(user = result.value)
                        _loginState.value = LoginEvent.Success(user = result.value)
                    }
                    is ResultWrapper.Failure -> {
                        _loginState.value = LoginEvent.Error(message = result.exception.message ?: "Something went wrong")
                    }
                }
            }
        }
    }
}

sealed class LoginEvent {
    object Idle : LoginEvent()

    object Loading : LoginEvent()

    data class Success(
        val user: UserDomainModel,
    ) : LoginEvent()

    data class Error(
        val message: String,
    ) : LoginEvent()
}
