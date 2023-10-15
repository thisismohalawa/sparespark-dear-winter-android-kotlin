package sparespark.forecast.data.network.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sparespark.forecast.data.network.response.CurrentWeatherResponse
import sparespark.forecast.data.network.response.WeatherStackService

class WeatherNetworkDataSourceImpl(
    private val weatherStackService: WeatherStackService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            val fetchedCurrentWeather = weatherStackService
                .getCurrentWeatherAsync(location)
                .await()

            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
