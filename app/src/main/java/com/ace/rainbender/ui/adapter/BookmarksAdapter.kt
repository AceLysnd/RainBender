package com.ace.rainbender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.data.services.geocode.GeocodeApiHelper
import com.ace.rainbender.data.services.singleweather.SingleWApiHelper
import com.ace.rainbender.data.services.singleweather.SingleWApiService
import com.ace.rainbender.databinding.ItemBookmarksBinding
import com.ace.rainbender.databinding.ItemCitySearchBinding

class BookmarksAdapter (
    private var items: MutableList<Result>,
    private val onResultClick: (result: Result) -> Unit
) :
    RecyclerView.Adapter<BookmarksAdapter.PostViewHolder>() {


    fun setItems(items: List<Result>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun addItems(item: MutableList<Result>) {
        this.items.clear()
        this.items = item
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemBookmarksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    inner class PostViewHolder(private val binding: ItemBookmarksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val apiService = SingleWApiHelper()

        fun bindView(item: Result) {
            itemView.setOnClickListener { }

            with(binding) {
                tvLocation.text = item.name
                apiService.getWeatherForecast(item.latitude!!, item.longitude!!) {
                    tvTempMin.text = it!!.daily!!.temperature2mMin!![0].toString()
                    tvTempMax.text = it!!.daily!!.temperature2mMax!![0].toString() + "°C"

                    with(binding) {
                        if (it.daily!!.weathercode!![0] == 0) {
                            ivWeatherIcon.setImageResource(R.drawable.wesun)
                        } else if (it.daily.weathercode!![0] in 1..2) {
                            ivWeatherIcon.setImageResource(R.drawable.wesuncloudy)
                        } else if (it.daily.weathercode!![0] == 3) {
                            ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                        } else if (it.daily.weathercode!![0] in 45..48) {
                            ivWeatherIcon.setImageResource(R.drawable.wecloudy)
                        } else if (it.daily.weathercode!![0] in 51..57) {
                            ivWeatherIcon.setImageResource(R.drawable.werain)
                        } else if (it.daily.weathercode!![0] in 61..67) {
                            ivWeatherIcon.setImageResource(R.drawable.werain)
                        } else if (it.daily.weathercode!![0] in 71..75) {
                            ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                        } else if (it.daily.weathercode!![0] == 77) {
                            ivWeatherIcon.setImageResource(R.drawable.wecloudsnowy)
                        } else if (it.daily.weathercode!![0] in 80..82) {
                            ivWeatherIcon.setImageResource(R.drawable.werain)
                        } else if (it.daily.weathercode!![0] in 85..86) {
                            ivWeatherIcon.setImageResource(R.drawable.wesnow)
                        } else if (it.daily.weathercode!![0] in 95..99) {
                            ivWeatherIcon.setImageResource(R.drawable.wethunderstorm)
                        }
                    }

                }
//                tvDetails.text = item.admin1 + ", " + item.country
//
//                val firstLetter = Character.codePointAt(item.countryCode, 0) - 0x41 + 0x1F1E6
//                val secondLetter = Character.codePointAt(item.countryCode, 1) - 0x41 + 0x1F1E6
//                val countryFlag =
//                    String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
//
//                tvFlag.text = countryFlag
                tvDetails.text = item.admin1 + ", " + item.country
            }

            itemView.setOnClickListener { onResultClick.invoke(item) }

        }
    }
}