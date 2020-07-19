package com.kasiopec.cityweather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.database.DatabaseEntities
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)


        val args = arguments
        val item = args?.getSerializable("item") as DatabaseEntities.CityWeather

        val cityText : TextView = view.findViewById(R.id.cityName)
        val tempText : TextView = view.findViewById(R.id.temperature)
        val statusDescriptionText : TextView = view.findViewById(R.id.status_description)
        val updateTimeText : TextView = view.findViewById(R.id.update_time)
        val windText : TextView = view.findViewById(R.id.wind)
        val windValueText : TextView = view.findViewById(R.id.wind_value)
        val humidityText : TextView = view.findViewById(R.id.humidity)
        val humidityValueText : TextView = view.findViewById(R.id.humidity_value)
        val feelLikeText : TextView = view.findViewById(R.id.feels_like)
        val feelsLikeValueText : TextView = view.findViewById(R.id.feels_like_value)
        val weatherIcon : ImageView = view.findViewById(R.id.weatherIcon)
        val requestTimeText : TextView = view.findViewById(R.id.request_time)


        cityText.text = item.cityName
        tempText.text = resources.getString(R.string.temperature, item.temp.toString())
        statusDescriptionText.text = item.statusDescription
        updateTimeText.text = resources.getString(R.string.update_text, item.date)
        requestTimeText.text = item.requestTime
        windText.text = resources.getString(R.string.wind_text)
        windValueText.text = resources.getString(R.string.wind_text_value, item.windSpeed.toString())
        humidityText.text = resources.getString(R.string.humidity_text)
        humidityValueText.text = resources.getString(R.string.humidity_text_value, item.humidity.toString())
        feelLikeText.text = resources.getString(R.string.feels_like_text)
        feelsLikeValueText.text = resources.getString(R.string.feels_like_text_value, item.feelsLike.toString())
        Picasso.get()
            .load(item.weatherIconUrl)
            .into(weatherIcon)

        // Inflate the layout for this fragment
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(activity as MainActivity).hideFab(true)
//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }
}