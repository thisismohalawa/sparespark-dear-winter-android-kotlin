package sparespark.forecast.data.model

data class WeatherMenuItem(
    val id: Int,
    val title: String,
    val subTitle: String? = null,
    val iconSrc: Int? = null,
    val isItemBackgrounded: Boolean = false,
    val isSwitchVisible: Boolean = false,
    val isMainTitleColored: Boolean = false,
)


