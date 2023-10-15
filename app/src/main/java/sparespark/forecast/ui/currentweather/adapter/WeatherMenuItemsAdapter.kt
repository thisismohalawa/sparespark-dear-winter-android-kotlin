package sparespark.forecast.ui.currentweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sparespark.forecast.data.model.WeatherMenuItem
import sparespark.forecast.databinding.ItemWeatherMenuGridBinding

class WeatherMenuItemsAdapter(
    private var itemList: MutableList<WeatherMenuItem>,
) : RecyclerView.Adapter<WeatherMenuItemsAdapter.WeatherMenuViewHolder>() {

    inner class WeatherMenuViewHolder(val binding: ItemWeatherMenuGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherMenuViewHolder {
        val binding = ItemWeatherMenuGridBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherMenuViewHolder, position: Int) {
        val weatherItem = itemList[position]
        holder.binding.item = weatherItem
    }

    override fun getItemCount(): Int = itemList.size

}
