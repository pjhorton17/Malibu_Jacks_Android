package com.example.puttputt_scorecard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener

class Location : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Set up all of the buttons
        setUpAllButtons()

        locationVisited()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpAllButtons() {
        // Set up the achievements button
        val nextButton: android.widget.Button = findViewById(R.id.goToNumOfPlayers)
        val locationTextField: android.widget.EditText = findViewById(R.id.locationTextField)

        // Check if text is in text field
        checkIfLocationIsPopulated()

        // Add text listener to the text field
        locationTextField.addTextChangedListener {
            if (locationTextField.text.toString() != "") {
                nextButton.isEnabled = true
                nextButton.setTextColor(Color.BLACK)
            }
            else {
                nextButton.isEnabled = false
                nextButton.setTextColor(Color.GRAY)
            }
        }

        // Set the listener to the next button
        nextButton.setOnClickListener {
            val intent = Intent(this, NumberOfPlayers::class.java)

            // Dismiss keyboard
            dismissKeyboard()

            saveLocation()
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    private fun showKeyboard() {
        val locationTextField: android.widget.EditText = findViewById(R.id.locationTextField)
        locationTextField.setFocusableInTouchMode(true);
        locationTextField.requestFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun dismissKeyboard() {
        val locationTextField: android.widget.EditText = findViewById(R.id.locationTextField)
        locationTextField.setFocusableInTouchMode(true);
        locationTextField.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(locationTextField.windowToken, 0)
    }

    private fun checkIfLocationIsPopulated() {
        val locationTextField: android.widget.EditText = findViewById(R.id.locationTextField)

        if (locationTextField.text.toString() != "") {
            val nextButton: android.widget.Button = findViewById(R.id.goToNumOfPlayers)
            nextButton.isEnabled = true
            nextButton.setTextColor(Color.BLACK)
        }
        else {
            val nextButton: android.widget.Button = findViewById(R.id.goToNumOfPlayers)
            nextButton.isEnabled = false
            nextButton.setTextColor(Color.GRAY)
        }
    }

    private fun saveLocation() {
        // Get the location text field
        val locationTextField: android.widget.EditText = findViewById(R.id.locationTextField)
        val location:String = locationTextField.text.toString()

        // Save the location to shared preferences
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save number of players
            putString("location", location)
            apply() // asynchronous saves data, where as commit() synchronise saves, blocking main thread
        }
        // For testing
        val savedLocation = sharedPref.getString("location", "")
        Log.d("Location", "Location = $savedLocation")
    }

    private fun locationVisited() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("locationVisited", 1)
            apply()
        }
    }
}