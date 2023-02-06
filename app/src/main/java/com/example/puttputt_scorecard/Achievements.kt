package com.example.puttputt_scorecard

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class Achievements : AppCompatActivity() {
    var numEarned = 0
    var NUM_ACHIEVEMENTS = 10
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
        setContentView(R.layout.activity_achievements)
        setUpAdaptiveAd()
        loadAchievementStatus()
    }

    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayoutAchievements)
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
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/8458235057"
    }

    private fun loadAchievementStatus() {
        // Determine status of "One is Done"
        loadOneIsDone()
        loadMiniGolfVet()
        loadExplorer()
        loadRatedApp()
        loadSharingIsCaring()
        loadNoMercy()
        loadHistoricGame()
        loadPar()
        loadBirdie()
        loadEagle()

        // Lastly, run this one
        loadMiniGolfPro()
    }

    private fun loadOneIsDone() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val oneIsDoneStatusLabel: EditText = findViewById(R.id.oneIsDoneStatusLabel)

        // Get the number of players
        val numGamesCompleted = sharedPref.getInt("numGamesCompleted", 0)

        // Update the text of the label
        if (numGamesCompleted == 0) {
            oneIsDoneStatusLabel.setText("0 / 1")
        }
        else {
            oneIsDoneStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadMiniGolfVet() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val miniGolfVetStatusLabel: EditText = findViewById(R.id.miniGolfVetStatusLabel)

        // Get the number of players
        val numGamesCompleted = sharedPref.getInt("numGamesCompleted", 0)

        // Update the text of the label
        if (numGamesCompleted < 5) {
            miniGolfVetStatusLabel.setText("$numGamesCompleted / 5")
        }
        else {
            miniGolfVetStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadExplorer() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val explorerStatusLabel: EditText = findViewById(R.id.explorerStatusLabel)

        // Initialize to 2 for main menu & achievements
        var numScreensVisited = 2
        if (sharedPref.getInt("historyVisited",0) == 1) {
            numScreensVisited += 1
        }

        if (sharedPref.getInt("locationVisited",0) == 1) {
            numScreensVisited += 1
        }

        if (sharedPref.getInt("numPlayersVisited",0) == 1) {
            numScreensVisited += 1
        }

        if (sharedPref.getInt("scoreCardVisited",0) == 1) {
            numScreensVisited += 1
        }

        if (numScreensVisited < 6) {
            explorerStatusLabel.setText("$numScreensVisited / 6")
        }
        else {
            explorerStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadRatedApp() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val appRatedStatusLabel: EditText = findViewById(R.id.appRatedStatusLabel)

        // Get the number of players
        val appRated = sharedPref.getInt("appRated", 0)

        // Update the text of the label
        if (appRated == 0) {
            appRatedStatusLabel.setText("0 / 1")
        }
        else {
            appRatedStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadSharingIsCaring() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val sharingStatusLabel: EditText = findViewById(R.id.sharingStatusLabel)

        // Get the number of players
        val appShared = sharedPref.getInt("appShared", 0)

        // Update the text of the label
        if (appShared == 0) {
            sharingStatusLabel.setText("0 / 1")
        }
        else {
            sharingStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadNoMercy() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val noMercyStatusLabel: EditText = findViewById(R.id.noMercyStatusLabel)

        // Get the number of players
        val badHole = sharedPref.getInt("noMercy", 0)

        // Update the text of the label
        if (badHole == 0) {
            noMercyStatusLabel.setText("0 / 1")
        }
        else {
            noMercyStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadHistoricGame() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val historicGameStatusLabel: EditText = findViewById(R.id.historicGameStatusLabel)

        // Get the number of players
        val historicGame = sharedPref.getInt("historicGameVisited", 0)

        // Update the text of the label
        if (historicGame == 0) {
            historicGameStatusLabel.setText("0 / 1")
        }
        else {
            historicGameStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadPar() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val parStatusLabel: EditText = findViewById(R.id.parStatusLabel)

        // Check if par has been met
        val parMet = sharedPref.getInt("parAchieved", 0)

        // Update the text of the label
        if (parMet == 0) {
            parStatusLabel.setText("0 / 1")
        }
        else {
            parStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadBirdie() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val birdieStatusLabel: EditText = findViewById(R.id.birdieStatusLabel)

        // Check if par has been met
        val birdieMet = sharedPref.getInt("birdieAchieved", 0)

        // Update the text of the label
        if (birdieMet == 0) {
            birdieStatusLabel.setText("0 / 1")
        }
        else {
            birdieStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadEagle() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val eagleStatusLabel: EditText = findViewById(R.id.eagleStatusLabel)

        // Check if par has been met
        val eagleMet = sharedPref.getInt("eagleAchieved", 0)

        // Update the text of the label
        if (eagleMet == 0) {
            eagleStatusLabel.setText("0 / 1")
        }
        else {
            eagleStatusLabel.setText(getString(R.string.Complete))
            numEarned += 1
        }
    }

    private fun loadMiniGolfPro() {
        val miniGolfProStatusLabel: EditText = findViewById(R.id.miniGolfProStatusLabel)

        // Update the text of the label
        if (numEarned != NUM_ACHIEVEMENTS) {
            miniGolfProStatusLabel.setText("$numEarned / $NUM_ACHIEVEMENTS")
        }
        else {
            miniGolfProStatusLabel.setText(getString(R.string.Complete))
        }
    }
}