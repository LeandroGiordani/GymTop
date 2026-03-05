package com.example.gymtop.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymtop.domain.model.Workout

/**
 * WorkoutListItem - Composable que representa um item da lista de treinos.
 *
 * Este componente é propositalmente simples (esqueleto) para o MVP e aprendizado.
 * Comentários explicam o que você normalmente faria ao estender:
 * - Estilizar com Material 3 tokens (cores, tipografia, shapes)
 * - Suportar estados como selectable/expanded
 * - Adicionar suporte a swipe-to-dismiss usando Modifier.pointerInput + Animations
 */
@Composable
fun WorkoutListItem(
    workout: Workout,
    onItemClick: (Long) -> Unit,
    onDeleteClick: (Workout) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onItemClick(workout.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = workout.title)
            IconButton(onClick = { onDeleteClick(workout) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir")
            }
        }
    }
}

