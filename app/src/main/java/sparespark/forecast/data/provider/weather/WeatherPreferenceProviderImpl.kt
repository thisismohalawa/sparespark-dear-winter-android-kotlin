package sparespark.forecast.data.provider.weather

import android.content.Context
import sparespark.forecast.data.provider.PreferenceProvider


private const val NON_LIVE_LOCATION = "NON_LIVE_LOCATION"
private const val NON_LIVE_TEMP = "NON_LIVE_TEMP"

class WeatherPreferenceProviderImpl(
    context: Context,
) : WeatherPreferenceProvider, PreferenceProvider(context) {

    override fun getNonLiveWeatherLocation(): String =
        preferences.getString(NON_LIVE_LOCATION, "Egypt").toString()

    override fun getNonLiveWeatherTemp(): Double =
        java.lang.Double.longBitsToDouble(
            preferences.getLong(
                NON_LIVE_TEMP,
                java.lang.Double.doubleToRawLongBits(0.0)
            )
        )

    override fun updateNonLiveWeatherLocation(country: String) =
        preferencesEditor.putString(NON_LIVE_LOCATION, country).apply()

    override fun updateNonLiveWeatherTemp(temp: Double) =
        preferencesEditor.putLong(
            NON_LIVE_TEMP,
            java.lang.Double.doubleToRawLongBits(temp)
        ).apply()
}
