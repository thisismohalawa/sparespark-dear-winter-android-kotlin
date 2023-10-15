package sparespark.forecast.core.view

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import sparespark.forecast.R
import sparespark.forecast.core.internal.isServiceRunning
import sparespark.forecast.core.internal.startRemoteListenerService
import sparespark.forecast.core.internal.stopRemoteListenerService
import sparespark.forecast.data.service.remote.FirebaseListenerService

@BindingAdapter("setItemTitle")
fun setItemTitle(view: TextView, subTitle: String?) {
    if (subTitle != null) view.text = subTitle
    else view.visible(false)
}

@BindingAdapter("setItemIconSrc")
fun setItemIconSrc(view: ImageView, iconSrc: Int?) {
    if (iconSrc != null) view.setImageResource(iconSrc)
    else view.visible(false)
}

@BindingAdapter("setItemCardBackground")
fun setItemCardBackground(view: CardView, isItemBackgrounded: Boolean) {
    if (isItemBackgrounded)
        view.setCardBackgroundColor(
            ContextCompat.getColor(
                view.context,
                R.color.blue_200
            )
        )
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("setItemSwitchBeh")
fun setItemSwitchBeh(
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    switch: Switch,
    isSwitchVisible: Boolean,
) {
    val context = switch.context

    if (isSwitchVisible) switch.visible(true)

    fun startService() {
        context.startRemoteListenerService(FirebaseListenerService::class.java)
    }

    fun stopService() {
        context.stopRemoteListenerService(FirebaseListenerService::class.java)
    }

    switch.isChecked = context.isServiceRunning(FirebaseListenerService::class.java)

    switch.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) startService()
        else stopService()
    }
}
