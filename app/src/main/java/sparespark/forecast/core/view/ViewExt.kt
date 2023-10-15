package sparespark.forecast.core.view

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sparespark.forecast.ui.MainActivity


internal fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

internal fun Activity?.makeToast(value: String) {
    this?.let { Toast.makeText(it, value, Toast.LENGTH_SHORT).show() }
}

internal fun View.setClickListenerWithViewDelayEnabled(action: () -> Unit) {
    setOnClickListener {
        this.isEnabled = false
        action()
        postDelayed({ isEnabled = true }, 3000)
    }
}

internal fun FragmentManager.attachMainFragment(
    containerId: Int,
    view: Fragment,
    tag: String,
) {
    this.beginTransaction()
        .replace(containerId, view, tag)
        .commitNowAllowingStateLoss()
}

internal fun FragmentManager.attachFragment(
    containerId: Int,
    view: Fragment,
    backTag: String
) {
    this.beginTransaction()
        .replace(containerId, view)
        .addToBackStack(backTag)
        .commit()
}

internal fun MainActivity.restartMainActivity() {
    val intent: Intent? = applicationContext.packageManager
        .getLaunchIntentForPackage(applicationContext.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
