package sparespark.forecast.data.repository.analytics

class WeatherAnalyticsImpl : WeatherAnalytics {
    private fun isTempRaising(oldTemp: Double, newTemp: Double): Boolean =
        oldTemp < newTemp

    private fun changeInTempValue(oldTemp: Double, newTemp: Double): Double =
        newTemp - oldTemp

    private fun changeInTempIsMinusOrPlusOneDegrees(oldTemp: Double, newTemp: Double): Boolean {
        return if (changeInTempValue(oldTemp, newTemp) > 1.0) false
        else changeInTempValue(oldTemp, newTemp) >= -1.0
    }

    override fun isRemoteWeatherTwoHoursAgo(remoteTimeString: String?): Boolean {
        if (remoteTimeString == null) return false
        return try {
            val twoHoursAgo = org.threeten.bp.LocalDateTime.now().minusHours(2)
            val remoteTime = org.threeten.bp.LocalDateTime.parse(remoteTimeString)
            remoteTime.isAfter(twoHoursAgo)
        } catch (ex: Exception) {
            false
        }
    }

    override fun currentTempStatus(oldTemp: Double?, newTemp: Double?): String =
        if (oldTemp == null || newTemp == null || newTemp == 0.0 ||
            oldTemp == 0.0
        ) "Tap for more details."
        else if (changeInTempIsMinusOrPlusOneDegrees(oldTemp, newTemp))
            "Steady-state temperature."
        else if (isTempRaising(oldTemp, newTemp))
            "Temp has risen (${changeInTempValue(oldTemp, newTemp)}°) degree up."
        else "Temp drops down (${changeInTempValue(oldTemp, newTemp)}°)."
}
