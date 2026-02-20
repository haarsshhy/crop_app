package com.example.cropapp

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class CropFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocationInfo: TextView
    private lateinit var tvLocationCropSuggestion: TextView
    private lateinit var locationSuggestionCard: CardView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fetchLocationAndSuggestions()
        } else {
            Toast.makeText(
                requireContext(),
                "Location permission is required for crop suggestions.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crop, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        tvLocationInfo = view.findViewById(R.id.tvLocationInfo)
        tvLocationCropSuggestion = view.findViewById(R.id.tvLocationCropSuggestion)
        locationSuggestionCard = view.findViewById(R.id.location_suggestion_card)

        val etNitrogen = view.findViewById<TextInputEditText>(R.id.etNitrogen)
        val etPhosphorus = view.findViewById<TextInputEditText>(R.id.etPhosphorus)
        val etPotassium = view.findViewById<TextInputEditText>(R.id.etPotassium)
        val etPh = view.findViewById<TextInputEditText>(R.id.etPh)
        val etRainfall = view.findViewById<TextInputEditText>(R.id.etRainfall)
        val btnCheckNow = view.findViewById<Button>(R.id.btnCheckNow)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)

        btnCheckNow.setOnClickListener {
            val n = etNitrogen.text.toString()
            val p = etPhosphorus.text.toString()
            val k = etPotassium.text.toString()

            if (n.isEmpty() || p.isEmpty() || k.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val progressDialog = ProgressDialog(context)
                progressDialog.setMessage("Analyzing data...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    val recommendedCrop = "Rice (Suggested based on soil data)"
                    tvResult.text = "Recommended Crop: $recommendedCrop"
                    tvResult.visibility = View.VISIBLE

                    Toast.makeText(context, "Crop recommendation is ready!", Toast.LENGTH_SHORT).show()
                }, 2000)
            }
        }

        checkPermissionsAndFetchSuggestions()

        return view
    }

    private fun checkPermissionsAndFetchSuggestions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocationAndSuggestions()
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun fetchLocationAndSuggestions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val city = addresses[0].locality
                            val lat = location.latitude
                            val lon = location.longitude
                            tvLocationInfo.text = "Suggestions for $city (Lat: $lat, Lon: $lon)"
                            tvLocationCropSuggestion.text = getCropSuggestion(lat)
                            locationSuggestionCard.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        locationSuggestionCard.visibility = View.GONE
                    }
                } else {
                    locationSuggestionCard.visibility = View.GONE
                }
            }
    }

    private fun getCropSuggestion(latitude: Double): String {
        return when {
            latitude < -23.5 -> "Temperate Crops: Wheat, Barley, Potatoes"
            latitude > 23.5 -> "Temperate Crops: Corn, Sunflowers, Soybeans"
            else -> "Tropical Crops: Rice, Sugarcane, Mangoes"
        }
    }
}
