package sparespark.forecast.data.repository.analytics

interface WeatherAnalytics {
    fun isRemoteWeatherTwoHoursAgo(remoteTimeString: String?): Boolean
    fun currentTempStatus(oldTemp: Double?, newTemp: Double?): String
}
