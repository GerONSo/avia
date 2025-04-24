package com.serrriy.aviascan.add_flight

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.serrriy.aviascan.R

@Composable
fun FlightOptionItem(
    supposedFlight: SupposedFlight
) {
    Row(
        modifier = Modifier
    ) {
        Text(
            text = supposedFlight.flightNumber,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(Modifier.weight(1f))

        Text(supposedFlight.departureCode)
        Spacer(Modifier.width(6.dp))
        Icon(
            painter = painterResource(R.drawable.ic_flight),
            contentDescription = "flight_takeoff",
            modifier = Modifier.size(24.dp).rotate(90f)
        )
        Spacer(Modifier.width(6.dp))
        Text(supposedFlight.arrivalCode)

        Spacer(Modifier.weight(1f))

        Text(
            text = supposedFlight.departureTime,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}