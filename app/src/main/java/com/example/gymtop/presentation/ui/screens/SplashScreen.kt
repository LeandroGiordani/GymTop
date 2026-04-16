package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.gymtop.R
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary

/**
 * Splash / landing screen shown when the app first launches.
 *
 * All colors are imported from the theme.
 *
 * Layout strategy (Compose equivalent of ConstraintLayout guidelines):
 *  • A single root [Column] fills the screen.
 *  • Two equal [Spacer]s with `weight(1f)` sandwich the logo + headline block,
 *    pinning it at vertical centre — just like a 50 % horizontal guideline.
 *  • Buttons and the footer live below the second spacer, always at the bottom.
 *
 * @param modifier     Standard Compose modifier forwarded to the root layout.
 * @param onStartClick Callback for the primary "COMEÇAR" button.
 * @param onEnterClick Callback for the secondary "ENTRAR" button.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {},
    onEnterClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(GymTopBackground)
            .padding(horizontal = 32.dp)
            // Adds bottom inset padding so the footer clears the system nav bar
            // when the activity runs edge-to-edge.
            .navigationBarsPadding()
    ) {

        // ── Top weight spacer ─────────────────────────────────────────────────
        // Pushes the logo+headline group downward until the matching bottom
        // spacer balances it — net effect: perfect vertical centering.
        Spacer(Modifier.weight(1f))

        // ── Logo ──────────────────────────────────────────────────────────────
        Image(
            painter = painterResource(id = R.drawable.ic_gym_top_logo),
            contentDescription = "Gym Top Logo",
            modifier = Modifier.size(144.dp)
        )

        Spacer(Modifier.height(40.dp))

        // ── "GYM TOP" headline ────────────────────────────────────────────────
        // Black weight + tight letter-spacing + glow shadow for the neon-sign look.
        Text(
            text = "GYM\nTOP",
            color = GymTopNeonGreen,
            textAlign = TextAlign.Center,
            lineHeight = 1.em,
            style = TextStyle(
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-4).sp,
                shadow = Shadow(
                    color = GymTopNeonGreen.copy(alpha = 0.55f),
                    offset = Offset(0f, 0f),
                    blurRadius = 36f
                )
            ),
            modifier = Modifier.shadow(elevation = 20.dp)
        )

        // ── Bottom weight spacer ──────────────────────────────────────────────
        // Equal weight to the top spacer → centres the logo+headline block.
        // Shrinks to zero if the screen is too small, pushing buttons down.
        Spacer(Modifier.weight(1f))

        // ── "COMEÇAR" primary CTA button ──────────────────────────────────────
        // Solid primary fill; onPrimary text ensures accessible contrast.
        Button(
            onClick = onStartClick,
            shape = RoundedCornerShape(9999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GymTopNeonGreen,
                contentColor = GymTopOnPrimary
            ),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(
                text = "COMEÇAR",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.8.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── "ENTRAR" secondary CTA button ─────────────────────────────────────
        // Transparent fill with a 1 dp primary-coloured border.
        Button(
            onClick = onEnterClick,
            shape = RoundedCornerShape(9999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GymTopNeonGreen
            ),
            border = BorderStroke(1.dp, GymTopNeonGreen),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "ENTRAR",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.8.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Version footer ────────────────────────────────────────────────────
        // Very low-contrast — purely informational, never interactive.
        Text(
            text = "VERSION 1.0.0 // GYM TOP ENGINE ACTIVE",
            color = GymTopNeonGreen.copy(alpha = 0.2f),
            fontSize = 8.sp,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center,
            lineHeight = 1.5.em
        )

        // Fixed gap between footer and the system nav bar inset.
        Spacer(Modifier.height(40.dp))
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
