package com.nurdev.weatherapp.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.nurdev.weatherapp.R
import com.nurdev.weatherapp.databinding.ActivityMainBinding
import com.nurdev.weatherapp.ui.fragments.InternetFragment
import com.nurdev.weatherapp.ui.fragments.SearchFragment
import com.nurdev.weatherapp.ui.fragments.WeatherFragment
import com.nurdev.weatherapp.utils.NetworkConnectivityObserver


class MainActivity : AppCompatActivity(), NetworkConnectivityObserver.ConnectivityChangeListener {
    private lateinit var binding: ActivityMainBinding
    private var fragment: WeatherFragment = WeatherFragment()
    private var observer: NetworkConnectivityObserver? = null
    private var fragmentInternet: InternetFragment = InternetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer = NetworkConnectivityObserver()
        observer?.observeConnectivityChanges(this, this)

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction
            .replace(R.id.main, fragment)
            .addToBackStack(null)
            .commit()

        // hide the status bar
        // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        observer!!.stopObservingConnectivityChanges(this)
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        if (isConnected) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit()
            Toast.makeText(this, "Internet connection available", Toast.LENGTH_SHORT).show()
        } else {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction
                .replace(R.id.main, fragmentInternet)
                .addToBackStack(null)
                .commit()
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}