package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Defina sua paleta de cores primária e secundária
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // Cor primária para o tema escuro
    secondary = Color(0xFF03DAC5), // Cor secundária para o tema escuro
    background = Color(0xFF121212) // Cor de fundo escura
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Cor primária para o tema claro
    secondary = Color(0xFF03DAC5), // Cor secundária para o tema claro
    background = Color(0xFFFFFFFF) // Cor de fundo clara
    /* Outras cores padrão do Material 3, se necessário.
    surface = ...,
    onPrimary = ...,
    ...
    */
)

@Composable
fun BancoOsorioTheme(
    // Por padrão, use o tema claro (isDarkTheme = false)
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        // Você pode definir a tipografia e as formas aqui se quiser customizar
        // typography = Typography,
        // shapes = Shapes,
        content = content
    )
}