package com.nurdev.weatherapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurdev.weatherapp.R
import com.nurdev.weatherapp.data.local.City
import com.nurdev.weatherapp.data.local.CityDB
import com.nurdev.weatherapp.data.local.CityViewModel
import com.nurdev.weatherapp.databinding.FragmentSearchBinding
import com.nurdev.weatherapp.ui.adapters.SearchAdapter
import com.nurdev.weatherapp.utils.Util


class SearchFragment : Fragment(), SearchAdapter.RecyclerViewEvent {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var database: CityDB
    private lateinit var viewModel: CityViewModel
    private var cities = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[CityViewModel::class.java]

        database = CityDB.getDatabase(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclerView)

        viewModel.allCities.asFlow().asLiveData().observe(requireActivity()) {
            it.forEach { city ->
                if (!cities.contains(city.city)) {
                    cities.add(city.city)
                }
            }

            updateRecyclerView(recyclerView, Util.array)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0, Util.array, recyclerView)
                return true
            }
        })
    }

    private fun filterList(
        query: String?,
        dataList: MutableList<String>,
        recyclerView: RecyclerView?
    ) {
        val filteredList = mutableListOf<String>()

        if (!query.isNullOrBlank()) {
            for (item in dataList) {
                if (item.lowercase().contains(query.lowercase())) {
                    filteredList.add(item)
                }
            }
        } else {
            filteredList.addAll(dataList)
        }

        updateRecyclerView(recyclerView, filteredList)
    }

    override fun onItemClick(position: Int, textView: TextView, imageView: ImageView) {
        if (!cities.contains(textView.text.toString())) {
            imageView.setImageResource(R.drawable.favorite)
            viewModel.addCity(City(textView.text.toString()))
            Toast.makeText(
                requireContext(),
                "${textView.text} has been added",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            cities.remove(textView.text.toString())
            viewModel.removeByName(textView.text.toString())
            Toast.makeText(
                requireContext(),
                "${textView.text} has been deleted",
                Toast.LENGTH_SHORT
            ).show()
            imageView.setImageResource(R.drawable.favorite_border)
        }
    }

    private fun updateRecyclerView(recyclerView: RecyclerView?, list: List<String>) {
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = SearchAdapter(list, this, cities)
    }

}