package com.ace.rainbender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.databinding.ItemCitySearchBinding
import com.google.common.base.Enums.getField

class CityResultAdapter(
    private var items: MutableList<Result>,
    private val onResultClick: (result: Result) -> Unit
) :
    RecyclerView.Adapter<CityResultAdapter.PostViewHolder>() {


    fun setItems(items: List<Result>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemCitySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    inner class PostViewHolder(private val binding: ItemCitySearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Result) {
            itemView.setOnClickListener { }

            with(binding) {
                tvLocation.text = item.name
                tvDetails.text = item.admin1 + ", " + item.country

                val firstLetter = Character.codePointAt(item.countryCode, 0) - 0x41 + 0x1F1E6
                val secondLetter = Character.codePointAt(item.countryCode, 1) - 0x41 + 0x1F1E6
                val countryFlag =
                    String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))

                tvFlag.text = countryFlag
            }

            itemView.setOnClickListener { onResultClick.invoke(item) }

        }
    }
}