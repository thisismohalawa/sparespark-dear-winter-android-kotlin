package sparespark.forecast.ui.base

interface BaseViewInteract {
    interface View {
        fun moveToSettingsView()
        fun moveToLoginView()
        fun restartActivity()
    }
}
