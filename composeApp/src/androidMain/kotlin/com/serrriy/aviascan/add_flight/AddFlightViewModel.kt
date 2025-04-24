package com.serrriy.aviascan.add_flight

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.serrriy.aviascan.data.airports.AirportDto
import com.serrriy.aviascan.data.aviation_stack.AviationStackAirportDataDto
import com.serrriy.aviascan.data.aviation_stack.AviationStackGetFlightInfoRequest
import com.serrriy.aviascan.data.flights.CreateFlightRequest
import com.serrriy.aviascan.data.flights.FlightTargetDto
import com.serrriy.aviascan.iata.Parser
import com.serrriy.aviascan.interactors.AviationStackInteractor
import com.serrriy.aviascan.interactors.FlightsInteractor
import com.serrriy.aviascan.interactors.GoogleMapsInteractor
import com.serrriy.aviascan.repositories.DataStoreKeys
import com.serrriy.aviascan.repositories.DataStoreRepository
import com.serrriy.aviascan.shared.BuildConfig
import com.serrriy.aviascan.utils.TokenProviderImpl
import com.serrriy.aviascan.utils.toDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddFlightViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val navController: NavHostController,
) : ViewModel() {
    private val aviationStackInteractor = AviationStackInteractor()
    private val googleMapsInteractor = GoogleMapsInteractor()
    private val flightsInteractor by lazy {
        FlightsInteractor(
            tokenProvider = TokenProviderImpl(dataStoreRepository = dataStoreRepository)
        )
    }

    private val _uiState = MutableStateFlow(AddFlightState())
    val uiState: StateFlow<AddFlightState> = _uiState.asStateFlow()

    private val formattedFlightDate: String
        get() = uiState.value.flightDate.drop(4) + "-" +
                uiState.value.flightDate.drop(2).dropLast(4) + "-" +
                uiState.value.flightDate.dropLast(6)


    init {
        getAllAirports()
    }

    private fun getAllAirports() {
        viewModelScope.launch {
            flightsInteractor.getAirports().onSuccess { airportsResponse ->
                _uiState.update { currentState ->
                    currentState.copy(airports = airportsResponse.airports)
                }
            }.onFailure {

            }
        }
    }

    fun flightNumberChanged(flightNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(flightNumber = flightNumber)
        }
        getFlightInfo()
    }

    fun departureAirportChanged(departureAirport: String) {
        _uiState.update { currentState ->
            currentState.copy(
                departure = currentState.departure.copy(
                    iata = departureAirport
                ),
            )
        }
        getFlightInfo()
    }

    fun arrivalAirportChanged(arrivalAirport: String) {
        _uiState.update { currentState ->
            currentState.copy(
                arrival = currentState.arrival.copy(
                    iata = arrivalAirport
                ),
            )
        }
        getFlightInfo()
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
                departure = currentState.departure.copy(
                    airport = code.firstFlightSegment.fromCity
                ),
                arrival = currentState.arrival.copy(
                    airport = code.firstFlightSegment.toCity
                ),
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
        viewModelScope.launch {
            val departure = uiState.value.departure
            val arrival = uiState.value.arrival

            val airport1 =
                uiState.value.airports.firstOrNull { it.code == departure.iata } ?: return@launch
            val airport2 =
                uiState.value.airports.firstOrNull { it.code == arrival.iata } ?: return@launch
            createSnapshot(airport1, airport2)
        }
    }

    private fun createFlight(snapshot: ByteArray) {
        viewModelScope.launch {
            val departure = uiState.value.departure
            val arrival = uiState.value.arrival

            dataStoreRepository.readFromDataStore(DataStoreKeys.userId)?.let { userId ->
                flightsInteractor.addFlight(
                    request = CreateFlightRequest(
                        userId = userId,
                        flightCode = uiState.value.flightNumber,
                        departure = FlightTargetDto(
                            flightTime = departure.scheduled.split("+").first(),
                            timeZone = departure.timezone,
                            airport = departure.iata,
                        ),
                        arrival = FlightTargetDto(
                            flightTime = arrival.scheduled.split("+").first(),
                            timeZone = arrival.timezone,
                            airport = arrival.iata,
                        )
                    ),
                    snapshot = snapshot,
                ).onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            flightNumber = "",
                            departure = AviationStackAirportDataDto("", "", "", ""),
                            arrival = AviationStackAirportDataDto("", "", "", ""),
                            flightDate = "",
                            supposedFlights = listOf(),
                        )
                    }
                    navController.popBackStack()
                }.onFailure {

                }
            }
        }
    }

    private fun createSnapshot(airport1: AirportDto, airport2: AirportDto) {
        viewModelScope.launch {
            val path =
                "color:0x0000ff|weight:5|${airport1.latitude},${airport1.longitude}|${airport2.latitude},${airport2.longitude}"

            googleMapsInteractor.getSnapshot(
                size = "1000x500",
                mapType = "roadmap",
                path = path,
                apiKey = BuildConfig.MAPKIT_API_KEY,
            ).onSuccess { imageBytes ->
                createFlight(imageBytes)
            }.onFailure {
                Log.d("testapi", it.toString())
            }
        }
    }

    fun updateAllWith(supposedFlight: SupposedFlight) {
        val zonedDateTime = ZonedDateTime.parse(supposedFlight.unparsed.departure.scheduled)
        val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
        val flightDate = zonedDateTime.format(formatter)
        _uiState.update { currentState ->
            currentState.copy(
                flightNumber = supposedFlight.flightNumber,
                departure = supposedFlight.unparsed.departure,
                arrival = supposedFlight.unparsed.arrival,
                flightDate = flightDate,
            )
        }
    }

    private fun getFlightInfo() {
        val flightNumber = uiState.value.flightNumber

        viewModelScope.launch(Dispatchers.IO) {
//            if (flightNumber.isNotEmpty()) {
            aviationStackInteractor.getFlightInfo(
                AviationStackGetFlightInfoRequest(
                    BuildConfig.AVIATIONSTACK_API_KEY,
                    flightDate = if (uiState.value.flightDate.length == 8 && uiState.value.flightDate.all { it.isDigit() }) {
                        formattedFlightDate
                    } else {
                        null
                    },
                    flightNumber = flightNumber.ifEmpty { null },
                    departureCode = uiState.value.departure.iata.ifEmpty { null },
                    arrivalCode = uiState.value.arrival.iata.ifEmpty { null },
                )
            ).onSuccess { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        supposedFlights = result.data?.map { supposedFlight ->
                            SupposedFlight(
                                flightNumber = supposedFlight.flight.iata,
                                departureCode = supposedFlight.departure.iata,
                                arrivalCode = supposedFlight.arrival.iata,
                                departureTime = supposedFlight.departure.scheduled.toDateTime()
                                    .toLocalTime().toString(),
                                unparsed = supposedFlight
                            )
                        } ?: listOf()
                    )
                }
//                    if (!result.data.isNullOrEmpty()) {
//                        val data = result.data!!.first()
//                        _uiState.update { currentState ->
//                            currentState.copy(
//                                departure = data.departure,
//                                arrival = data.arrival,
//                                flightTime = countInAirTime(data)
//                            )
//                        }
//                    }
            }.onFailure {

            }
//            }
        }
    }
}

data class AddFlightState(
    val flightNumber: String = "",
    val departure: AviationStackAirportDataDto = AviationStackAirportDataDto("", "", "", ""),
    val arrival: AviationStackAirportDataDto = AviationStackAirportDataDto("", "", "", ""),
    val flightDate: String = "",
    val showTextFields: Boolean = false,
    val supposedFlights: List<SupposedFlight> = listOf(),
    val airports: List<AirportDto> = listOf(),
)