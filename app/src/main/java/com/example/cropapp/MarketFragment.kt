package com.example.cropapp

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class MarketFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market, container, false)

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading market data...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Simulate a delay for loading data
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            Toast.makeText(context, "Market data loaded", Toast.LENGTH_SHORT).show()
        }, 2000) // 2-second delay

        return view
    }
}
