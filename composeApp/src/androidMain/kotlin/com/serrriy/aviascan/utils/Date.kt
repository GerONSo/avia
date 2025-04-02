package com.serrriy.aviascan.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation : VisualTransformation {
    private val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset +
                    (if (offset >= 2) 1 else 0) +
                    (if (offset >= 4) 1 else 0)
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset -
                    (if (offset >= 3) 1 else 0) -
                    (if (offset >= 6) 1 else 0)
        }

    }

    override fun filter(text: AnnotatedString): TransformedText {
        var output = ""
        text.forEachIndexed { index, char ->
            output += char
            when (index) {
                1, 3 -> output += '.'
            }
        }
        return TransformedText(AnnotatedString(output), numberOffsetTranslator)
    }

}