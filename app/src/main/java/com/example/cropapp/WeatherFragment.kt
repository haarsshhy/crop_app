package com.example.cropapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnNearbyPlaces = view.findViewById<Button>(R.id.btnNearbyPlaces)
        btnNearbyPlaces.setOnClickListener {
            Toast.makeText(context, "Loading nearby places...", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, WeatherNearbyPlacesActivity::class.java)
            startActivity(intent)
        }
    }
}
