package sparespark.forecast.core.internal

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import sparespark.forecast.data.service.remote.ACTION_LISTENER_START
import sparespark.forecast.data.service.remote.ACTION_LISTENER_STOP
import java.time.LocalDateTime
import java.time.ZoneId


internal fun startHandlerPostDelayed(action: (() -> Unit)? = null) {
    Handler(Looper.getMainLooper()).postDelayed({
        action?.let { it() }
    }, 2000)
}

internal fun getNowLocalDateTime(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    LocalDateTime.now(ZoneId.systemDefault()).toString()
} else ""

internal fun String?.toFullDateFormat(): String {
    return if (isNullOrBlank()) getNowLocalDateTime()
    else if (length > 15) {
        substring(0, 16).replace("T", " at ")
    } else replace("T", " at ")
}

internal fun Context.startRemoteListenerService(serviceClass: Class<*>) {
    val serviceIntent = Intent(this@startRemoteListenerService, serviceClass)
    serviceIntent.action = ACTION_LISTENER_START
    this.startService(serviceIntent)
}

internal fun Context.stopRemoteListenerService(serviceClass: Class<*>) {
    val stopIntent = Intent(this@stopRemoteListenerService, serviceClass)
    stopIntent.action = ACTION_LISTENER_STOP
    this.startService(stopIntent)
}

internal fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}
