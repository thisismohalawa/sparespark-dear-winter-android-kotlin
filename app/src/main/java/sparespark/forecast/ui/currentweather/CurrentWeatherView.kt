package sparespark.forecast.ui.currentweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sparespark.forecast.core.internal.startHandlerPostDelayed
import sparespark.forecast.core.internal.weatherToDataItemsList
import sparespark.forecast.core.view.*
import sparespark.forecast.data.db.entity.CurrentWeatherEntry
import sparespark.forecast.data.db.entity.WeatherLocationEntry
import sparespark.forecast.databinding.CurrentweatherViewBinding
import sparespark.forecast.ui.base.BaseViewInteract
import sparespark.forecast.ui.base.ScopedFragment
import sparespark.forecast.ui.currentweather.adapter.WeatherMenuItemsAdapter
import sparespark.forecast.ui.currentweather.viewmodel.CurrentWeatherViewModel
import sparespark.forecast.ui.currentweather.viewmodel.CurrentWeatherViewModelFactory

@SuppressLint("SetTextI18n")
class CurrentWeatherView : ScopedFragment<CurrentweatherViewBinding>(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var communicator: BaseViewInteract.View
    private lateinit var viewModel: CurrentWeatherViewModel
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): CurrentweatherViewBinding = CurrentweatherViewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as BaseViewInteract.View
        viewModel = ViewModelProviders.of(
            this, viewModelFactory
        )[CurrentWeatherViewModel::class.java]
        setSwipeRefreshLayout()
        setUpViewsClickListener()
        setUpWeatherGridList()
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val currentWeather = viewModel.currentWeather.await()
        val weatherLocation = viewModel.weatherLocation.await()
        val currentUserName = viewModel.currentUserName.await()
        /*
        * observing
        *
        *
        * */
        currentWeather.observe(viewLifecycleOwner) { weather ->
            if (weather == null) return@observe
            updateWeather(weather)
            updateSwipeRefreshState(isRefresh = false)
        }
        weatherLocation.observe(viewLifecycleOwner) { location ->
            if (location == null) return@observe
            updateWeatherLocation(location)
        }
        currentUserName.observe(viewLifecycleOwner) { user ->
            if (user == null) return@observe
            updateSignedUserNameText(user)
        }
    }

    /*
    *
    *
    *
    *
    * */
    private fun updateSignedUserNameText(name: String) {
        binding.itemUserData.txtUserName.text = name
    }

    private fun updateWeatherLocation(location: WeatherLocationEntry) {
        binding.itemTempData.textLocation.text = "${location.country} ${location.name}"
    }

    private fun updateWeather(weather: CurrentWeatherEntry) {
        binding.itemTempData.textTemp.text = " ${weather.temperature}°"
        binding.recyclerViewMenu.adapter = WeatherMenuItemsAdapter(weatherToDataItemsList(weather))
    }

    private fun setUpViewsClickListener() {
        binding.itemUserData.imgSettings.setClickListenerWithViewDelayEnabled {
            communicator.moveToSettingsView()
        }

        binding.itemUserData.userLayout.setClickListenerWithViewDelayEnabled {
            communicator.moveToLoginView()
        }
    }

    private fun setUpWeatherGridList() = binding.recyclerViewMenu.apply {
        setHasFixedSize(true)
        val sGridLayoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        layoutManager = sGridLayoutManager
    }

    private fun updateSwipeRefreshState(isRefresh: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isRefresh
    }

    private fun setSwipeRefreshLayout() = binding.swipeRefreshLayout.apply {
        isRefreshing = true
        setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        setOnRefreshListener {
            startHandlerPostDelayed {
                communicator.restartActivity()
                isRefreshing = false
            }
        }
    }
}
