package com.example.gymtop.domain.model

/**
 * Gender — enum que representa as opções de gênero disponíveis no cadastro.
 *
 * Usar enum garante type-safety: o código nunca aceita uma string arbitrária
 * onde Gender é esperado. O [displayName] é usado na UI (labels dos botões).
 *
 * Armazenado no Firestore como a string [name] (ex: "MASCULINO").
 */
enum class Gender(val displayName: String) {
    MASCULINO("MASCULINO"),
    FEMININO("FEMININO"),
    OUTRO("OUTRO")
}

