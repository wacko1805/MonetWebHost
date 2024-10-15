package com.wacko1805.monetwebadapter

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import fi.iki.elonen.NanoHTTPD

class HttpServer(private val context: Context) : NanoHTTPD(8192) {

    // Check if the device supports dynamic colors (API 31+)
    @RequiresApi(Build.VERSION_CODES.S)
    private fun getDynamicColors(isDarkTheme: Boolean): Map<String, Color> {
        val colorScheme = if (isDarkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }

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

    // Get dynamic colors and return them as a JSON response
    override fun serve(session: IHTTPSession): Response {
        val isDarkTheme = isDarkModeEnabled()
        val dynamicColors = getDynamicColors(isDarkTheme)

        // Convert dynamic colors to JSON
        val colorsJson = dynamicColors.entries.joinToString(
            separator = ", ",
            prefix = "\"colors\": {",
            postfix = "}"
        ) {
            val colorInt = it.value.toArgb()
            val colorHex = Integer.toHexString(colorInt).takeLast(6).padStart(6, '0').uppercase()
            "\"${it.key}\": \"#$colorHex\""
        }

        val json = """
            {
                "message": "MonetWebAdapter is running!",
                "status": "OK",
                $colorsJson
            }
        """.trimIndent()

        return newFixedLengthResponse(Response.Status.OK, "application/json", json).apply {
            addHeader("Access-Control-Allow-Origin", "*")
            addHeader("Access-Control-Allow-Methods", "GET, POST")
            addHeader("Access-Control-Allow-Headers", "Content-Type")
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        return context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}
