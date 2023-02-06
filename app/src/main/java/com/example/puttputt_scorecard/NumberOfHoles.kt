package com.example.puttputt_scorecard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class NumberOfHoles : AppCompatActivity() {
    var otherNumberOfHoles = 3
    var numberOfPlayers = 1
    private lateinit var adView: AdView
    private lateinit var adContainerView: android.widget.FrameLayout
    private var initialLayoutComplete = false

    // Determine the screen width (less decorations) to use for the ad width.
    private val adSize: AdSize
        @RequiresApi(Build.VERSION_CODES.R)
        get() {
            val windowMetrics = windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds

            var adWidthPixels = adContainerView.width.toFloat()

            // If the ad hasn't been laid out, default to the full screen width.
            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numer_of_holes)

        setUpAdaptiveAd()

        setUpButtons()

        numberOfPlayers = intent.getIntExtra("numberOfPlayers", 1)
    }

    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayoutNumPlayers)
        adView = AdView(this)
        adContainerView.addView(adView)
        // Since we're loading the banner based on the adContainerView size, we need
        // to wait until this view is laid out before we can get the width.
        adContainerView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun loadBanner() {
        adView.adUnitId = AD_UNIT_ID
        adView.setAdSize(adSize)

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
        val adRequest = AdRequest
            .Builder()
            .build()
        //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)

    }
    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        //private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

        // Real ad unit
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/4538606697"
    }

    private fun setUpButtons() {

        // Set up the "9 holes" button
        val nineHolesSelectedButton: android.widget.Button = findViewById(R.id.nineHolesSelectedButton)
        nineHolesSelectedButton.setOnClickListener {
            val intent = Intent(this, Scorecard::class.java)
            intent.putExtra("numberOfHoles", 9)
            intent.putExtra("numberOfPlayers", numberOfPlayers)
            startActivity(intent)
        }

        // Set up the "18 holes" button
        val eighteenHolesSelectedButton: android.widget.Button = findViewById(R.id.eighteenHolesSelectedButton)
        eighteenHolesSelectedButton.setOnClickListener {
            val intent = Intent(this, Scorecard::class.java)
            intent.putExtra("numberOfHoles", 18)
            intent.putExtra("numberOfPlayers", numberOfPlayers)
            startActivity(intent)
        }

        // Set up the "confirm custom hole #" button
        val customHoleNumberSelectedButton: android.widget.Button = findViewById(R.id.customHoleNumberSelectedButton)
        customHoleNumberSelectedButton.setOnClickListener {
            val intent = Intent(this, Scorecard::class.java)
            intent.putExtra("numberOfHoles", otherNumberOfHoles)
            intent.putExtra("numberOfPlayers", numberOfPlayers)
            startActivity(intent)
        }

        // Set up the "Other" Holes button
        val otherNumberOfHolesButton: android.widget.Button = findViewById(R.id.otherNumberOfHolesButton)
        otherNumberOfHolesButton.setOnClickListener {
            // Hide the 9 and 18 holes button, and the "other" button
            nineHolesSelectedButton.alpha = 0.0F
            nineHolesSelectedButton.isVisible = false
            eighteenHolesSelectedButton.alpha = 0.0F
            eighteenHolesSelectedButton.isVisible = false
            otherNumberOfHolesButton.alpha = 0.0F
            otherNumberOfHolesButton.isVisible = false

            // Bring up the "Other # of holes" selection
            val otherHolesSelectionView: android.widget.LinearLayout = findViewById(R.id.otherHolesSelectionView)
            otherHolesSelectionView.alpha = 1.0F
            otherHolesSelectionView.isVisible = true

            // Bring up the - button
            val decreaseButton: android.widget.Button = findViewById(R.id.decreaseHoleCountButton)
            decreaseButton.alpha = 1.0F
            decreaseButton.isVisible = true

            // Bring up the + button
            val increaseButton: android.widget.Button = findViewById(R.id.increaseHoleCountButton)
            increaseButton.alpha = 1.0F
            increaseButton.isVisible = true

            // Bring up the next button
            customHoleNumberSelectedButton.alpha = 1.0F
        }

        // Set up the "increase hole #" button
        val increaseHoleCountButton: android.widget.Button = findViewById(R.id.increaseHoleCountButton)
        increaseHoleCountButton.alpha = 0.0F
        increaseHoleCountButton.isVisible = false

        increaseHoleCountButton.setOnClickListener {
            // Increment the # of holes
            if (otherNumberOfHoles < 25) {
                otherNumberOfHoles+= 1

                // Increase the # holes label
                val holeCountLabel: android.widget.TextView = findViewById(R.id.holeCountLabel)
                holeCountLabel.setText("${otherNumberOfHoles}")
            }
        }

        // Set up the "decrease hole #" button
        val decreaseHoleCountButton: android.widget.Button = findViewById(R.id.decreaseHoleCountButton)
        decreaseHoleCountButton.alpha = 0.0F
        decreaseHoleCountButton.isVisible = false

        decreaseHoleCountButton.setOnClickListener {
            // Increment the # of holes
            if (otherNumberOfHoles > 3) {
                otherNumberOfHoles-= 1

                // Increase the # holes label
                val holeCountLabel: android.widget.TextView = findViewById(R.id.holeCountLabel)
                holeCountLabel.setText("${otherNumberOfHoles}")
            }
        }
    }
}