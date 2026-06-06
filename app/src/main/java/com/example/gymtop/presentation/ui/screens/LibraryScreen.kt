package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * LibraryScreen — placeholder para a tela de biblioteca de exercícios.
 *
 * Exibirá a lista de exercícios disponíveis carregados do JSON local
 * (exercise_library.json). Implementação completa em sprint futuro.
 */
@Composable
fun LibraryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Biblioteca de Exercícios — Em breve",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
