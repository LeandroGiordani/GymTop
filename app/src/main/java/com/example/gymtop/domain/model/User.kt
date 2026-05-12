package com.example.gymtop.domain.model

/**
 * User — modelo de domínio que representa o usuário autenticado.
 *
 * Esta classe é independente do Firebase: a camada de domínio não conhece
 * FirebaseUser, garantindo que a lógica de negócio seja portável
 * (ex: futura migração para Kotlin Multiplatform / iOS).
 *
 * Campos:
 * @param uid     Identificador único gerado pelo Firebase Auth.
 * @param name    Nome completo informado no onboarding.
 * @param email   E-mail usado no cadastro/login.
 * @param gender  Gênero selecionado no onboarding — ver [Gender].
 */
data class User(
    val uid: String,
    val name: String,
    val email: String,
    val gender: Gender
)

