package sparespark.forecast.data.provider.weather


interface WeatherPreferenceProvider {
    fun getNonLiveWeatherLocation(): String
    fun getNonLiveWeatherTemp():Double
    fun updateNonLiveWeatherLocation(country: String)
    fun updateNonLiveWeatherTemp(temp:Double)
}
