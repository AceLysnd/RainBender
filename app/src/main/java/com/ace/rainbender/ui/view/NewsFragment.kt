package com.ace.rainbender.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.data.model.news.Article
import com.ace.rainbender.data.services.news.NewsApiHelper
import com.ace.rainbender.databinding.FragmentNewsBinding
import com.ace.rainbender.ui.adapter.NewsAdapter
import com.ace.rainbender.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var newsRv: RecyclerView

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsRv = binding.rvNews

        loadNews()
        setLayout()
        setAdapter()

    }

    private fun setLayout() {
        newsRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setAdapter() {
        newsAdapter = NewsAdapter(mutableListOf()) { article -> onNewsClick(article) }
        newsRv.adapter = newsAdapter
    }

    private fun onNewsClick(article: Article?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(article!!.url)
        startActivity(intent)
    }

    private fun loadNews() {
        val apiService = NewsApiHelper()

        binding.pbPost.isVisible = true
        val postDelayed = Handler(Looper.myLooper()!!).postDelayed({
            binding.rvNews.isVisible = true
            binding.pbPost.isVisible = false
        }, 2000)



        apiService.getNews {
            newsAdapter.setItems(it!!.articles)
        }
    }
}