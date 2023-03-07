package com.ace.rainbender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.data.model.news.Article
import com.ace.rainbender.databinding.ItemCitySearchBinding
import com.ace.rainbender.databinding.ItemNewsBinding
import com.bumptech.glide.Glide

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
        val binding = ItemCitySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            itemView.setOnClickListener { onResultClick.invoke(item) }

        }
    }
}