package com.example.cropapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import kotlin.random.Random

class ScannerFragment : Fragment() {

    private lateinit var ivLeafPreview: ImageView
    private lateinit var btnScanLeaf: MaterialButton
    private lateinit var resultCard: CardView
    private lateinit var tvResultTitle: TextView
    private lateinit var tvResultBody: TextView

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                ivLeafPreview.setImageBitmap(imageBitmap)
                simulateDiseaseDetection()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

        ivLeafPreview = view.findViewById(R.id.ivLeafPreview)
        btnScanLeaf = view.findViewById(R.id.btnScanLeaf)
        resultCard = view.findViewById(R.id.resultCard)
        tvResultTitle = view.findViewById(R.id.tvResultTitle)
        tvResultBody = view.findViewById(R.id.tvResultBody)

        btnScanLeaf.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        return view
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Could not launch camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun simulateDiseaseDetection() {
        val isHealthy = Random.nextBoolean()
        val resultTitle: String
        val resultBody: String

        if (isHealthy) {
            resultTitle = "Healthy Leaf"
            resultBody = "No disease detected. Keep up the good work!"
        } else {
            resultTitle = "Powdery Mildew Detected"
            resultBody = "This is a common fungal disease. Consider applying a fungicide and improving air circulation."
        }

        tvResultTitle.text = resultTitle
        tvResultBody.text = resultBody
        resultCard.visibility = View.VISIBLE
    }
}
