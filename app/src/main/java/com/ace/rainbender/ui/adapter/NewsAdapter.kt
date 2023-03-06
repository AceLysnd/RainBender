package com.ace.rainbender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.model.news.Article
import com.ace.rainbender.databinding.ItemNewsBinding
import com.bumptech.glide.Glide

class NewsAdapter(
    private var items: MutableList<Article>,
    private val onArticleClick: (article: Article) -> Unit
) :
    RecyclerView.Adapter<NewsAdapter.PostViewHolder>() {


    fun setItems(items: List<Article>?) {
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
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    inner class PostViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Article) {
            itemView.setOnClickListener { }

            Glide.with(binding.ivNewsImage.context)
                .load(item.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.newsplaceholder)
                .into(binding.ivNewsImage)
            binding.ivNewsImage
            binding.tvNewsTitle.text = item.title
            binding.tvNewsText.text = item.content
            binding.tvSource.text = item.source!!.name.toString()
            binding.tvPublishDate.text = item.publishedAt!!.substring(0,10)

            itemView.setOnClickListener { onArticleClick.invoke(item) }

        }
    }
}