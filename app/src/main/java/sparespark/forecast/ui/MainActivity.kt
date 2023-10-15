package sparespark.forecast.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import sparespark.forecast.R
import sparespark.forecast.core.Constants
import sparespark.forecast.core.view.attachFragment
import sparespark.forecast.core.view.attachMainFragment
import sparespark.forecast.core.view.makeToast
import sparespark.forecast.core.view.restartMainActivity
import sparespark.forecast.ui.base.BaseViewInteract
import sparespark.forecast.ui.currentweather.CurrentWeatherView
import sparespark.forecast.ui.login.LoginView
import sparespark.forecast.ui.settings.SettingsView

private const val CURRENT_WEATHER_VIEW = "CURRENT_WEATHER_VIEW"
private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware, BaseViewInteract.View {
    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.attachMainFragment(
            containerId = R.id.root_activity_main,
            view = CurrentWeatherView(),
            tag = CURRENT_WEATHER_VIEW,
        )

        if (!hasLocationPermission()) requestLocationPermission()
        else bindLocationManager()

        checkForNewUpdates()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun checkForNewUpdates() {
        val appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it, AppUpdateType.IMMEDIATE, this@MainActivity, Constants.UPDATE_REQUEST_CODE
                )
            }
        }.addOnFailureListener {
            makeToast(Constants.ERROR_UPDATING_VERSION)
        }
    }

    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle(getString(R.string.gps_settings))
        alertDialog.setMessage(getString(R.string.gps_not_enabled))

        alertDialog.setPositiveButton(
            getString(R.string.settings)
        ) { _, _ ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(intent)
        }

        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, _ -> dialog.cancel() }

        alertDialog.show()
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this, fusedLocationProviderClient, locationCallback
        )
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }


    override fun moveToSettingsView() {
        supportFragmentManager.attachFragment(
            containerId = R.id.root_activity_main,
            view = SettingsView(),
            backTag = CURRENT_WEATHER_VIEW
        )
    }

    override fun moveToLoginView() {
        supportFragmentManager.attachFragment(
            containerId = R.id.root_activity_main,
            view = LoginView(),
            backTag = CURRENT_WEATHER_VIEW
        )
    }

    override fun restartActivity() {
        restartMainActivity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bindLocationManager()
            makeToast(Constants.PERMISSION_GRANTED)
        } else makeToast(Constants.PERMISSION_DINED)
    }
}
