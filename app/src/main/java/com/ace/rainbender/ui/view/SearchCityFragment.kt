package com.ace.rainbender.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ace.rainbender.R
import com.ace.rainbender.data.model.geocoding.GeocodeResponse
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.data.services.geocode.GeocodeApiHelper
import com.ace.rainbender.databinding.FragmentSearchCityBinding
import com.ace.rainbender.ui.adapter.CityResultAdapter
import com.ace.rainbender.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Result as Result1

@AndroidEntryPoint
class SearchCityFragment : Fragment() {

    private lateinit var binding: FragmentSearchCityBinding

    lateinit var rvLocation: RecyclerView

    lateinit var listAdapter: CityResultAdapter

//    lateinit var responseList: GeocodeResponse

    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchCityBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    var i = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = GeocodeApiHelper()

        rvLocation = binding.rvLocations
        searchView = binding.searchView

        setLayout()
        setAdapter()

//        responseList = ArrayList()
//        responseList.add("Depok")

//        listAdapter = ArrayAdapter<String>(
//            requireActivity(),
//            android.R.layout.simple_list_item_1,
//            responseList
//        )


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.pbPost.isVisible = true
                apiService.getGeocodeResponse(query!!) {
//                    responseList.clear()
                    if (it != null) {
                        if (!it.results.isNullOrEmpty()) {
                            Log.d("query response", it.results[0].name.toString())
                            i = 0
                            listAdapter.clearItems()
                            listAdapter.setItems(it.results)
//                            addResponseList(it.results)


                            val postDelayed = Handler(Looper.myLooper()!!).postDelayed({

                                binding.pbPost.isVisible = false
                            }, 1500)

//                            listAdapter = ArrayAdapter<String>(
//                                requireActivity(),
//                                android.R.layout.simple_list_item_1,
//                                responseList
//                            )

//                            lvLocation.adapter = listAdapter
                        }
                    }
                }
//
//                if (responseList.contains(query)) {
//                    listAdapter.filter.filter(query)
//                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {

//                listAdapter.filter.filter(newQuery)

                return false
            }
        })
    }

    private fun setAdapter() {
        listAdapter = CityResultAdapter(mutableListOf()) {result -> onResultClick(result) }
        rvLocation.adapter = listAdapter
    }

    private fun onResultClick(result: Result) {

    }

    private fun setLayout() {
        rvLocation.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

//    private fun addResponseList(results: List<Result?>) {
//        if (i < results.size) {
//            responseList.add(results[i]!!.name!!)
//            Log.d("query response added ", results[i]!!.name.toString())
//            i +=1
//            addResponseList(results)
//        }
//    }
}