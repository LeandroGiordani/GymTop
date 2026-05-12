package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── UI State ───────────────────────────────────────────────────────────────────

/**
 * LoginUiState — estado da UI da tela de login.
 *
 * @param email        E-mail digitado pelo usuário.
 * @param password     Senha digitada pelo usuário.
 * @param isLoading    true enquanto a chamada ao Firebase está em andamento.
 * @param errorMessage Mensagem de erro; null = sem erro.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ── Navigation Events ──────────────────────────────────────────────────────────

/**
 * Eventos de navegação emitidos pelo [LoginViewModel].
 * Um-shot via Channel — a UI reage e navega sem o ViewModel conhecer o NavController.
 */
sealed class LoginNavigationEvent {
    /** Login bem-sucedido — vai para o app principal. */
    object NavigateToWorkoutList : LoginNavigationEvent()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * LoginViewModel — gerencia o estado e a lógica da tela de login.
 *
 * Responsabilidades:
 *  1. Manter [LoginUiState] com os dados digitados pelo usuário.
 *  2. Validar campos antes de chamar o repositório.
 *  3. Chamar [AuthRepository.signInWithEmail] ao confirmar.
 *  4. Emitir [LoginNavigationEvent] para a UI navegar em caso de sucesso.
 *
 * @param authRepository Injetado pelo Hilt; abstrai a comunicação com Firebase.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ── State ──────────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ── Navigation Events ──────────────────────────────────────────────────────

    private val _navigationEvent = Channel<LoginNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // ── User Actions ───────────────────────────────────────────────────────────

    /** Chamado ao digitar no campo de e-mail. Limpa erro ao mesmo tempo. */
    fun onEmailChanged(value: String) =
        _uiState.update { it.copy(email = value, errorMessage = null) }

    /** Chamado ao digitar no campo de senha. Limpa erro ao mesmo tempo. */
    fun onPasswordChanged(value: String) =
        _uiState.update { it.copy(password = value, errorMessage = null) }

    /** Limpa a mensagem de erro — útil ao entrar/sair da tela. */
    fun clearError() = _uiState.update { it.copy(errorMessage = null) }

    /**
     * Chamado ao pressionar "ENTRAR".
     * Valida os campos e chama o repositório para autenticar com Firebase.
     */
    fun onLoginClicked() {
        val state = _uiState.value
        when {
            state.email.isBlank() ->
                _uiState.update { it.copy(errorMessage = "Informe seu e-mail.") }
            !state.email.contains("@") ->
                _uiState.update { it.copy(errorMessage = "Informe um e-mail válido.") }
            state.password.isBlank() ->
                _uiState.update { it.copy(errorMessage = "Informe sua senha.") }
            else -> viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val result = authRepository.signInWithEmail(
                    email    = state.email,
                    password = state.password
                )

                _uiState.update { it.copy(isLoading = false) }

                result.fold(
                    onSuccess = {
                        _navigationEvent.send(LoginNavigationEvent.NavigateToWorkoutList)
                    },
                    onFailure = { _ ->
                        // Firebase retorna mensagens em inglês; simplificamos para o usuário.
                        _uiState.update {
                            it.copy(errorMessage = "E-mail ou senha incorretos.")
                        }
                    }
                )
            }
        }
    }
}
