package sparespark.forecast.data.model

import sparespark.forecast.core.internal.getNowLocalDateTime

data class FirebaseWeather(
    val temperature: Double? = 0.0,
    val windSpeed: Double? = 0.0,
    val windDegree: Double? = 0.0,
    val windDir: String? = "",
    val pressure: Double? = 0.0,
    val time: String? = getNowLocalDateTime()
) {
    constructor(temp: Double?, time: String?) : this(
        temperature = temp, null, null, null, null, time = time
    )
}
