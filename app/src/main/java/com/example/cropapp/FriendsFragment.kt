package com.example.cropapp

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class FriendsFragment : Fragment() {

    private lateinit var tvSelectedDateTime: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        tvSelectedDateTime = view.findViewById(R.id.tvSelectedDateTime)
        val btnSelectDate = view.findViewById<Button>(R.id.btnSelectDate)
        val btnSelectTime = view.findViewById<Button>(R.id.btnSelectTime)
        val btnSetBirthday = view.findViewById<Button>(R.id.btnSetBirthday)

        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        btnSetBirthday.setOnClickListener {
            setBirthdayNotification()
        }

        updateDateTimeDisplay()

        return view
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateTimeDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                updateDateTimeDisplay()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun updateDateTimeDisplay() {
        tvSelectedDateTime.text = java.text.DateFormat.getDateTimeInstance().format(calendar.time)
    }

    private fun setBirthdayNotification() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), BirthdayReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(requireContext(), "Birthday notification set!", Toast.LENGTH_SHORT).show()
    }
}
