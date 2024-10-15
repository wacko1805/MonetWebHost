package com.wacko1805.monetwebadapter

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color

class DynamicMaterialColors(private val context: Context) {

    // Check if the device supports dynamic colors (API 31+)
    @RequiresApi(Build.VERSION_CODES.S)
    fun getDynamicColors(isDarkTheme: Boolean): Map<String, Color> {
        // Get the dynamic color scheme based on light or dark theme
        val colorScheme = if (isDarkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }

        // Map dynamic colors to a Map<String, Int>
        return mapOf(
            "primary" to colorScheme.primary,
            "onPrimary" to colorScheme.onPrimary,
            "primaryContainer" to colorScheme.primaryContainer,
            "onPrimaryContainer" to colorScheme.onPrimaryContainer,
            "secondary" to colorScheme.secondary,
            "onSecondary" to colorScheme.onSecondary,
            "secondaryContainer" to colorScheme.secondaryContainer,
            "onSecondaryContainer" to colorScheme.onSecondaryContainer,
            "tertiary" to colorScheme.tertiary,
            "onTertiary" to colorScheme.onTertiary,
            "error" to colorScheme.error,
            "onError" to colorScheme.onError,
            "background" to colorScheme.background,
            "onBackground" to colorScheme.onBackground,
            "surface" to colorScheme.surface,
            "onSurface" to colorScheme.onSurface,
            "surfaceVariant" to colorScheme.surfaceVariant,
            "onSurfaceVariant" to colorScheme.onSurfaceVariant,
            "outline" to colorScheme.outline,
            "inverseOnSurface" to colorScheme.inverseOnSurface,
            "inverseSurface" to colorScheme.inverseSurface,
            "surfaceTint" to colorScheme.surfaceTint
        )
    }
}
