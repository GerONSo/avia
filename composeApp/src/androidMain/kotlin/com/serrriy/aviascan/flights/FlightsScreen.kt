package com.serrriy.aviascan.flights

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlightsScreen(
    navController: NavHostController,
    bottomSheetViewModel: BottomSheetViewModel = viewModel(),
) {
    val uiStateBottomSheet by bottomSheetViewModel.uiState.collectAsState()
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    val default = LatLng(37.062672,37.3817645)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(default, 1f)
    }
    val sheetState =
        rememberBottomSheetState(initialValue = SheetValue.PartiallyExpanded, defineValues = {
            SheetValue.Hidden at height(120.dp)
            SheetValue.PartiallyExpanded at offset(percent = 60)
            SheetValue.Expanded at contentHeight
        })
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(uiStateBottomSheet, bottomSheetViewModel, navController)
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) { innerPadding ->
        Box {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
            ) {
                uiStateBottomSheet.flightsList.map { flight ->
                    val coords = listOf(flight.departure.airport.latitude, flight.departure.airport.longitude, flight.arrival.airport.latitude, flight.arrival.airport.longitude)
                    if (coords.all { it != null }) {
                        Polyline(
                            points = listOf(
                                LatLng(
                                    flight.departure.airport.latitude!!,
                                    flight.departure.airport.longitude!!
                                ),
                                LatLng(
                                    flight.arrival.airport.latitude!!,
                                    flight.arrival.airport.longitude!!
                                )
                            ),
                            color = Color.Red,
                            width = 5f,
                            geodesic = true,
                        )

                        listOf(flight.departure, flight.arrival).map { target ->
                            MarkerComposable(
                                state = rememberMarkerState(position = LatLng(target.airport.latitude!!, target.airport.longitude!!)),
                                anchor = Offset(0.5f, 0.5f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(color = Color.Black, shape = CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
