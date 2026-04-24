package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtop.domain.model.Gender
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary
import com.example.gymtop.ui.theme.GymTopSurface

/**
 * OnboardingInfoScreen — primeira tela do fluxo de cadastro de novo usuário.
 *
 * Layout baseado no design "CONFIGURAR PERFIL":
 *  - Cabeçalho "GYM TOP" em neon
 *  - Título de duas linhas: "CONFIGURAR" (branco) + "PERFIL" (neon)
 *  - Subtítulo descritivo
 *  - Campo "Nome Completo" com ícone de pessoa
 *  - Campo "Email" com ícone de envelope
 *  - Seletor de gênero: 3 botões (MASCULINO | FEMININO | OUTRO)
 *    O botão selecionado fica com fundo neon; os demais com fundo [GymTopSurface].
 *  - FAB com seta no canto inferior esquerdo para avançar ao próximo passo.
 *
 * @param name            Valor atual do campo Nome.
 * @param email           Valor atual do campo Email.
 * @param selectedGender  Gênero atualmente selecionado (null = nenhum).
 * @param errorMessage    Mensagem de erro exibida abaixo do seletor de gênero.
 * @param onNameChanged         Callback disparado ao digitar no campo Nome.
 * @param onEmailChanged        Callback disparado ao digitar no campo Email.
 * @param onGenderSelected      Callback disparado ao tocar num botão de gênero.
 * @param onContinueClicked     Callback disparado ao pressionar o FAB.
 */
@Composable
fun OnboardingInfoScreen(
    name: String = "",
    email: String = "",
    selectedGender: Gender? = null,
    errorMessage: String? = null,
    onNameChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onGenderSelected: (Gender) -> Unit = {},
    onContinueClicked: () -> Unit = {}
) {
    // ── Root container ─────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymTopBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            // ── "GYM TOP" app name header ──────────────────────────────────────
            Text(
                text = "GYM TOP",
                color = GymTopNeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))

            // ── Title: "CONFIGURAR" (white) + "PERFIL" (neon) ─────────────────
            Text(
                text = "CONFIGURAR",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "PERFIL",
                color = GymTopNeonGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(20.dp))

            // ── Subtitle ───────────────────────────────────────────────────────
            Text(
                text = "Bem-vindo ao motor da sua evolução.\nPreencha seus dados para começarmos a monitorar sua performance.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(32.dp))

            // ── "Nome Completo" field ──────────────────────────────────────────
            // OutlinedTextField: variante Material3 com borda visível e ícone
            OutlinedTextField(
                value = name,
                onValueChange = onNameChanged,
                placeholder = {
                    Text(
                        text = "Nome Completo",
                        color = Color.White.copy(alpha = 0.4f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // ── "Email" field ──────────────────────────────────────────────────
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChanged,
                placeholder = {
                    Text(
                        text = "Email",
                        color = Color.White.copy(alpha = 0.4f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            // ── "GÊNERO" label ─────────────────────────────────────────────────
            Text(
                text = "GÊNERO",
                color = GymTopNeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.height(12.dp))

            // ── Gender selector row ────────────────────────────────────────────
            // Três botões com peso igual; o selecionado recebe fundo neon.
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Gender.entries.forEach { gender ->
                    GenderButton(
                        gender = gender,
                        isSelected = selectedGender == gender,
                        onClick = { onGenderSelected(gender) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Inline error message ───────────────────────────────────────────
            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // ── FAB (bottom-left) — avança para criação de senha ──────────────────
        // Posicionado com Box + align para ficar fixo no canto inferior esquerdo,
        // independente do conteúdo acima.
        FloatingActionButton(
            onClick = onContinueClicked,
            containerColor = GymTopNeonGreen,
            contentColor = GymTopOnPrimary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Continuar",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ── Gender Button ──────────────────────────────────────────────────────────────

/**
 * GenderButton — botão de seleção de gênero.
 *
 * Quando [isSelected] = true: fundo [GymTopNeonGreen], texto [GymTopOnPrimary].
 * Quando [isSelected] = false: fundo [GymTopSurface], texto branco semi-transparente.
 *
 * Implementado como Box clicável (em vez de Button) para permitir
 * controle total sobre aparência sem estilos padrão do Material.
 */
@Composable
private fun GenderButton(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) GymTopNeonGreen else GymTopSurface
    val textColor = if (isSelected) GymTopOnPrimary else Color.White.copy(alpha = 0.7f)
    val borderColor = if (isSelected) GymTopNeonGreen else Color.White.copy(alpha = 0.15f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        // Gender icon above the label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = genderIcon(gender),
                fontSize = 18.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = gender.displayName,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

/** Simple emoji/symbol representation for each gender option. */
private fun genderIcon(gender: Gender): String = when (gender) {
    Gender.MASCULINO -> "♂"
    Gender.FEMININO  -> "♀"
    Gender.OUTRO     -> "⚧"
}

// ── TextField colors helper ────────────────────────────────────────────────────

/**
 * Centraliza as cores do OutlinedTextField para manter consistência
 * visual com o tema escuro do GymTop.
 */
@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor       = Color.White,
    unfocusedTextColor     = Color.White,
    focusedBorderColor     = GymTopNeonGreen,
    unfocusedBorderColor   = Color.White.copy(alpha = 0.25f),
    cursorColor            = GymTopNeonGreen,
    focusedContainerColor  = GymTopSurface,
    unfocusedContainerColor= GymTopSurface
)

// ── Preview ────────────────────────────────────────────────────────────────────

@Preview(widthDp = 390, heightDp = 844, showBackground = true, backgroundColor = 0xFF0E0E0E)
@Composable
private fun OnboardingInfoScreenPreview() {
    OnboardingInfoScreen(
        name = "",
        email = "",
        selectedGender = Gender.FEMININO
    )
}



