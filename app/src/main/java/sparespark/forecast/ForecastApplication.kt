package sparespark.forecast

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import sparespark.forecast.data.db.ForecastDatabase
import sparespark.forecast.data.network.connectivity.ConnectivityInterceptor
import sparespark.forecast.data.network.connectivity.ConnectivityInterceptorImpl
import sparespark.forecast.data.network.datasource.WeatherNetworkDataSource
import sparespark.forecast.data.network.datasource.WeatherNetworkDataSourceImpl
import sparespark.forecast.data.network.response.WeatherStackService
import sparespark.forecast.data.provider.location.LocationProvider
import sparespark.forecast.data.provider.location.LocationProviderImpl
import sparespark.forecast.data.provider.user.UserPreferenceProvider
import sparespark.forecast.data.provider.user.UserPreferenceProviderImpl
import sparespark.forecast.data.provider.weather.WeatherPreferenceProvider
import sparespark.forecast.data.provider.weather.WeatherPreferenceProviderImpl
import sparespark.forecast.data.repository.analytics.WeatherAnalytics
import sparespark.forecast.data.repository.analytics.WeatherAnalyticsImpl
import sparespark.forecast.data.repository.forecast.ForecastRepository
import sparespark.forecast.data.repository.forecast.ForecastRepositoryImpl
import sparespark.forecast.data.repository.user.UserRepository
import sparespark.forecast.data.repository.user.UserRepositoryImpl
import sparespark.forecast.ui.currentweather.viewmodel.CurrentWeatherViewModelFactory
import sparespark.forecast.ui.login.viewmodel.LoginViewModelFactory

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        // NetworkResponse
        bind() from singleton { WeatherStackService(instance()) }
        // DataSource
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        // Connectivity
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        // database
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentUserDao() }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        // location provider
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        // pref
        bind<WeatherPreferenceProvider>() with singleton { WeatherPreferenceProviderImpl(instance()) }
        bind<UserPreferenceProvider>() with singleton { UserPreferenceProviderImpl(instance()) }
        // repository
        bind<WeatherAnalytics>() with singleton { WeatherAnalyticsImpl() }
        bind<UserRepository>() with singleton {
            UserRepositoryImpl(
                instance(),
                instance()
            )
        }
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                instance(), instance(), instance(), instance(), instance()
            )
        }
        // viewmodel
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        FirebaseApp.initializeApp(this)
    }
}
