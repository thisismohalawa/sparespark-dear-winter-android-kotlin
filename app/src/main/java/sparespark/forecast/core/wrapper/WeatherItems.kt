package sparespark.forecast.core.internal

import sparespark.forecast.R
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.model.WeatherMenuItem

internal fun weatherToDataItemsList(
    weather: CurrentWeatherEntry
): MutableList<WeatherMenuItem> {
    val list = mutableListOf<WeatherMenuItem>()
    list.add(
        WeatherMenuItem(
            0,
            title = "About\nWinter App?",
            subTitle = "Receive  a notification whenever the weather temperature changes, whether it drops down, gets too hot, or is about to rain."
        )
    )
    list.add(
        WeatherMenuItem(
            1,
            title = "Wind Degree is ${weather.windDegree}," +
                    "\n" + "Direction ${weather.windDir}",
            iconSrc = R.drawable.cloudy_128
        )
    )
    list.add(
        WeatherMenuItem(
            2, title = "Dear winter, I love you", isItemBackgrounded = true
        )
    )
    list.add(
        WeatherMenuItem(
            3, title = "Wind Speed is ${weather.windSpeed}"
        )
    )
    list.add(
        WeatherMenuItem(
            4,
            title = "Enable Notifications Listener?",
            iconSrc = R.drawable.ic_cloud,
            isSwitchVisible = true,

            )
    )
    return list
}
