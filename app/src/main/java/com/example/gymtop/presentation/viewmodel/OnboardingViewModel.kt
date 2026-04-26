package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.domain.model.Gender
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
 * OnboardingUiState — estado da UI compartilhado entre OnboardingInfoScreen
 * e CreatePasswordScreen.
 *
 * Usar um único estado para as duas telas permite que o ViewModel retenha
 * os dados digitados na primeira tela enquanto o usuário avança para a segunda.
 *
 * @param name            Nome digitado na tela de informações.
 * @param email           E-mail digitado na tela de informações.
 * @param gender          Gênero selecionado (null = nenhum selecionado ainda).
 * @param password        Senha digitada na tela de criação de senha.
 * @param confirmPassword Confirmação de senha.
 * @param isLoading       true enquanto a chamada ao Firebase está em andamento.
 * @param errorMessage    Mensagem de erro exibida na UI; null = sem erro.
 */
data class OnboardingUiState(
    val name: String = "",
    val email: String = "",
    val gender: Gender? = null,
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ── Navigation Events ──────────────────────────────────────────────────────────

/**
 * Eventos de navegação emitidos pelo [OnboardingViewModel].
 * Um-shot via Channel — a UI reage e navega sem o ViewModel conhecer o NavController.
 */
sealed class OnboardingNavigationEvent {
    /** Avança da tela de informações para a tela de criação de senha. */
    object NavigateToCreatePassword : OnboardingNavigationEvent()

    /** Cadastro concluído — redireciona para o login para o usuário autenticar. */
    object NavigateToLogin : OnboardingNavigationEvent()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * OnboardingViewModel — gerencia o fluxo de cadastro de novo usuário.
 *
 * Responsabilidades:
 *  1. Manter [OnboardingUiState] com os dados digitados pelo usuário.
 *  2. Validar campos antes de avançar.
 *  3. Chamar [AuthRepository.createAccountWithEmail] na confirmação.
 *  4. Emitir [OnboardingNavigationEvent] para a UI navegar.
 *
 * @param authRepository Injetado pelo Hilt; abstrai a comunicação com Firebase.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ── State ──────────────────────────────────────────────────────────────────

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    // ── Navigation Events ──────────────────────────────────────────────────────

    private val _navigationEvent = Channel<OnboardingNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // ── Screen 1: User Info Actions ────────────────────────────────────────────

    fun onNameChanged(value: String) = _uiState.update { it.copy(name = value, errorMessage = null) }
    fun onEmailChanged(value: String) = _uiState.update { it.copy(email = value, errorMessage = null) }
    fun onGenderSelected(gender: Gender) = _uiState.update { it.copy(gender = gender, errorMessage = null) }

    /**
     * Chamado ao pressionar o FAB "→" na OnboardingInfoScreen.
     * Valida os campos antes de navegar para a tela de senha.
     */
    fun onInfoContinueClicked() {
        val state = _uiState.value
        when {
            state.name.isBlank() ->
                _uiState.update { it.copy(errorMessage = "Informe seu nome completo.") }
            !state.email.contains("@") ->
                _uiState.update { it.copy(errorMessage = "Informe um e-mail válido.") }
            state.gender == null ->
                _uiState.update { it.copy(errorMessage = "Selecione seu gênero.") }
            else ->
                viewModelScope.launch {
                    _navigationEvent.send(OnboardingNavigationEvent.NavigateToCreatePassword)
                }
        }
    }

    // ── Screen 2: Password Actions ─────────────────────────────────────────────

    fun onPasswordChanged(value: String) = _uiState.update { it.copy(password = value, errorMessage = null) }
    fun onConfirmPasswordChanged(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }

    /**
     * Chamado ao pressionar "CRIAR CONTA" na CreatePasswordScreen.
     * Valida senha e chama o repositório para criar a conta no Firebase.
     */
    fun onCreateAccountClicked() {
        val state = _uiState.value
        when {
            state.password.length < 6 ->
                _uiState.update { it.copy(errorMessage = "A senha deve ter ao menos 6 caracteres.") }
            state.password != state.confirmPassword ->
                _uiState.update { it.copy(errorMessage = "As senhas não coincidem.") }
            else -> viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val result = authRepository.createAccountWithEmail(
                    name     = state.name,
                    email    = state.email,
                    gender   = state.gender ?: Gender.OUTRO,
                    password = state.password
                )

                _uiState.update { it.copy(isLoading = false) }

                result.fold(
                    onSuccess = {
                        // Conta criada com sucesso — envia o usuário para o login
                        // para que ele autentique com as credenciais recém-criadas.
                        _navigationEvent.send(OnboardingNavigationEvent.NavigateToLogin)
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(errorMessage = error.localizedMessage ?: "Erro ao criar conta.")
                        }
                    }
                )
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}

