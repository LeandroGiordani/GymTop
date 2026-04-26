package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SplashViewModel — gerencia o estado e a navegação da SplashScreen.
 *
 * Responsabilidades:
 *  1. Ao inicializar, verificar se há usuário autenticado via [AuthRepository].
 *     - Se sim → emite [SplashNavigationEvent.NavigateToWorkoutList] (login persistido).
 *     - Se não → aguarda ação do usuário nos botões da tela.
 *  2. Expor [SplashNavigationEvent] para a UI reagir e navegar.
 *
 * Padrão de navegação:
 *  O ViewModel NÃO conhece o NavController — apenas emite eventos.
 *  A UI (NavGraph) observa e navega.
 *
 * @param authRepository Injetado pelo Hilt; verifica sessão ativa no Firebase.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvent = Channel<SplashNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // ── Init: check persistent session ────────────────────────────────────────

    init {
        // Verifica sessão ao inicializar: se o usuário já estava logado,
        // navega diretamente para o app sem passar pelo onboarding.
        viewModelScope.launch {
            val loggedInUser = authRepository.getCurrentUser()
            if (loggedInUser != null) {
                _navigationEvent.send(SplashNavigationEvent.NavigateToWorkoutList)
            }
            // Se null: a SplashScreen permanece visível aguardando ação do usuário.
        }
    }

    // ── User Actions ───────────────────────────────────────────────────────────

    /**
     * Chamado quando o usuário pressiona "COMEÇAR" (novo usuário).
     * Direciona para o fluxo de onboarding (cadastro).
     */
    fun onStartClicked() {
        viewModelScope.launch {
            _navigationEvent.send(SplashNavigationEvent.NavigateToOnboarding)
        }
    }

    /**
     * Chamado quando o usuário pressiona "ENTRAR" (usuário retornante).
     * Navega para a tela de login.
     */
    fun onEnterClicked() {
        viewModelScope.launch {
            _navigationEvent.send(SplashNavigationEvent.NavigateToLogin)
        }
    }
}

// ── Navigation Events ──────────────────────────────────────────────────────────

/**
 * Eventos de navegação emitidos pelo [SplashViewModel].
 * A UI observa estes eventos e chama o NavController correspondente.
 *
 * Usar sealed class garante que todos os destinos possíveis sejam tratados
 * explicitamente no `when` da UI (type-safety).
 */
sealed class SplashNavigationEvent {
    /** Usuário já autenticado — vai direto para o app principal. */
    object NavigateToWorkoutList : SplashNavigationEvent()

    /** Novo usuário — inicia o fluxo de cadastro (OnboardingInfoScreen). */
    object NavigateToOnboarding : SplashNavigationEvent()

    /** Usuário retornante — vai para a tela de login. */
    object NavigateToLogin : SplashNavigationEvent()
}
