package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.gymtop.R
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary

/**
 *
 * All colors are imported from the theme.
 *
 * Visual structure (top → bottom):
 *  1. Centred column: circular logo  →  "GYM TOP" headline.
 *  2. Bottom section: "COMEÇAR →" and "ENTRAR" buttons  →  version footer.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onStartClick Callback fired when the user presses "COMEÇAR". Wire this
 *                     to your NavController to push into the main app graph.
 * @param onEnterClick Callback fired when the user presses "ENTRAR". Wire this
 *                     to your NavController to push into the main app graph.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {},
    onEnterClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GymTopBackground)
    ) {
        // ── Centre content ────────────────────────────────────────────────────
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {

            // -- Logo --
            Image(
                painter = painterResource(id = R.drawable.ic_gym_top_logo),
                contentDescription = "Gym Top Logo",
                modifier = Modifier.size(144.dp)
            )

            Spacer(Modifier.height(40.dp))

            // -- "GYM TOP" headline --
            // Black weight + tight letter-spacing + glow shadow replicates the
            // neon sign aesthetic from the design.
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
        }

        // ── Bottom section: CTA button + version string ───────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 40.dp)
        ) {

            // "COMEÇAR →" call-to-action button
            // Solid NeonGreen fill with GymTopOnPrimary (dark olive) label text,
            // matching the colour ratio from the design.
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

            // "ENTRAR" secondary call-to-action button
            Button(
                onClick = onEnterClick,
                shape = RoundedCornerShape(9999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
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

            // Version footer — very low-contrast, purely informational
            Text(
                text = "VERSION 1.0.0 // GYM TOP ENGINE ACTIVE",
                color = GymTopNeonGreen.copy(alpha = 0.2f),
                fontSize = 8.sp,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                lineHeight = 1.5.em
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    // onStartClick and onEnterClick are left as no-ops for preview purposes
    SplashScreen()
}

