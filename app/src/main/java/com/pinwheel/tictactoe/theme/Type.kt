/**
 * This file is intended to be identical across all Pinwheel Android apps
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 *
 * If a modification is required, it is INCREDIBLY IMPORTANT to update the theme in ALL Pinwheel Android apps.
 */

package com.pinwheel.tictactoe.theme

import androidx.compose.material3.Typography
import com.pinwheel.tictactoe.theme.Wsp.wsp

val defaultTypography = Typography()
val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(
        fontSize = 57.wsp,
        lineHeight = 64.wsp,
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontSize = 45.wsp,
        lineHeight = 52.wsp,
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontSize = 36.wsp,
        lineHeight = 44.wsp,
    ),
    headlineLarge = defaultTypography.headlineLarge.copy(
        fontSize = 32.wsp,
        lineHeight = 40.wsp,
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontSize = 28.wsp,
        lineHeight = 36.wsp,
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontSize = 24.wsp,
        lineHeight = 32.wsp,
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontSize = 22.wsp,
        lineHeight = 28.wsp,
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontSize = 16.wsp,
        lineHeight = 24.wsp
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontSize = 14.wsp,
        lineHeight = 20.wsp
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontSize = 16.wsp,
        lineHeight = 24.wsp
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontSize = 14.wsp,
        lineHeight = 20.wsp
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontSize = 12.wsp,
        lineHeight = 16.wsp
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontSize = 14.wsp,
        lineHeight = 20.wsp
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontSize = 12.wsp,
        lineHeight = 16.wsp
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontSize = 11.wsp,
        lineHeight = 16.wsp
    )
)
