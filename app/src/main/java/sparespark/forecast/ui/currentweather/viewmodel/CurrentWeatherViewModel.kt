package sparespark.forecast.ui.currentweather.viewmodel

import androidx.lifecycle.ViewModel
import sparespark.forecast.core.internal.lazyDeferred
import sparespark.forecast.data.provider.user.UserPreferenceProvider
import sparespark.forecast.data.repository.forecast.ForecastRepository

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val userPreferenceProvider: UserPreferenceProvider
) : ViewModel() {

    val currentWeather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
    val currentUserName by lazyDeferred {
        userPreferenceProvider.getSignedUserName()
    }
}
