package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GoogleBlueDark,
    onPrimary = GoogleBlueOnDark,
    primaryContainer = GoogleBlueContainerDark,
    onPrimaryContainer = GoogleBlueOnContainerDark,
    secondary = GoogleGreenDark,
    onSecondary = GoogleGreenOnDark,
    secondaryContainer = GoogleGreenContainerDark,
    onSecondaryContainer = GoogleGreenOnContainerDark,
    background = GoogleSurfaceDark,
    surface = GoogleSurfaceDark,
    onBackground = GoogleOnSurfaceDark,
    onSurface = GoogleOnSurfaceDark,
    surfaceVariant = GoogleSurfaceVariantDark,
    onSurfaceVariant = GoogleOnSurfaceVariantDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GoogleBluePrimary,
    onPrimary = GoogleBlueOnPrimary,
    primaryContainer = GoogleBlueContainer,
    onPrimaryContainer = GoogleBlueOnContainer,
    secondary = GoogleGreenSecondary,
    onSecondary = GoogleGreenOnSecondary,
    secondaryContainer = GoogleGreenContainer,
    onSecondaryContainer = GoogleGreenOnContainer,
    background = GoogleSurfaceLight,
    surface = GoogleSurfaceLight,
    onBackground = GoogleOnSurfaceLight,
    onSurface = GoogleOnSurfaceLight,
    surfaceVariant = GoogleSurfaceVariantLight,
    onSurfaceVariant = GoogleOnSurfaceVariantLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
