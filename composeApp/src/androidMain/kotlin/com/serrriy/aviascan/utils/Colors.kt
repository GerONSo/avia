package com.serrriy.aviascan.utils

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun OutlinedTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Black,
        unfocusedIndicatorColor = Color.Gray,
        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color.Gray,
        focusedPlaceholderColor = Color.White,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        selectionColors = TextSelectionColors(
            handleColor = Color.Black,
            backgroundColor = Color.White
        ),
    )
}