package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary
import com.example.gymtop.ui.theme.GymTopSurface

/**
 * CreatePasswordScreen — segunda tela do fluxo de cadastro.
 *
 * Segue o mesmo estilo visual da [OnboardingInfoScreen]:
 *  - Cabeçalho "GYM TOP"
 *  - Título "CRIAR" (branco) + "SENHA" (neon)
 *  - Subtítulo com instrução mínima
 *  - Campo "Senha" com ícone de cadeado e toggle de visibilidade
 *  - Campo "Confirmar Senha" com mesmos controles
 *  - FAB com seta no canto inferior esquerdo para criar a conta
 *  - Indicador de loading enquanto a chamada ao Firebase está em andamento
 *
 * @param password            Valor atual do campo Senha.
 * @param confirmPassword     Valor atual do campo Confirmar Senha.
 * @param isLoading           true durante a criação de conta (desabilita o FAB).
 * @param errorMessage        Mensagem de erro; null = sem erro.
 * @param onPasswordChanged         Callback ao digitar no campo Senha.
 * @param onConfirmPasswordChanged  Callback ao digitar no campo Confirmar Senha.
 * @param onCreateAccountClicked    Callback ao pressionar o FAB.
 */
@Composable
fun CreatePasswordScreen(
    password: String = "",
    confirmPassword: String = "",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onCreateAccountClicked: () -> Unit = {}
) {
    // ── Local state: controla visibilidade de cada campo de senha ──────────────
    // Esse estado é local à tela (não pertence ao ViewModel) pois é puramente
    // visual — não afeta a lógica de negócio.
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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

            // ── App name header ────────────────────────────────────────────────
            Text(
                text = "GYM TOP",
                color = GymTopNeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))

            // ── Title ──────────────────────────────────────────────────────────
            Text(
                text = "CRIAR",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "SENHA",
                color = GymTopNeonGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(20.dp))

            // ── Subtitle ───────────────────────────────────────────────────────
            Text(
                text = "Escolha uma senha forte com pelo menos 6 caracteres para proteger sua conta.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(40.dp))

            // ── Password field ─────────────────────────────────────────────────
            // PasswordVisualTransformation oculta os caracteres com "•" quando
            // passwordVisible = false.
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChanged,
                placeholder = {
                    Text(text = "Senha", color = Color.White.copy(alpha = 0.4f))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                trailingIcon = {
                    // Toggle de visibilidade da senha
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = passwordTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // ── Confirm Password field ─────────────────────────────────────────
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChanged,
                placeholder = {
                    Text(text = "Confirmar Senha", color = Color.White.copy(alpha = 0.4f))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = passwordTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Error message ──────────────────────────────────────────────────
            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }

            // ── Password strength hint ─────────────────────────────────────────
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Mínimo 6 caracteres.",
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 11.sp
            )
        }

        // ── FAB / Loading indicator ────────────────────────────────────────────
        // Enquanto isLoading = true, exibe um spinner no lugar do FAB para
        // evitar que o usuário envie o formulário mais de uma vez.
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .size(64.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = GymTopNeonGreen)
            } else {
                FloatingActionButton(
                    onClick = onCreateAccountClicked,
                    containerColor = GymTopNeonGreen,
                    contentColor = GymTopOnPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Criar conta",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ── TextField colors helper ────────────────────────────────────────────────────

@Composable
private fun passwordTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor        = Color.White,
    unfocusedTextColor      = Color.White,
    focusedBorderColor      = GymTopNeonGreen,
    unfocusedBorderColor    = Color.White.copy(alpha = 0.25f),
    cursorColor             = GymTopNeonGreen,
    focusedContainerColor   = GymTopSurface,
    unfocusedContainerColor = GymTopSurface
)

// ── Preview ────────────────────────────────────────────────────────────────────

@Preview(widthDp = 390, heightDp = 844, showBackground = true, backgroundColor = 0xFF0E0E0E)
@Composable
private fun CreatePasswordScreenPreview() {
    CreatePasswordScreen(password = "", confirmPassword = "")
}




