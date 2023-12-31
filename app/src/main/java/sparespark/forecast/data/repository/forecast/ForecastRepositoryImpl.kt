package sparespark.forecast.data.repository.forecast

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import sparespark.forecast.data.db.dao.CurrentWeatherDao
import sparespark.forecast.data.db.dao.WeatherLocationDao
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.db.entity.WeatherLocationEntry
import sparespark.forecast.data.network.datasource.WeatherNetworkDataSource
import sparespark.forecast.data.network.response.CurrentWeatherResponse
import sparespark.forecast.data.provider.location.LocationProvider
import sparespark.forecast.data.provider.weather.WeatherPreferenceProvider

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider,
    private val weatherPreferenceProvider: WeatherPreferenceProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            /*
            * updateEntries..
            * */
            fetchedWeather.currentWeatherEntry?.let {
                currentWeatherDao.upsert(it)
            }
            fetchedWeather.location?.let {
                weatherLocationDao.upsert(it)
                if (it.country.isNotBlank()) weatherPreferenceProvider.updateNonLiveWeatherLocation(
                    it.country
                )
            }

        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getNonLiveLocation()
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentWeatherNeeded(lastWeatherLocation.zonedDateTime)) fetchCurrentWeather()
    }


    private fun isFetchCurrentWeatherNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val twoHoursAgo = ZonedDateTime.now().minusHours(2)
        return lastFetchTime.isBefore(twoHoursAgo)
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString()
        )
    }

    /*
    *
    *
    * */

    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocationEntry> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }
}
