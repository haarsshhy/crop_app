package com.example.cropapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.ChipGroup
import java.util.Locale

class MarketFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView
    private lateinit var rvShops: RecyclerView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fetchLocationAndData()
        } else {
            Toast.makeText(
                requireContext(),
                "Location permission is required for local market data.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        tvLocation = view.findViewById(R.id.tvLocation)
        rvShops = view.findViewById(R.id.rvShops)
        rvShops.layoutManager = LinearLayoutManager(context)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupMarketScope)

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipGlobal -> {
                    tvLocation.visibility = View.GONE
                    rvShops.visibility = View.GONE
                    Toast.makeText(context, "Showing global market data", Toast.LENGTH_SHORT).show()
                }
                R.id.chipLocal -> {
                    tvLocation.visibility = View.VISIBLE
                    rvShops.visibility = View.VISIBLE
                    checkPermissionsAndFetchData()
                }
            }
        }

        // Initially hide local data until selected
        tvLocation.visibility = View.GONE
        rvShops.visibility = View.GONE

        return view
    }

    private fun checkPermissionsAndFetchData() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocationAndData()
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

    private fun fetchLocationAndData() {
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
                        val lat = location.latitude
                        val lon = location.longitude
                        if (addresses != null && addresses.isNotEmpty()) {
                            val city = addresses[0].locality
                            tvLocation.text = "Location: $city (Lat: $lat, Lon: $lon)"
                            setupShopList()
                        }
                    } catch (e: Exception) {
                        tvLocation.text = "Location: Error"
                    }

                } else {
                    tvLocation.text = "Location: Not available"
                }
            }
    }

    private fun setupShopList() {
        val shops = listOf(
            Shop("Green Valley Organics", "123 Farm Road", 12.9716, 77.5946),
            Shop("Harvest Moon Supplies", "456 Market St", 12.9726, 77.5956),
            Shop("CropCare Solutions", "789 Agri Ave", 12.9736, 77.5966),
            Shop("Farmer's Friend", "101 Cultivator Ln", 12.9746, 77.5976)
        )

        val adapter = ShopAdapter(shops) { shop ->
            Toast.makeText(
                context,
                "Lat: ${shop.latitude}, Lon: ${shop.longitude}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvShops.adapter = adapter
    }
}
