package com.serrriy.aviascan.flights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.serrriy.aviascan.R
import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.flights.data.FlightListItem
import com.serrriy.aviascan.main.MainScreen
import com.serrriy.aviascan.utils.TimeUtils
import com.serrriy.aviascan.utils.toDateTime
import kotlinx.datetime.TimeZone
import java.time.Duration
import java.time.OffsetDateTime

val timeZone = TimeZone.of("Europe/Moscow")

@Composable
fun BottomSheetContent(
    uiState: BottomSheetState,
    bottomSheetViewModel: BottomSheetViewModel,
    navController: NavHostController
) {
    Column(
        Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "My flights",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.ButtonColor)
                ),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.6f).height(50.dp)
                    .padding(end = 16.dp),
                onClick = {
                    navController.navigate(MainScreen.AddFlight.name)
                }
            ) {
                Text("Add Flight")
            }
        }
        LazyColumn(
            Modifier.fillMaxWidth().fillMaxHeight().padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            itemsIndexed(uiState.flightsList) { index, flightItem ->
                BottomSheetFlightListItem(
                    item = flightItem,
                    lastItem = index == uiState.flightsList.lastIndex,
                    viewModel = bottomSheetViewModel,
                )
            }
        }
    }
}

@Composable
fun BottomSheetFlightListItem(
    item: FlightListItem,
    lastItem: Boolean = true,
    expandable: Boolean = true,
    viewModel: BottomSheetViewModel,
) {
    Column(
        Modifier.fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                viewModel.onFlightClick(item)
            }
    ) {
        MainItemInfo(item, item.expanded, expandable)
        if (item.expanded) {
            Spacer(Modifier.height(16.dp))
            FullItemInfo(item)
        }
        if (!lastItem) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color(0xFFB0BEC5)
            )
        }
    }
}

@Composable
fun MainItemInfo(item: FlightListItem, expanded: Boolean, expandable: Boolean) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (expand, direction, datetime) = this@ConstraintLayout.createRefs()
        Column(
            Modifier.constrainAs(datetime) {
                start.linkTo(parent.start)
            }
        ) {
            Text(item.departure.datetime.toDateTime().toSimpleDate())
        }
        Row(
            modifier = Modifier.constrainAs(direction) {
                end.linkTo(expand.start)
                start.linkTo(datetime.end)
            }
        ) {
            Text(item.departure.airport.code)
            Spacer(Modifier.width(6.dp))
            Icon(
                painter = painterResource(R.drawable.ic_flight),
                contentDescription = "flight_takeoff",
                modifier = Modifier.size(24.dp).rotate(90f)
            )
            Spacer(Modifier.width(6.dp))
            Text(item.arrival.airport.code)
        }
        Column(
            Modifier.constrainAs(expand) {
                end.linkTo(parent.end)
            }
        ) {
            if (expandable) {
                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
fun FullItemInfo(item: FlightListItem) {
    Text(getAirportName(item.departure.airport), fontWeight = FontWeight.Bold)
    Text(item.departure.datetime.toDateTime().toLocalTime().toString(), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

    Row {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = "Clock",
            modifier = Modifier.size(24.dp).align(Alignment.CenterVertically).padding(end = 4.dp),
            tint = colorResource(R.color.Secondary)
        )
        Text(
            countFlightTime(item),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(R.color.Secondary)
        )
        HorizontalDivider(
            color = colorResource(R.color.Secondary),
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp, end = 8.dp)
        )
    }
    Text(getAirportName(item.arrival.airport), fontWeight = FontWeight.Bold)
    Text(item.arrival.datetime.toDateTime().toLocalTime().toString(), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
}

fun OffsetDateTime.toSimpleDate(): String {
    return "$dayOfMonth ${TimeUtils.getMonth(monthValue)} $year"
}

fun getAirportName(airport: AirportDto): String {
    return "${airport.code} • ${airport.name}"
}

fun countFlightTime(item: FlightListItem): String {
    val arrival = item.arrival.datetime.toDateTime()
    val departure = item.departure.datetime.toDateTime()
    val length = Duration.between(departure, arrival)
    return "${length.toHours()}h ${length.toMinutes() % 60}m"
}