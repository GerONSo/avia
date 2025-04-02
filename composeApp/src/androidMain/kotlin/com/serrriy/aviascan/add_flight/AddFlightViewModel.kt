package com.serrriy.aviascan.add_flight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoRequest
import com.serrriy.aviascan.iata.Parser
import com.serrriy.aviascan.interactors.AviationStackInteractor
import com.serrriy.aviascan.utils.onError
import com.serrriy.aviascan.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AddFlightViewModel: ViewModel() {
    private val aviationStackInteractor = AviationStackInteractor()

    private val _uiState = MutableStateFlow(AddFlightState())
    val uiState: StateFlow<AddFlightState> = _uiState.asStateFlow()

    private val formattedFlightDate: String
        get() = uiState.value.flightDate.drop(4) + "-" +
                uiState.value.flightDate.drop(2).dropLast(4) + "-" +
                uiState.value.flightDate.dropLast(6)

    fun flightNumberChanged(flightNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(flightNumber = flightNumber)
        }
        getFlightInfo()
    }

    fun departureAirportChanged(departureAirport: String) {
        _uiState.update { currentState ->
            currentState.copy(departureAirport = departureAirport)
        }
    }

    fun arrivalAirportChanged(arrivalAirport: String) {
        _uiState.update { currentState ->
            currentState.copy(arrivalAirport = arrivalAirport)
        }
    }

    fun flightDateChanged(flightDate: String) {
        _uiState.update { currentState ->
            currentState.copy(flightDate = flightDate)
        }
        getFlightInfo()
    }

    fun boardingPassScanned(data: String) {
        val code = Parser().parse(data)
        val sdf = SimpleDateFormat("ddMMyyyy")
        _uiState.update { currentState ->
            currentState.copy(
                flightNumber = code.firstFlightSegment.operatingCarrierDesignator + code.firstFlightSegment.flightNumber.dropWhile { it == '0' },
                departureAirport = code.firstFlightSegment.fromCity,
                arrivalAirport = code.firstFlightSegment.toCity,
                flightDate = sdf.format(code.firstFlightSegment.dateOfFlight.time),
                showTextFields = true
            )
        }
        getFlightInfo()
    }

    fun changeShowTextFields(showTextFields: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showTextFields = showTextFields)
        }
    }

    fun addFlight() {

    }

    private fun getFlightInfo() {
        val flightNumber = uiState.value.flightNumber

        viewModelScope.launch(Dispatchers.IO) {
            if (flightNumber.isNotEmpty()) {
                aviationStackInteractor.getFlightInfo(
                    AviationStackGetFlightInfoRequest(
                        "817fd96cb2cee1da402b26a5c6d45fb2",
                        flightDate = formattedFlightDate,
                        flightNumber = flightNumber
                    )
                ).onSuccess { result ->
                    if (!result.data.isNullOrEmpty()) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                departureAirport = result.data!!.first().departure.iata,
                                arrivalAirport = result.data!!.first().arrival.iata,
                            )
                        }
                    }
                }.onError {

                }
            }
        }
    }
}

data class AddFlightState(
    val flightNumber: String = "",
    val departureAirport: String = "",
    val arrivalAirport: String = "",
    val flightDate: String = "",
    val showTextFields: Boolean = false,
)