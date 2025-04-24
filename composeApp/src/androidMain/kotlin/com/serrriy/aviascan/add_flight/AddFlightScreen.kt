package com.serrriy.aviascan.add_flight

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.serrriy.aviascan.R
import com.serrriy.aviascan.main.MainScreen
import com.serrriy.aviascan.utils.DateTransformation
import com.serrriy.aviascan.utils.OutlinedTextFieldColors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddFlightScreen(
    navController: NavHostController,
    viewModel: AddFlightViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val buttonWidth by animateFloatAsState(
        targetValue = if (uiState.showTextFields) 1f else 0.4f,
        animationSpec = tween(durationMillis = 300)
    )
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Go Back",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        if (uiState.showTextFields) {
                            viewModel.changeShowTextFields(false)
                        } else {
                            navController.popBackStack()
                        }
                    }
            )
            if (uiState.showTextFields) {
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.aviascan),
                    contentDescription = "logo",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(Modifier.weight(1f))
            }
        }
        if (!uiState.showTextFields) {
            Image(
                painter = painterResource(id = R.drawable.aviascan),
                contentDescription = "logo",
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Text(
            text = "Add your flight. Past or coming.",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Simply scan barcode from boarding pass to fill everything for you",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (uiState.showTextFields) {
            NoBoardingPassContent(uiState, viewModel)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 16.dp, bottom = 26.dp)) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.ButtonColor)
                ),
                modifier = Modifier
                    .fillMaxWidth(buttonWidth).height(50.dp),
                onClick = {
                    if (!uiState.showTextFields) {
                        if (cameraPermissionState.status.isGranted) {
                            navController.navigate(MainScreen.Camera.name)
                        } else {
                            cameraPermissionState.run { launchPermissionRequest() }
                        }
                    } else {
                        viewModel.addFlight()
                    }
                }
            ) {
                if (!uiState.showTextFields) {
                    Text("Scan")
                } else {
                    Text("Add flight")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!uiState.showTextFields) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.height(50.dp),
                    onClick = {
                        viewModel.changeShowTextFields(true)
                    }
                ) {
                    Text(
                        text = "No boarding pass",
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun NoBoardingPassContent(
    uiState: AddFlightState,
    viewModel: AddFlightViewModel,
) {
    Column {
        OutlinedTextField(
            value = uiState.flightNumber,
            onValueChange = { viewModel.flightNumberChanged(it) },
            label = { Text("Flight number") },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors()
        )
        Row(modifier = Modifier.padding(top = 26.dp)) {
            OutlinedTextField(
                value = uiState.departure.iata,
                onValueChange = { viewModel.departureAirportChanged(it) },
                label = { Text("From") },
                modifier = Modifier.padding(top = 16.dp, end = 26.dp).fillMaxWidth(0.5f),
                colors = OutlinedTextFieldColors()
            )
            OutlinedTextField(
                value = uiState.arrival.iata,
                onValueChange = { viewModel.arrivalAirportChanged(it) },
                label = { Text("To") },
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                colors = OutlinedTextFieldColors()
            )
        }
        OutlinedTextField(
            value = uiState.flightDate,
            onValueChange = { newText ->
                if (newText.length <= 8) {
                    viewModel.flightDateChanged(newText)
                }
            },
            label = { Text("Date of flight") },
            modifier = Modifier.padding(top = 16.dp, bottom = 26.dp).fillMaxWidth(),
            colors = OutlinedTextFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            visualTransformation = DateTransformation(),
        )

        LazyColumn(Modifier.fillMaxWidth()) {
            itemsIndexed(uiState.supposedFlights) { index, supposedFlight ->
                Column(Modifier.clickable {
                    viewModel.updateAllWith(supposedFlight)
                }) {
                    FlightOptionItem(supposedFlight)
                    if (index < uiState.supposedFlights.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            } //            item {
//                AsyncImage(
//                    model = uiState.snapshot,
//                    contentDescription = "Flight Path"
//                )
//            }
        }
    }
}