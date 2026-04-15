package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * SplashViewModel — gerencia o estado e a navegação da SplashScreen.
 *
 * Responsabilidades:
 *  2. Expor [SplashNavigationEvent] para a UI reagir e navegar sem conhecer o NavController.
 *
 * Padrão de navegação:
 *  O ViewModel NÃO conhece o NavController — ele apenas emite um [SplashNavigationEvent].
 *  A UI observa esse evento e chama o callback de navegação adequado.
 *  Após consumir o evento, a UI deve chamar [onNavigationEventConsumed] para limpar o estado.
 *
 */
@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    // ── Navigation Events (one-shot) ───────────────────────────────────────────
    // StateFlow com valor nullable: null = nenhum evento pendente.
    // A UI lê o evento, navega, e chama onNavigationEventConsumed() para limpar.

    private val _navigationEvent = MutableStateFlow<SplashNavigationEvent?>(null)
    val navigationEvent: StateFlow<SplashNavigationEvent?> = _navigationEvent

    // ── User Actions ───────────────────────────────────────────────────────────

    /**
     * Chamado quando o usuário pressiona "COMEÇAR" (novo usuário / onboarding).
     * Atualmente navega direto para a lista de treinos.
     * TODO: redirecionar para tela de onboarding quando ela existir.
     */
    fun onStartClicked() {
        _navigationEvent.value = SplashNavigationEvent.NavigateToWorkoutList
    }

    /**
     * Chamado quando o usuário pressiona "ENTRAR" (usuário retornante / login).
     * Atualmente navega para a lista de treinos enquanto autenticação não está implementada.
     * TODO: redirecionar para tela de login quando autenticação for adicionada.
     */
    fun onEnterClicked() {
        _navigationEvent.value = SplashNavigationEvent.NavigateToWorkoutList
    }

    /**
     * Deve ser chamado pela UI logo após consumir um [SplashNavigationEvent],
     * para evitar que o mesmo evento seja processado novamente em recomposições.
     */
    fun onNavigationEventConsumed() {
        _navigationEvent.value = null
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
    /** Navegar para a lista de treinos (destino principal do app). */
    object NavigateToWorkoutList : SplashNavigationEvent()

    // TODO: adicionar quando autenticação for implementada
    // object NavigateToLogin : SplashNavigationEvent()

    // TODO: adicionar quando tela de onboarding for implementada
    // object NavigateToOnboarding : SplashNavigationEvent()
}
