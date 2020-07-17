package com.kasiopec.cityweather.ui

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.model.CityItem

class WeatherListAdapter(
    var context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<WeatherViewHolder>() {

    var items: List<DatabaseEntities.CityWeather> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.cityName
        holder.moreDetailsText.text = context.resources.getString(R.string.more_details)
        holder.updateText.text = context.resources.getString(R.string.update_text, item.date)
        holder.temperatureText.text = context.resources.getString(R.string.temperature, item.temp.toString())
        holder.statusText.text = item.status
        holder.deleteImage.setOnClickListener{
            listener.onDeleteClicked(item)
        }
        holder.moreDetailsText.setOnClickListener{
            listener.onItemClicked(item)
        }
    }


}


class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameText : TextView = itemView.findViewById(R.id.cityName)
    val temperatureText : TextView = itemView.findViewById(R.id.temperature)
    val moreDetailsText : TextView = itemView.findViewById(R.id.moreDetails)
    val updateText : TextView = itemView.findViewById(R.id.updateDate)
    val statusText : TextView = itemView.findViewById(R.id.status)
    val deleteImage : ImageView = itemView.findViewById(R.id.delete)
}
