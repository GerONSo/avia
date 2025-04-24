package com.serrriy.aviascan.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.serrriy.aviascan.R

@Composable
fun LoadingScreen() {
    Column(Modifier.fillMaxWidth().fillMaxHeight()) {
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.aviascan),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(Modifier.weight(1f))
    }
}