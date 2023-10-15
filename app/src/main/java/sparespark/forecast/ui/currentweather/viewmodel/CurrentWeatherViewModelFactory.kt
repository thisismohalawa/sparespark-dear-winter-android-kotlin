package sparespark.forecast.ui.currentweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.forecast.data.provider.user.UserPreferenceProvider
import sparespark.forecast.data.repository.forecast.ForecastRepository

class CurrentWeatherViewModelFactory(
    private val weatherRepository: ForecastRepository,
    private val userPreferenceProvider: UserPreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, userPreferenceProvider) as T
    }
}
