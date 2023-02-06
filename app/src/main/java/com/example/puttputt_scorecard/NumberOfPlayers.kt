package com.example.puttputt_scorecard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.admanager.AdManagerAdRequest

class NumberOfPlayers : AppCompatActivity() {
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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_of_players)

        setUpAdaptiveAd()

        setUpAllButtons()
        numPlayersVisited()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayout)
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

        // Real Ad Unit
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/5832071718"
    }

    private fun setUpAllButtons() {
        val intent = Intent(this, NumberOfHoles::class.java)

        // Set up the number of players button
        val players1: android.widget.Button = findViewById(R.id.players1)
        players1.setOnClickListener {
            intent.putExtra("numberOfPlayers", 1)
            setNumberOfPlayers(1)
            startActivity(intent)
        }

        val players2: android.widget.Button = findViewById(R.id.players2)
        players2.setOnClickListener {
            intent.putExtra("numberOfPlayers", 2)
            setNumberOfPlayers(2)
            startActivity(intent)
        }

        val players3: android.widget.Button = findViewById(R.id.players3)
        players3.setOnClickListener {
            intent.putExtra("numberOfPlayers", 3)
            setNumberOfPlayers(3)
            startActivity(intent)
        }

        val players4: android.widget.Button = findViewById(R.id.players4)
        players4.setOnClickListener {
            intent.putExtra("numberOfPlayers", 4)
            setNumberOfPlayers(4)
            startActivity(intent)
        }

        val players5: android.widget.Button = findViewById(R.id.players5)
        players5.setOnClickListener {
            intent.putExtra("numberOfPlayers", 5)
            setNumberOfPlayers(5)
            startActivity(intent)
        }

        val players6: android.widget.Button = findViewById(R.id.players6)
        players6.setOnClickListener {
            intent.putExtra("numberOfPlayers", 6)
            setNumberOfPlayers(6)
            startActivity(intent)
        }

        val players7: android.widget.Button = findViewById(R.id.players7)
        players7.setOnClickListener {
            intent.putExtra("numberOfPlayers", 7)
            setNumberOfPlayers(7)
            startActivity(intent)
        }
        val players8: android.widget.Button = findViewById(R.id.players8)
        players8.setOnClickListener {
            intent.putExtra("numberOfPlayers", 8)
            setNumberOfPlayers(8)
            startActivity(intent)
        }
        val players9: android.widget.Button = findViewById(R.id.players9)
        players9.setOnClickListener {
            intent.putExtra("numberOfPlayers", 9)
            setNumberOfPlayers(9)
            startActivity(intent)
        }
    }

    private fun setNumberOfPlayers(number: Int) {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save number of players
            putInt("numberOfPlayers", number)
            apply() // asynchronously saves data, where as commit() synchronously saves, blocking main thread
        }
    }

    private fun numPlayersVisited() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("numPlayersVisited", 1)
            apply()
        }
    }
}