package com.example.cropapp

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class CropFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crop, container, false)

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

                // Simulate a delay for processing
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    // Placeholder logic for crop recommendation
                    val recommendedCrop = "Rice (Suggested based on soil data)"
                    tvResult.text = "Recommended Crop: $recommendedCrop"
                    tvResult.visibility = View.VISIBLE

                    Toast.makeText(context, "Crop recommendation is ready!", Toast.LENGTH_SHORT).show()

                    // Trigger ACTIONABLE notification via MainActivity
                    (activity as? MainActivity)?.showActionableNotification(
                        "Recommendation Ready",
                        "We suggest planting $recommendedCrop. Tap to view market for seeds."
                    )
                }, 2000) // 2-second delay
            }
        }

        return view
    }
}
