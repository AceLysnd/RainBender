package com.ace.rainbender.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.databinding.FragmentBookmarkBinding
import com.ace.rainbender.ui.adapter.BookmarksAdapter
import com.ace.rainbender.ui.viewmodel.BookmarksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding

    private val viewModel: BookmarksViewModel by viewModels()

    lateinit var bookmarksAdapter: BookmarksAdapter

    lateinit var rvBookmarks: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBookmarks = binding.rvBookmarks

        setLayout()
        setAdapter()

        var username = ""
        viewModel.getAccount().observe(viewLifecycleOwner){
            username = it.username
            viewModel.getUser(username)

        }

        viewModel.getUser.observe(viewLifecycleOwner){
            binding.pbPost.isVisible = true
            binding.rvBookmarks.isVisible = false
            bookmarksAdapter.setItems(it.bookmark)
            val postDelayed = Handler(Looper.myLooper()!!).postDelayed({

                binding.pbPost.isVisible = false
                binding.rvBookmarks.isVisible = true
            }, 3000)

        }

        binding.btnAdd.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host, SearchCityFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

    }

    private fun setLayout() {
        rvBookmarks.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setAdapter() {
        bookmarksAdapter = BookmarksAdapter(mutableListOf()) {result -> onResultClick(result) }
        rvBookmarks.adapter = bookmarksAdapter
    }

    private fun onResultClick(result: Result) {
        RES_LAT = result.latitude!!
        RES_LONG = result.longitude!!
        RES_LOCATION = result.name!!
        RES_ADMIN1 = result.admin1!!
        RES_COUNTRY = result.country!!
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host, BookmarksDetail())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
    companion object {
        var RES_LAT = 0.0
        var RES_LONG = 0.0
        var RES_LOCATION = ""
        var RES_ADMIN1 = ""
        var RES_COUNTRY = ""
    }
}