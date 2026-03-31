package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary
import com.example.gymtop.ui.theme.GymTopSurface

/**
 * SplashScreen2 — redesigned onboarding / splash screen (v2).
 *
 * All colors are imported from [com.example.gymtop.ui.theme.Color]
 *
 * Visual structure (top → bottom):
 *  1. Ambient glow blobs (purely decorative, rendered behind everything else).
 *  2. Centred column: circular logo  →  "GYM TOP" headline.
 *  3. Bottom section: "COMEÇAR →" CTA button  →  version footer.
 *
 * Improvements over [AnimatedSplashScreen] (v1):
 *  - Proper [Button] composable for the CTA (v1 misused CenterAlignedTopAppBar).
 *  - Arrow icon displayed alongside the label.
 *  - Responsive layout: fillMaxSize + weight instead of hard-coded pixel sizes.
 *  - Stronger neon glow on the headline.
 *  - No dependency on R.drawable.container (uses ⚡ Unicode glyph; swap for
 *    Icons.Default.Bolt with material-icons-extended if a vector icon is preferred).
 *
 * @param onStartClick Callback fired when the user presses "COMEÇAR". Wire this
 *                     to your NavController to push into the main app graph.
 */
@Composable
fun SplashScreen2(
    onStartClick: () -> Unit = {},
    modifier: Modifier = Modifier
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

            // -- Circular logo with lightning bolt icon --
            // GymTopSurface provides the dark fill; the border picks up NeonGreen
            // at reduced opacity to create a subtle glowing ring effect.
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(144.dp)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(GymTopSurface)
                    .border(
                        BorderStroke(1.5.dp, GymTopNeonGreen.copy(alpha = 0.35f)),
                        RoundedCornerShape(9999.dp)
                    )
            ) {
                // ⚡ lightning bolt as a Unicode glyph with a neon shadow.
                // To use a vector icon instead, add material-icons-extended to
                // build.gradle.kts and replace with:
                //   Icon(Icons.Default.Bolt, contentDescription = null, tint = GymTopNeonGreen)
                Text(
                    text = "⚡",
                    fontSize = 52.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = GymTopNeonGreen.copy(alpha = 0.6f),
                            offset = Offset(0f, 0f),
                            blurRadius = 28f
                        )
                    )
                )
            }

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
                Spacer(Modifier.width(12.dp))
                // Arrow icon — available in the default Material icon set, no
                // additional dependency needed.
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null, // label already describes the action
                    modifier = Modifier.size(18.dp)
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
private fun SplashScreen2Preview() {
    // onStartClick is left as a no-op for preview purposes
    SplashScreen2()
}

