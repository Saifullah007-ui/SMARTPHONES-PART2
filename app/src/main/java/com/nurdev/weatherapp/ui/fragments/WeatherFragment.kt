package com.nurdev.weatherapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.nurdev.weatherapp.R
import com.nurdev.weatherapp.data.local.CityDB
import com.nurdev.weatherapp.data.local.CityViewModel
import com.nurdev.weatherapp.databinding.FragmentWeatherBinding
import com.nurdev.weatherapp.utils.RetrofitInstance
import com.nurdev.weatherapp.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private var fragment_search: SearchFragment = SearchFragment()

    private lateinit var database: CityDB
    private lateinit var viewModel: CityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[CityViewModel::class.java]

        database = CityDB.getDatabase(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var weekDay = LocalDate.now().dayOfWeek.name
            var day = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")).toString()
            var month = LocalDate.now().month.toString()
            var year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")).toString()
            binding.date.text = "${
                weekDay[0] + weekDay.substring(1).lowercase()
            } | $day ${month[0] + month.substring(1, 3).lowercase()} $year"
        }

        binding.profileIcon.setOnClickListener {
            var transaction: FragmentTransaction =
                activity?.supportFragmentManager?.beginTransaction()!!
            transaction
                .replace(R.id.main, fragment_search)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.allCities.asFlow().asLiveData().observe(requireActivity()) {
            val cities = mutableListOf<String>()
            it.forEach { city ->
                cities.add(city.city)
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            if (cities.isEmpty()) {
                binding.autoCompleteTextView.text.clear()
                binding.autoCompleteTextView.text.append("Choose a city")
            }

            binding.autoCompleteTextView.doOnTextChanged { text, start, before, count ->
                if (cities.contains(text.toString())) {
                    getCurrentWeather(text.toString())
                }
            }
        }
    }

    private fun getCurrentWeather(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.weatherApi.getCurrentWeather(city, "metric")
            } catch (e: IOException) {
                Toast.makeText(context, "app error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(context, "http error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }
            Log.e("result", response.toString())

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    binding.tvDesc.text = response.body()!!.weather[0].main
                    binding.tvTemp.text = "${response.body()!!.main.temp.toInt()}°C"
                    Glide.with(requireContext())
                        .load("${Util.IMG_URL + response.body()!!.weather[0].icon}@4x.png")
                        .into(binding.ivIcon)
                }
            }
        }


    }

}
