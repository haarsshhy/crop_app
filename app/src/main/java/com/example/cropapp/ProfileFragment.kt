package com.example.cropapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnSmsHelp = view.findViewById<Button>(R.id.btnSmsHelp)

        btnSmsHelp.setOnClickListener {
            val phoneNumber = "1234567890" // Replace with your helpline number
            val message = "I need help with the CropApp."

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", message)
            }
            startActivity(intent)
        }

        return view
    }
}