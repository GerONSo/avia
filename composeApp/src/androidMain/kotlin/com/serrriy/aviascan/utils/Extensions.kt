package com.serrriy.aviascan.utils

import android.content.Context
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.LatLng
import kotlinx.datetime.LocalDateTime
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun String.toDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}

