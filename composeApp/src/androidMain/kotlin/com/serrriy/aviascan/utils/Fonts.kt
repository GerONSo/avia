package com.serrriy.aviascan.utils

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.serrriy.aviascan.R

object AppFont {
    val SanFrancisco = FontFamily(
        Font(R.font.sf_regular),
        Font(R.font.sf_regular_italic, style = FontStyle.Italic),
        Font(R.font.sf_medium, FontWeight.Medium),
        Font(R.font.sf_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
        Font(R.font.sf_bold, FontWeight.Bold),
        Font(R.font.sf_bold_italic, FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.sf_light, FontWeight.Light),
        Font(R.font.sf_light_italic, FontWeight.Light, style = FontStyle.Italic),
        Font(R.font.sf_semibold, FontWeight.SemiBold),
        Font(R.font.sf_semibold_italic, FontWeight.SemiBold, style = FontStyle.Italic),
        Font(R.font.sf_heavy, FontWeight.ExtraBold),
        Font(R.font.sf_heavy_italic, FontWeight.ExtraBold, style = FontStyle.Italic),
    )
}

private val defaultTypography = Typography()
val Typography: Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.SanFrancisco),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.SanFrancisco),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.SanFrancisco),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.SanFrancisco),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.SanFrancisco),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.SanFrancisco),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.SanFrancisco),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.SanFrancisco),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.SanFrancisco),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.SanFrancisco),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.SanFrancisco),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.SanFrancisco),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.SanFrancisco),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.SanFrancisco),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.SanFrancisco)
)