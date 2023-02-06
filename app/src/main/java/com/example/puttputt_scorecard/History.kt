package com.example.puttputt_scorecard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.json.JSONObject
import java.io.File


class History : AppCompatActivity() {
    private var gameNumber = 0
    private var gameSelected = 0
    var gameToDelete = 0

    // For sort by location
    var allLocations = arrayOf<String>()
    var sortedLocationsByGame = arrayOf<String>()
    var byLocation = false
    var locationAccending = false

    // For sort by date
    var allDates = arrayOf<String>()
    var sortedDatesByGame = arrayOf<String>()
    var byDate = false
    var dateAccending = false

    // For sort by Score
    var allScores = arrayOf<String>()
    var sortedScoresByGame = arrayOf<String>()
    var byScore = false
    var scoreAccending = false

    // For sort by winningPlayer
    var allWinningPlayers = arrayOf<String>()
    var sortedWinningPlayersByGame = arrayOf<String>()
    var byWinningPlayer = false
    var winningPlayerAccending = false

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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val deleteGameConfirmationView: android.widget.FrameLayout = findViewById(R.id.deleteGameConfirmationView)
        deleteGameConfirmationView.alpha = 0.0F

        setUpAdaptiveAd()

        setUpAllElements()
        determineIfThereIsHistory()
        historyVisited()
        setUpButtons()

    }

    private fun setUpButtons() {
        val dgConfirmationview: android.widget.FrameLayout = findViewById(R.id.deleteGameConfirmationView)
        val doNotDeleteGameButton: android.widget.Button = findViewById(R.id.doNotDeleteGameButton)
        doNotDeleteGameButton.setOnClickListener{
            dgConfirmationview.alpha = 0.0f
        }

        val yesDeleteGameButton: android.widget.Button = findViewById(R.id.yesDeleteGameButton)
        yesDeleteGameButton.setOnClickListener{
            val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)

            // Delete game
            var file = File(filesDir, "game_${gameToDelete}")
            file.delete()

            // Rename all of the files that are after the one we just deleted
            var done = false
            var increment = 1

            while (done == false) {

                    val from = File(filesDir, "game_${gameToDelete + increment}")
                    val to = File(filesDir, "game_${gameToDelete + increment - 1}")
                    if (from.exists()) {
                        from.renameTo(to)
                        increment += 1
                    }
                    else {
                        // Decrement the total number of games
                        gameNumber = sharedPref.getInt("game_Number", 0) - 1

                        // Save the new total number of games to history
                        val editor = sharedPref.edit()

                        editor.apply {
                            // Save the number of games completed
                            putInt("game_Number", gameNumber)
                            apply()
                        }

                        done = true
                    }
            }

            // Reload the table
            determineIfThereIsHistory()

            // Dismiss the view
            dgConfirmationview.alpha = 0.0f
        }
    }

    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayoutHistory)
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
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/9008899398"
    }

    private fun setUpAllElements() {
        val noHistoryTextView: TextView = findViewById(R.id.noHistoryTextView)
        noHistoryTextView.alpha = 0.0F

        val locationHeaderLabel: TextView = findViewById(R.id.locationHeaderLabel)
        locationHeaderLabel.setOnClickListener{
            sortByLocation()

            // Update the table
            populateHistory()
        }

        val dateHeaderLabel: TextView = findViewById(R.id.dateHeaderLabel)
        dateHeaderLabel.setOnClickListener{
            sortByDate()

            // Update the table
            populateHistory()
        }

        val scoreHeaderLabel: TextView = findViewById(R.id.scoreHeaderLabel)
        scoreHeaderLabel.setOnClickListener{
            sortByScore()

            // Update the table
            populateHistory()
        }

        val winningPlayerHeaderLabel: TextView = findViewById(R.id.winningPlayerHeaderLabel)
        winningPlayerHeaderLabel.setOnClickListener{
            sortByWinningPlayer()

            // Update the table
            populateHistory()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun determineIfThereIsHistory() {
        // Check if a game is in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        gameNumber = sharedPref.getInt("game_Number", 0)
        val noHistoryTextView: TextView = findViewById(R.id.noHistoryTextView)

        if (gameNumber > 0) {
            // Initialize the allLocations variables
            allLocations = Array(gameNumber, {i -> (i * 1).toString()})
            sortedLocationsByGame = Array(gameNumber, {i -> (i * 1).toString()})

            // Initialize the allDates variables
            allDates = Array(gameNumber, {i -> (i * 1).toString()})
            sortedDatesByGame = Array(gameNumber, {i -> (i * 1).toString()})

            // Initialize the allScores variables
            allScores = Array(gameNumber, {i -> (i * 1).toString()})
            sortedScoresByGame = Array(gameNumber, {i -> (i * 1).toString()})

            // Initialize the winningPlayers variables
            allWinningPlayers = Array(gameNumber, {i -> (i * 1).toString()})
            sortedWinningPlayersByGame = Array(gameNumber, {i -> (i * 1).toString()})

            // Remove the no history text
            noHistoryTextView.alpha = 0.0F

            // Populate the tableview/scrollview/instructions
            val historyScrollView: android.widget.ScrollView = findViewById(R.id.historyScrollView)
            historyScrollView.alpha = 1.0F
            val historyInstructions: android.widget.TextView = findViewById(R.id.historyInstructionsTV)
            historyInstructions.alpha = 1.0F

            // Populate the table
            populateHistory()
        }

        else {
            // There is not a game saved. So show the no history text and UI
            noHistoryTextView.alpha = 1.0F

            val historyScrollView: android.widget.ScrollView = findViewById(R.id.historyScrollView)
            historyScrollView.alpha = 0.0F

            val historyInstructions: android.widget.TextView = findViewById(R.id.historyInstructionsTV)
            historyInstructions.alpha = 0.0F
        }
    }

    private var myNum = 0
    @RequiresApi(Build.VERSION_CODES.Q)

    var deleteConfirmationDate:String      = ""
    var deleteConfirmationLocation: String = ""
    var deleteConfirmationWinner: String   = ""
    var deleteConfirmationScore: String    = ""

    @SuppressLint("SuspiciousIndentation")
    fun populateHistory() {

        // Get the table
        val historyTableView: android.widget.TableLayout = findViewById(R.id.scoreHistoryTableView)
        //historyTableView.removeAllViewsInLayout()
        val childCount = historyTableView.childCount

            // Remove all rows except the first one
            if (childCount > 1) {
                historyTableView.removeViews(1, childCount - 1);
            }

        for (i in 0 until gameNumber) {

            var file = File(filesDir, "game_${i + 1}")

            if (byLocation == true) {
                file = File(filesDir, "game_${sortedLocationsByGame[i]}")
            }
            else if (byDate == true) {
                file = File(filesDir, "game_${sortedDatesByGame[i]}")
            }

            else if (byScore == true) {
                file = File(filesDir, "game_${sortedScoresByGame[i]}")
            }

            else if (byWinningPlayer == true) {
                file = File(filesDir, "game_${sortedWinningPlayersByGame[i]}")
            }

            val fileReader = java.io.FileReader(file)
            val bufferedReader = java.io.BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            val line: String = bufferedReader.readLine()
           // while (line != null) {
                stringBuilder.append(line)
               // line = bufferedReader.readLine()
           // }
            bufferedReader.close()

            // This response will have Json Format String
            val response = stringBuilder.toString()

            // Turn the string into JSON
            val jsonObject = JSONObject(response)

       // for (i in 0..30) {
            // First, create a table row
            val rowToAdd = TableRow(this)

            // Now, create textViews to add to the table row
            for (j in 1..5) {
                val textViewToAdd = TextView(this)
                val buttonToAdd = Button(this)
                if (j != 5) {
                    textViewToAdd.setTextColor(Color.BLACK)
                    textViewToAdd.setTextSize(COMPLEX_UNIT_SP, 18f)
                    textViewToAdd.gravity = Gravity.CENTER_HORIZONTAL
                    textViewToAdd.minLines = 2
                    textViewToAdd.maxLines = 2
                    textViewToAdd.setPadding(10, 20, 10, 20)
                }

                else {
                    buttonToAdd.setTextColor(Color.BLACK)
                    buttonToAdd.setTextSize(COMPLEX_UNIT_SP, 18f)
                    buttonToAdd.gravity = Gravity.CENTER_HORIZONTAL
                    buttonToAdd.minLines = 2
                    buttonToAdd.maxLines = 2
                    buttonToAdd.setPadding(10, 20, 10, 20)
                }

                // Determine the color of the row
                if (i % 2 != 0) {
                    // Set background to green
                    textViewToAdd.setBackgroundResource(R.drawable.history_tv_cell_green)
                    buttonToAdd.setBackgroundResource(R.drawable.history_tv_cell_green)
                }
                else {
                    textViewToAdd.setBackgroundResource(R.drawable.history_tv_cell_yellow)
                    buttonToAdd.setBackgroundResource(R.drawable.history_tv_cell_yellow)
                }

                when (j) {
                    1 -> {
                        textViewToAdd.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 3f)
                        textViewToAdd.text = jsonObject.getString("date").toString()

                        // Add this to the allDates variable
                        if (byDate == true) {
                            allDates[i] = jsonObject.getString("date").toString() + "game_${sortedDatesByGame[i]}"
                            deleteConfirmationDate = jsonObject.getString("date").toString()
                            rowToAdd.tag = sortedDatesByGame[i].toInt()
                        }
                        else {
                            allDates[i] = jsonObject.getString("date").toString() + "game_${i + 1}"
                            deleteConfirmationDate = jsonObject.getString("date").toString()
                            rowToAdd.tag = sortedDatesByGame[i].toInt()
                        }

                        // Add this textView to the tableRow
                        rowToAdd.addView(textViewToAdd)

                    }
                    2 -> {
                        textViewToAdd.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 4f)
                        textViewToAdd.text = jsonObject.getString("location").toString()

                        // Add this to the allLocations variable
                        if (byLocation == true) {
                            allLocations[i] = jsonObject.getString("location").toString() + "game_${sortedLocationsByGame[i]}"
                            deleteConfirmationLocation = jsonObject.getString("location").toString()
                            rowToAdd.tag = sortedLocationsByGame[i].toInt()
                        }
                        else {
                            allLocations[i] = jsonObject.getString("location").toString() + "game_${i + 1}"
                            deleteConfirmationLocation = jsonObject.getString("location").toString()
                            rowToAdd.tag = sortedLocationsByGame[i].toInt()
                        }

                        // Add this textView to the tableRow
                        rowToAdd.addView(textViewToAdd)
                    }
                    3 -> {
                        textViewToAdd.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 4f)
                        textViewToAdd.text = jsonObject.getString("winningPlayer").toString()

                        // Add this to the allWinningPlayers variable
                        if (byWinningPlayer == true) {
                            allWinningPlayers[i] = jsonObject.getString("winningPlayer").toString() + "game_${sortedWinningPlayersByGame[i]}"
                            deleteConfirmationWinner = jsonObject.getString("winningPlayer").toString()
                            rowToAdd.tag = sortedWinningPlayersByGame[i].toInt()
                        }
                        else {
                            allWinningPlayers[i] = jsonObject.getString("winningPlayer").toString() + "game_${i + 1}"
                            deleteConfirmationWinner = jsonObject.getString("winningPlayer").toString()
                            rowToAdd.tag = sortedWinningPlayersByGame[i].toInt()
                        }

                        // Add this textView to the tableRow
                        rowToAdd.addView(textViewToAdd)
                    }
                    4 -> {
                        textViewToAdd.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 3f)
                        textViewToAdd.text = jsonObject.getString("winningScore").toString()

                        // Add this to the allScores variable
                        if (byScore == true) {
                            allScores[i] = jsonObject.getString("winningScore").toString() + "game_${sortedScoresByGame[i]}"
                            deleteConfirmationScore = jsonObject.getString("winningScore").toString()
                            rowToAdd.tag = sortedScoresByGame[i].toInt()
                        }
                        else {
                            allScores[i] = jsonObject.getString("winningScore").toString() + "game_${i + 1}"
                            deleteConfirmationScore = jsonObject.getString("winningScore").toString()
                            rowToAdd.tag = sortedScoresByGame[i].toInt()
                        }

                        // Add this textView to the tableRow
                        rowToAdd.addView(textViewToAdd)
                    }
                    5 -> {
                        buttonToAdd.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 3f)
                        buttonToAdd.text = getString(R.string.details)
                        buttonToAdd.setBackgroundResource(R.drawable.button_background)
                        buttonToAdd.isAllCaps = false

                        if (byLocation == true) {
                            buttonToAdd.setTag(R.id.details_button, (sortedLocationsByGame[i].toInt() - 1))
                            buttonToAdd.id = sortedLocationsByGame[i].toInt() - 1
                        }
                        else {
                            buttonToAdd.setTag(R.id.details_button, i)
                            buttonToAdd.id = i
                        }

                        buttonToAdd.setOnClickListener {
                            // Set the current cell
                             gameSelected = buttonToAdd.getTag(R.id.details_button) as Int
                            Log.d("Game Selected", "$gameSelected")

                            val intent = Intent(this, Details::class.java)

                            intent.putExtra("gameToLoad", gameSelected + 1)
                            intent.putExtra("gameNumber", gameSelected)

                            startActivity(intent)
                        }

                        buttonToAdd.setOnLongClickListener {

                            populateDeleteGameConfirmationView(rowToAdd.tag as Int)

                            true

                        }

                        // Add a swipe listener
                        rowToAdd.setOnLongClickListener {
                            populateDeleteGameConfirmationView(rowToAdd.tag as Int)

                            true
                        }

                        // Add this button to the tableRow
                        rowToAdd.addView(buttonToAdd)
                    }
                    else -> { // Note the block
                        print("idk")
                    }
                }
            }
            // Add this row to the tableView
            historyTableView.addView(rowToAdd)
        }
        // Set all filters to false
        setAllFiltersToFalse()
    }

    private fun populateDeleteGameConfirmationView(tag:Int) {
        val deleteGameConfirmationView: android.widget.FrameLayout = findViewById(R.id.deleteGameConfirmationView)
        val confirmationTextView: android.widget.TextView = findViewById(R.id.confirmationTextView)

        // Set the game to delete variable
        gameToDelete = (tag + 1)

        var file = File(filesDir, "game_${tag + 1}")
        val fileReader = java.io.FileReader(file)
        val bufferedReader = java.io.BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        val line: String = bufferedReader.readLine()
        // while (line != null) {
        stringBuilder.append(line)
        // line = bufferedReader.readLine()
        // }
        bufferedReader.close()

        // This response will have Json Format String
        val response = stringBuilder.toString()

        // Turn the string into JSON
        val jsonObject = JSONObject(response)

        // Get the info to display

        val score =  jsonObject.getString("winningScore").toString()
        val location = jsonObject.getString("location").toString()
        val winner = jsonObject.getString("winningPlayer").toString()
        val date = jsonObject.getString("date").toString()

        confirmationTextView.text = "Are you sure you want to delete this game?\n\n" +
                                    "Date: ${date}\n" +
                                    "Location: ${location}\n" +
                                    "Winner: ${winner}\n" +
                                    "Score: ${score}"
        // Present the view
        deleteGameConfirmationView.alpha = 1.0F
    }

    private fun setAllFiltersToFalse() {
        byLocation      = false
        byDate          = false
        byScore         = false
        byWinningPlayer = false
    }

    private fun historyVisited() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("historyVisited", 1)
            apply()
        }
    }

    private fun sortByLocation() {
        if (locationAccending == false) {
            allLocations.sort()
            locationAccending = true
        }

        else {
            allLocations.sortDescending()
            locationAccending = false
        }

        // Now, put the "game_x" in the correct order
        for (i in 0 until gameNumber) {
            // Get the "substring" that only contains "game_x"
            sortedLocationsByGame[i] = allLocations[i].substringAfterLast("_")
        }
        byLocation = true
    }

    private fun sortByDate() {
        if (dateAccending == false) {
            allDates.sort()
            dateAccending = true
        }

        else {
            allDates.sortDescending()
            dateAccending = false
        }

        // Now, put the "game_x" in the correct order
        for (i in 0 until gameNumber) {
            // Get the "substring" that only contains "game_x"
            sortedDatesByGame[i] = allDates[i].substringAfterLast("_")
        }
        byDate = true
    }

    private fun sortByScore() {
        if (scoreAccending == false) {
            allScores.sort()
            scoreAccending = true
        }

        else {
            allScores.sortDescending()
            scoreAccending = false
        }

        // Now, put the "game_x" in the correct order
        for (i in 0 until gameNumber) {
            // Get the "substring" that only contains "game_x"
            sortedScoresByGame[i] = allScores[i].substringAfterLast("_")
        }
        byScore = true
    }

    private fun sortByWinningPlayer() {
        if (winningPlayerAccending == false) {
            allWinningPlayers.sort()
            winningPlayerAccending = true
        }

        else {
            allWinningPlayers.sortDescending()
            winningPlayerAccending = false
        }

        // Now, put the "game_x" in the correct order
        for (i in 0 until gameNumber) {
            // Get the "substring" that only contains "game_x"
            sortedWinningPlayersByGame[i] = allWinningPlayers[i].substringAfterLast("_")
        }
        byWinningPlayer = true
    }
}