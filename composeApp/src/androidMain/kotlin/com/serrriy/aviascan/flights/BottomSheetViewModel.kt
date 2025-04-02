package com.serrriy.aviascan.flights

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.data.flights.FlightListItemDto
import com.serrriy.aviascan.interactors.FlightsInteractor
import com.serrriy.aviascan.flights.data.FlightListItem
import com.serrriy.aviascan.flights.data.toFlightListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import kotlin.coroutines.coroutineContext

class BottomSheetViewModel : ViewModel() {
    private val flightsInteractor = FlightsInteractor()

    private val _uiState = MutableStateFlow(BottomSheetState())
    val uiState: StateFlow<BottomSheetState> = _uiState.asStateFlow()

    init {
        getAllFlights()
    }

    fun getAllFlights() {
        _uiState.update { currentState ->
            currentState.copy(
                flightsList = flightsInteractor.getAllFlights().map(FlightListItemDto::toFlightListItem)
            )
        }
    }

    fun onFlightClick(item: FlightListItem) {
        val index = _uiState.value.flightsList.indexOf(item)
        val mutableFlightsList = _uiState.value.flightsList.toMutableList()
        mutableFlightsList[index] = mutableFlightsList[index].copy(
            expanded = !mutableFlightsList[index].expanded
        )
        _uiState.update { currentState ->
            currentState.copy(
                flightsList = mutableFlightsList
            )
        }
    }
}

data class BottomSheetState(
    val flightsList: List<FlightListItem> = emptyList()
)