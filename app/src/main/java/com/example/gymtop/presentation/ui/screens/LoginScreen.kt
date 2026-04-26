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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
 * LoginScreen — tela de autenticação para usuários já cadastrados.
 *
 * Segue o mesmo estilo visual das demais telas do fluxo de autenticação:
 *  - Cabeçalho "GYM TOP"
 *  - Título "BEM-VINDO" (branco) + "DE VOLTA" (neon)
 *  - Campo de e-mail com ícone
 *  - Campo de senha com toggle de visibilidade
 *  - Mensagem de erro inline quando credenciais são inválidas
 *  - Botão primário "ENTRAR" para submeter o formulário
 *  - Indicador de loading enquanto a chamada ao Firebase está em andamento
 *
 * @param email                  Valor atual do campo de e-mail.
 * @param password               Valor atual do campo de senha.
 * @param isLoading              true durante a autenticação (exibe spinner no lugar do botão).
 * @param errorMessage           Mensagem de erro; null = sem erro.
 * @param onEmailChanged         Callback ao digitar no campo de e-mail.
 * @param onPasswordChanged      Callback ao digitar no campo de senha.
 * @param onLoginClicked         Callback ao pressionar o botão "ENTRAR".
 */
@Composable
fun LoginScreen(
    email: String = "",
    password: String = "",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {}
) {
    // ── Local state: controla visibilidade da senha ────────────────────────────
    // Puramente visual — não pertence ao ViewModel.
    var passwordVisible by remember { mutableStateOf(false) }

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
                text = "BEM-VINDO",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "DE VOLTA",
                color = GymTopNeonGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(20.dp))

            // ── Subtitle ───────────────────────────────────────────────────────
            Text(
                text = "Faça login para continuar seus treinos.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(40.dp))

            // ── Email field ────────────────────────────────────────────────────
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChanged,
                placeholder = {
                    Text(text = "E-mail", color = Color.White.copy(alpha = 0.4f))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = loginTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

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
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                                          else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar senha"
                                                 else "Mostrar senha",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                                       else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = loginTextFieldColors(),
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

            Spacer(Modifier.height(32.dp))

            // ── Primary CTA button ─────────────────────────────────────────────
            // Enquanto isLoading = true, exibe um spinner centralizado no lugar
            // do botão para evitar que o usuário envie o formulário mais de uma vez.
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = GymTopNeonGreen)
                }
            } else {
                Button(
                    onClick = onLoginClicked,
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GymTopNeonGreen,
                        contentColor   = GymTopOnPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        text          = "ENTRAR",
                        fontSize      = 14.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 2.8.sp
                    )
                }
            }
        }
    }
}

// ── TextField colors helper ────────────────────────────────────────────────────

@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
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
private fun LoginScreenPreview() {
    LoginScreen(email = "", password = "")
}
