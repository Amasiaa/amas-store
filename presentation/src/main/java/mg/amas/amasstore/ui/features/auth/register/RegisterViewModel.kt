package mg.amas.amasstore.ui.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mg.amas.amasstore.AmasStoreSession
import mg.amas.domain.model.UserDomainModel
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.usecase.RegisterUseCase

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterEvent>(RegisterEvent.Idle)
    val registerState = _registerState.asStateFlow()

    fun register(
        name: String,
        email: String,
        password: String,
    ) {
        _registerState.value = RegisterEvent.Loading
        viewModelScope.launch {
            registerUseCase.execute(email, password, name).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        AmasStoreSession.storeUser(user = result.value)
                        _registerState.value = RegisterEvent.Success(user = result.value)
                    }
                    is ResultWrapper.Failure -> {
                        _registerState.value = RegisterEvent.Error(message = result.exception.message ?: "Something went wrong")
                    }
                }
            }
        }
    }
}

sealed class RegisterEvent {
    object Idle : RegisterEvent()

    object Loading : RegisterEvent()

    data class Success(
        val user: UserDomainModel,
    ) : RegisterEvent()

    data class Error(
        val message: String,
    ) : RegisterEvent()
}
