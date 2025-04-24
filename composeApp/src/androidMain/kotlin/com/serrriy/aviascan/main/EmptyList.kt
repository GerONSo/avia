package com.serrriy.aviascan.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TempleBuddhist
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serrriy.aviascan.R

@Composable
fun EmptyList() {
    Column(Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Default.TempleBuddhist,
            contentDescription = "Empty Icon",
            tint = colorResource(R.color.Secondary),
            modifier = Modifier
                .padding(top = 26.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Nothing here yet",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorResource(R.color.Secondary),
            modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
        )
    }
}