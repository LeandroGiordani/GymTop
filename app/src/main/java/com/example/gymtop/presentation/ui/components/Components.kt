package com.example.gymtop.presentation.ui.components

import androidx.compose.runtime.Composable

/**
 * Components - Arquivo centralizado para componentes reutilizáveis
 *
 * Padrão Compose:
 * Componentes reutilizáveis tornam o código mais limpo e mantível.
 * Cada tela que precisa exibir um "card de treino" usa o mesmo componente,
 * garantindo consistência visual.
 *
 * Benefícios:
 * - Reutilização de código
 * - Consistência visual
 * - Facilita manutenção (mudar design em um lugar)
 * - Facilita testes
 *
 * TODO: Implementar componentes conforme necessário
 *
 * Exemplos de componentes a criar:
 * - WorkoutItem: Card que exibe um treino em uma lista
 * - ExerciseItem: Card que exibe um exercício
 * - WorkoutInputField: TextField para nome de treino
 * - ExerciseInputFields: TextFields para dados de exercício
 * - DeleteConfirmationDialog: Dialog para confirmar deleção
 */

/**
 * WorkoutItem - TODO: Implementar
 * Componente que exibe um treino em card
 * @param workout Treino a exibir
 * @param onItemClick Callback quando clica no card
 * @param onDeleteClick Callback quando clica em deletar
 */
@Composable
fun WorkoutItem(
    // TODO: Implementar
) {
    // Card com nome, data, descrição do treino
    // Botões de editar e deletar
}

/**
 * ExerciseItem - TODO: Implementar
 * Componente que exibe um exercício em card
 * @param exercise Exercício a exibir
 * @param onEditClick Callback para editar
 * @param onDeleteClick Callback para deletar
 */
@Composable
fun ExerciseItem(
    // TODO: Implementar
) {
    // Card com nome, sets, reps, peso do exercício
    // Botões de editar e deletar
}

/**
 * TODO: Adicionar mais componentes conforme necessário
 * - WorkoutInputField
 * - ExerciseInputFields
 * - DeleteConfirmationDialog
 * - LoadingIndicator
 * - ErrorMessage
 * etc.
 */

