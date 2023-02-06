package com.example.puttputt_scorecard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.TableLayout
import android.widget.LinearLayout
import java.lang.StringBuilder
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.core.view.setPadding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener


class Scorecard : AppCompatActivity() {
    private var playerNamesArray: Array<String> = emptyArray()
    private var gameCardJSON = JSONObject()
    private var playerTotalsJSON = JSONObject()
    private var parValuesJSON = JSONArray()
    private var colorValuesJSON = JSONArray()
    private var playerNamesJSON = JSONArray()
    private var numberOfPlayers = 1
    private var winningPlayer = "Player 1"
    private var winningScore = 0
    private var gameHistoryJSON = JSONObject()
    private var location = ""
    private var gameInProgress = false
    private var currentHole: Int = 0
    private var gameNumber = 0
    private var date = ""
    private var currentPlayer: Int = 0
    private var holesCompleted: Int = 0
    private var numberOfHoles:Int = 18
    private var xValue: Int = 0
    private var player1Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player2Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player3Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player4Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player5Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player6Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player7Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player8Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var player9Totals = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var parArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var coursePar = 0
    private var playerTotals: Array<IntArray> = arrayOf(player1Totals)
    private var completedHoleArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var safeAreaWidth = 0
    private var ballLabelWidth = 0
    private var playerLabelWidth = 0
    private var totalLabelWidth = 0
    private var totalLabelHeight = 0
    private var playerNameStackviewHeight = 0
    private var testWidth = 0
    private var totalScoreArray = intArrayOf(999, 999, 999, 999, 999, 999, 999, 999, 999)
    private var alreadyMercy = 0
    private var defaultBallColor = 0
    private var ballColorToUpdate = 0
    private var golfBallColorArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    val h = 115
    private lateinit var adView: AdView
    private lateinit var adContainerView: FrameLayout
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
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)
        setUpAdaptiveAd()
        setUpInterstitialAd()

        // Populate the JSON scorecards
        populatePlaceholderJSONScores()

        totalScoreArray = IntArray(numberOfPlayers) { 0 }

        // See if there is a game in progress
        loadGame()
        if (!gameInProgress) {
            numberOfPlayers = intent.getIntExtra("numberOfPlayers", 1)
            location = intent.getStringExtra("location").toString()
            numberOfHoles = intent.getIntExtra("numberOfHoles", 18)
        }

        // Dismiss all subviews
        dismissAllSubviews()

        // Configure the buttons
        configureButtons()

        gameInProgress = true

        // Add placeholder to player names
        playerNamesPlaceholder()

        // SaveGame
        saveGame()

        // Update the viewRanking table
        createViewRankingTable(false)
        scoreCardVisited()

    }

    private fun playerNamesPlaceholder() {
        for (i in 0 until numberOfPlayers) {
            playerNamesArray[i] = "Player ${i + 1}"
        }
    }

    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayoutScorecard)
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

    private fun setUpInterstitialAd() {
        var adRequest = AdRequest.Builder().build()

        // Test Ad Unit
        //InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback()

        // Real Ad Unit
        InterstitialAd.load(this,"ca-app-pub-5910555078334309/7017446286", adRequest, object : InterstitialAdLoadCallback()
        {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
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
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/3205908379"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val width: Int = android.content.res.Resources.getSystem().displayMetrics.widthPixels
        safeAreaWidth = width

        val playerLabel: TextView = findViewById(R.id.playerLabel)
        playerLabelWidth = playerLabel.width

        val totalLabel: TextView = findViewById(R.id.totalLabel)
        totalLabelWidth = totalLabel.width
        totalLabelHeight = totalLabel.height

        val ballLabel: TextView = findViewById(R.id.ballColorLabel)
        ballLabelWidth = ballLabel.width

        val horizontalScrollView: HorizontalScrollView = findViewById(R.id.horizontalScrollView)

        // Check to see if the player names have already been loaded
        val playerNameStackView: LinearLayout = findViewById(R.id.playerNameStackview)
        if (playerNameStackView.childCount == 0) {
            addTotalScoreStackView(intent.getIntExtra("numberOfPlayers", 1))
            addPlayerNames(intent.getIntExtra("numberOfPlayers", 1))
            addPlayerRows(intent.getIntExtra("numberOfPlayers", 1), numberOfHoles)
            addBallColors(intent.getIntExtra("numberOfPlayers", 1))
            populateParValues(numberOfHoles)
            populateHoleNumbers(numberOfHoles)

            // Update the SV
            if (holesCompleted != 0) {
                updateScrollViewOffSet(xValue, holesCompleted)
                Log.d("X_Position", "x-position = $xValue")
            }
            val horizontalScrollView: HorizontalScrollView = findViewById(R.id.horizontalScrollView)
            horizontalScrollView.smoothScrollTo(xValue, 0)
        }
       // playerNameStackviewHeight = playerNameStackView.height
        playerNameStackviewHeight = h * numberOfPlayers
        calculateTotalScore()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createViewRankingTable(gameDone: Boolean) {
        var viewRankingTable: TableLayout = findViewById(R.id.viewRankingTable)
        val font = resources.getFont(R.font.chalkboard_se_light_)

        if (gameDone == true) {
            // Clear table
            clearTable()
        }

        // Update the coursePar header
        val courseParHeaderLabel:TextView = findViewById(R.id.courseParHeaderLabel)
        if (coursePar == 0) {
            courseParHeaderLabel.text = "Course Par\n(TBD)"
        }
        else {
            courseParHeaderLabel.text = "Course Par\n($coursePar)"
        }


        // For each player, create a table row
        for (i in 0 until numberOfPlayers) {
            // Create the table row
            val tableRow = TableRow(this)
            tableRow.tag = i


            // Now create all of the textviews for each piece of info
            if (gameDone == false) {
                val rankTV = TextView(this)
                rankTV.setTextColor(Color.BLACK)
                rankTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                rankTV.gravity = Gravity.CENTER
                rankTV.typeface = font
                rankTV.minHeight = 100
                tableRow.addView(rankTV)
            }

            else {
                if (i < 3) {
                    val rankImageView = ImageView(this)
                    rankImageView.setBackgroundResource(R.drawable.view_ranking_cell_background)
                    rankImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    rankImageView.adjustViewBounds = true

                    rankImageView.maxHeight = 100
                    rankImageView.maxWidth = 100
                    rankImageView.setPadding(20)
                    tableRow.addView(rankImageView)
                }

                else {
                    val rankTV = TextView(this)
                    rankTV.setTextColor(Color.BLACK)
                    rankTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                    rankTV.gravity = Gravity.CENTER
                    rankTV.typeface = font
                    rankTV.minHeight = 100
                    tableRow.addView(rankTV)
                }
            }

            val playerTV = TextView(this)
                playerTV.setTextColor(Color.BLACK)
                playerTV.typeface = font
                playerTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                playerTV.gravity = Gravity.CENTER
                playerTV.minHeight = 100

            val scoreTV = TextView(this)
                scoreTV.setTextColor(Color.BLACK)
                scoreTV.typeface = font
                scoreTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                scoreTV.gravity = Gravity.CENTER
                scoreTV.minHeight = 100

            val strokesBehindTV = TextView(this)
                strokesBehindTV.setTextColor(Color.BLACK)
                strokesBehindTV.typeface = font
                strokesBehindTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                strokesBehindTV.gravity = Gravity.CENTER
                strokesBehindTV.minHeight = 100

            val courseParBehindTV = TextView(this)
                courseParBehindTV.setTextColor(Color.BLACK)
                courseParBehindTV.typeface = font
                courseParBehindTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                courseParBehindTV.gravity = Gravity.CENTER
                courseParBehindTV.minHeight = 100

            // Add these TVs to the table row
            tableRow.addView(playerTV)
            tableRow.addView(scoreTV)
            tableRow.addView(strokesBehindTV)
            tableRow.addView(courseParBehindTV)

            // Add this TableRow to the table
            viewRankingTable.addView(tableRow)
        }
    }

    private fun clearTable() {
        val viewRankingTable:TableLayout = findViewById(R.id.viewRankingTable)
        val childCount = viewRankingTable.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            viewRankingTable.removeViews(1, childCount - 1);
        }
    }
    private fun populatePlaceholderJSONScores() {
        // Convert the playerNamesJSON to string, and save it to shared pref
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        gameInProgress = sharedPref.getBoolean("gameInProgress", false)
        if (gameInProgress) {
            // Get the number of players
            numberOfPlayers = sharedPref.getInt("numberOfPlayers", 1)
            Log.d("PLAYER_SCORES", "Player Totals JSON = $playerTotalsJSON")

            // Get the players names and update the name tags
            playerNamesArray = Array(numberOfPlayers) {""}
            val strNamesJson = sharedPref.getString("playerNames", "")
            Log.d("PLAYER_TEXT_VIEW", "Size of player array = ${playerNamesArray.size}")

            if (strNamesJson != null) {
                try {
                    val response = JSONArray(strNamesJson)

                    // Now add the names to the name tags
                    for (i in 0 until numberOfPlayers) {
                        playerNamesArray[i] =response[i].toString()
                    }
                } catch (e: JSONException) {
                }
            }
        }
        else {
            numberOfPlayers = intent.getIntExtra("numberOfPlayers", 1)
            // Get the players names and update the name tags
            playerNamesArray = Array(numberOfPlayers) {""}

            for (i in 0 until numberOfPlayers) {
                val playerScoresJSON = JSONArray()
                for (j in 0..numberOfHoles - 1) {
                    playerScoresJSON.put(0)
                }
                playerTotalsJSON.put("player${i + 1}Totals", playerScoresJSON)


                // Add the player names default (e.g. "Player 1", "Player 2",..., "Player 9")
                playerNamesArray[i] = ""
            }

            editor.apply {
                putString("playerTotalScores", playerTotalsJSON.toString())
                Log.d("PLAYER_SCORES", "Player Totals JSON = $playerTotalsJSON")
                apply()
            }
        }
    }

    private fun updateGameNumber() {
        val sharedPref = getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        gameNumber += 1

        editor.apply {
            // 1. Save game in progress value
            putInt("game_Number", gameNumber)
            apply()
        }
    }

    private fun saveGame() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val sb = StringBuilder()
            for (i in 0 until playerNamesArray.size) {
                sb.append(playerNamesArray[i]).append(",")
            }

        editor.apply {
            // 1. Save game in progress value
                putBoolean("gameInProgress", gameInProgress)

            // 3. Add the player names JSON to the scorecard history JSON
            //gameCardJSON.put("player_Names", playerNamesJSON)
            putString("playerNames", sb.toString())
                // Convert the playerNamesJSON to string, and save it to shared pref
                //putString("playerNames", playerNamesJSON.toString())
                //Log.d("PLAYER_NAMES", "Player Names JSON = $playerNamesJSON")

            // Save date
            val myDate = Date()
            val dateFormat = SimpleDateFormat("dd MMM yyyy")

            // Save the string date
            date = dateFormat.format(myDate)

            // Add the date JSON to the scorecard history JSON
           // gameCardJSON.put("game_Date", dateJSON)
           // Log.d("DATE", "Date = ${stringDate}")

            // save # of players
            val numPlayersJSON = JSONArray()
            numPlayersJSON.put(numberOfPlayers)

            // Save # of holes
            putInt("numberOfHoles", numberOfHoles)

            // Add the number of players JSON to the scorecard history JSON
            gameCardJSON.put("number_Of_Players", numPlayersJSON)
            putInt("NUMBER_OF_PLAYERS", numberOfPlayers)
            Log.d("NUMBER_OF_PLAYERS", "Number of players = $numberOfPlayers")

            // Add the player scores totals to the scorecard history JSON
            gameCardJSON.put("player_Scores", playerTotalsJSON)

            // Add the location to the scorecard history JSON
            gameCardJSON.put("location", location)

            // Add the # of holes to the scorecard history JSON
            gameCardJSON.put("number_Of_Holes", numberOfHoles)

            // Add the winning player
            gameCardJSON.put("winning_Player", winningPlayer)

            // Add the winning score
            gameCardJSON.put("winning_Score", winningScore)

            // save the whole scorecard to the history collection
            gameHistoryJSON.put("game#${gameNumber}", gameCardJSON)

            apply() // asynchronously saves data, where as commit() synchronously saves, blocking main thread
        }

        editor.apply {
            // 1. Save game in progress value
            putBoolean("gameInProgress", gameInProgress)
            apply()
        }
        val testBool:Boolean = sharedPref.getBoolean("gameInProgress", false)
        Log.d("GAME_IN_PROGRESS", "$testBool")
    }

    private fun saveGameToHistory() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)

        // Get all of the properties to save to the gameModel
        val gameIDTS = gameNumber
        val locationTS = sharedPref.getString("location", "").toString()
        val dateTS = date
        val playerScoreArrayTS = playerTotalsJSON.toString()

        // Get the winning score + index of winning player
        val totalScoreArrayTS = getTotalScoreArray()
        val winningArray = determineWinningArray()
        val winningScoreTS = winningArray[0]
        val winningPlayerTS = playerNamesArray[winningArray[1]]



        val numberOfPlayersTS = numberOfPlayers
        val numberOfHolesTS = numberOfHoles
        val parValuesTS = parValuesJSON.toString()
        val colorValuesTS = colorValuesJSON.toString()

        // Get the names
        for (element in playerNamesArray) {
            playerNamesJSON.put(element)
        }

        val playerNamesTS = playerNamesJSON.toString()

        // Create a game model as a JSON Object
        val gameDetailsTS = JSONObject()
        gameDetailsTS.put("gameId", gameIDTS)
        gameDetailsTS.put("location", locationTS)
        gameDetailsTS.put("numberOfPlayers", numberOfPlayersTS)
        gameDetailsTS.put("winningScore", winningScoreTS)
        gameDetailsTS.put("winningPlayer", winningPlayerTS)
        gameDetailsTS.put("date", dateTS)
        gameDetailsTS.put("totalScoreArray", totalScoreArrayTS)
        gameDetailsTS.put("playerNames", playerNamesTS)
        gameDetailsTS.put("numberOfHoles", numberOfHolesTS)
        gameDetailsTS.put("parValuesArray", parValuesTS)
        gameDetailsTS.put("playerScoreArray", playerScoreArrayTS)
        gameDetailsTS.put("golfBallColorArray", colorValuesTS)

        // Save the JSON Object to a JSON Text File
        val userString: String = gameDetailsTS.toString()

        // Save this textFile to internal storage
        val file = "game_${gameIDTS}"
        val data:String = userString
        val fileOutputStream:FileOutputStream

        try {
            fileOutputStream = openFileOutput(file, android.content.Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()

            // Update the # of completed games
            addToCompletedGameTracker()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun addToCompletedGameTracker() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Get the number of players
        var numGamesCompleted = sharedPref.getInt("numGamesCompleted", 0)

        // Increment the game #
        numGamesCompleted += 1

        editor.apply {
            // Save the number of games completed
            putInt("numGamesCompleted",numGamesCompleted)
            apply()
        }
    }

    private fun getTotalScoreArray(): String {
        val totalScoresJSON = JSONArray()

        for (i in 0 until numberOfPlayers) {
            var totalScore = 0

            when (i) {
                0 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player1Totals[j]
                    }
                }
                1 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player2Totals[j]
                    }
                }
                2 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player3Totals[j]
                    }
                }
                3 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player4Totals[j]
                    }
                }
                4 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player5Totals[j]
                    }
                }
                5 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player6Totals[j]
                    }
                }
                6 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player7Totals[j]
                    }
                }
                7 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player8Totals[j]
                    }
                }
                8 -> {
                    for (j in 0..numberOfHoles - 1) {
                        totalScore += player9Totals[j]
                    }
                }
            }
            // Add the total score to the JSON array
            totalScoresJSON.put(totalScore)
            totalScoreArray[i] = totalScore
        }

        return totalScoresJSON.toString()
    }

    private fun determineWinningArray(): IntArray {
        val arrayToReturn = IntArray(2) { 0 }
        var winningScore = 3000
        var winningIndex = 0

        // Loop through each total score, and determine which is the lowest
        for (i in 0 until numberOfPlayers) {
            if (totalScoreArray[i] < winningScore) {
                winningScore = totalScoreArray[i]
                winningIndex = i
            }
        }

        arrayToReturn[0] = winningScore
        arrayToReturn[1] = winningIndex
        return arrayToReturn
    }

    private fun saveXValue() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the xPosition
            putInt("xPosition", xValue)
            apply()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun populateParValues(numberOfHoles: Int) {
        val parvalueCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        testWidth = parvalueCellWidth
        val parValueRow: TableRow = findViewById(R.id.parValueRow)
        val totalLabel: TextView = findViewById(R.id.totalLabel)
        val font = resources.getFont(R.font.chalkboard_se_bold_)
        for (i in 1..numberOfHoles) {
            val parValueButton = Button(this)
            parValueButton.isAllCaps = false
            parValueButton.typeface = font
            parValueButton.setTextColor(Color.parseColor("#ff0000"))
            parValueButton.setBackgroundResource(R.drawable.button_background)
            parValueButton.tag = i
            parValueButton.textSize = 14F
            parValueButton.setPadding(0)
            parValueButton.setLayoutParams(TableRow.LayoutParams(parvalueCellWidth, 75))

            // populate the text
            if (gameInProgress == false  || parArray[i - 1] == 0) {
                parValueButton.text = ("Enter Par")
                parValueButton.setTextColor(Color.RED)
                parValueButton.setBackgroundResource(R.drawable.button_background)
            }
            else {
                parValueButton.text = "Par: ${parArray[i - 1]}"
                parValueButton.setTextColor(Color.BLACK)
                parValueButton.setBackgroundResource(R.drawable.par_entered_background)
            }
            parValueButton.setOnClickListener {
                // Bring up either the par update confirmation view, or the enter par view
                if (parValueButton.text == "Enter Par") {
                    // Bring up the enter par view
                    val enterParView: FrameLayout = findViewById(R.id.enterParView)
                    enterParView.alpha = 1.0F
                    enterParView.isVisible = true

                    // Set the par reminder label
                    val parReminderLabel:TextView = findViewById(R.id.enterParHoleReminderLabel)
                    parReminderLabel.text = "Enter par for hole $i"

                    // Set the current hole
                    currentHole = i

                    // Dismiss the view ranking button and end game button
                    val viewRankingButton: Button = findViewById(R.id.viewRankingButton)
                    val endGameEarlyButton: Button = findViewById(R.id.endGameButton)

                    viewRankingButton.alpha = 0.0F
                    viewRankingButton.isVisible = false
                    endGameEarlyButton.alpha = 0.0F
                    endGameEarlyButton.isVisible = false

                    disableScoreCells()
                }

                else {
                    // Bring up the par update confirmation view
                    val parUpdateConfirmationView: FrameLayout = findViewById(R.id.parUpdateConfirmationView)
                    parUpdateConfirmationView.alpha = 1.0F
                    parUpdateConfirmationView.isVisible = true

                    // Dismiss the view ranking button and end game button
                    val viewRankingButton: Button = findViewById(R.id.viewRankingButton)
                    val endGameEarlyButton: Button = findViewById(R.id.endGameButton)

                    viewRankingButton.alpha = 0.0F
                    viewRankingButton.isVisible = false
                    endGameEarlyButton.alpha = 0.0F
                    endGameEarlyButton.isVisible = false
                }
            }

            // Add this hole label to the tableRow
            parValueRow.addView(parValueButton)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun populateHoleNumbers(numberOfHoles: Int) {
        val holeNumberCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        testWidth = holeNumberCellWidth
        val holeNumberRow: TableRow = findViewById(R.id.holeNumberRow)
        val font = resources.getFont(R.font.chalkboard_se_bold_)
        for (i in 1..numberOfHoles) {
            val holeNumberLabel = TextView(this)

            holeNumberLabel.text = ("Hole $i")
            holeNumberLabel.typeface = font
            holeNumberLabel.setTextColor(Color.parseColor("#000000"))
            holeNumberLabel.setBackgroundResource(R.drawable.hole_label_background)
            holeNumberLabel.width = holeNumberCellWidth
            holeNumberLabel.height = 100
            holeNumberLabel.gravity = android.view.Gravity.CENTER

            // Add this hole label to the tableRow
            holeNumberRow.addView(holeNumberLabel)
        }
    }

    private var myNum = 0
    private fun addPlayerRows(numberOfPlayers: Int, numberOfHoles: Int) {
        val horizontalSV: HorizontalScrollView = findViewById(R.id.horizontalScrollView)
        val totalLabel: TextView = findViewById(R.id.totalLabel)
        totalLabelHeight = totalLabel.height

        val scoreCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        val player1ScoreRow: TableRow = findViewById(R.id.player1ScoreRow)
        val player2ScoreRow: TableRow = findViewById(R.id.player2ScoreRow)
        val player3ScoreRow: TableRow = findViewById(R.id.player3ScoreRow)
        val player4ScoreRow: TableRow = findViewById(R.id.player4ScoreRow)
        val player5ScoreRow: TableRow = findViewById(R.id.player5ScoreRow)
        val player6ScoreRow: TableRow = findViewById(R.id.player6ScoreRow)
        val player7ScoreRow: TableRow = findViewById(R.id.player7ScoreRow)
        val player8ScoreRow: TableRow = findViewById(R.id.player8ScoreRow)
        val player9ScoreRow: TableRow = findViewById(R.id.player9ScoreRow)

        // Put all the player rows in an array
        val placeholderArray: ArrayList<TableRow> = ArrayList()
        placeholderArray.add(player1ScoreRow)
        placeholderArray.add(player2ScoreRow)
        placeholderArray.add(player3ScoreRow)
        placeholderArray.add(player4ScoreRow)
        placeholderArray.add(player5ScoreRow)
        placeholderArray.add(player6ScoreRow)
        placeholderArray.add(player7ScoreRow)
        placeholderArray.add(player8ScoreRow)
        placeholderArray.add(player9ScoreRow)

        var completedHoleArray = IntArray(numberOfHoles) {0}
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val strPlayerScoresJson = sharedPref.getString("playerTotalScores", "")

        for (i in 1..numberOfPlayers) {
            // Now add buttons for each hole for that player
            for (j in 1..numberOfHoles) {
                val playerScoreButton = Button(this)
                var playerTag:String
                //playerScoreButton.height = totalLabelHeight - 25
                playerScoreButton.width = scoreCellWidth
                playerScoreButton.minWidth = 0
                playerScoreButton.textSize = 11F
                playerScoreButton.maxWidth = 40
                if (strPlayerScoresJson == "") {
                    playerScoreButton.setBackgroundResource(R.drawable.empty_scorecell_background)
                }
                else {
                    var obtainedScore = 0
                    // get the correct score
                    when (i) {
                        1 -> {
                            obtainedScore = player1Totals[j - 1]
                        }
                        2 -> {
                            obtainedScore = player2Totals[j - 1]
                        }
                        3 -> {
                            obtainedScore = player3Totals[j - 1]
                        }
                        4 -> {
                            obtainedScore = player4Totals[j - 1]
                        }
                        5 -> {
                            obtainedScore = player5Totals[j - 1]
                        }
                        6 -> {
                            obtainedScore = player6Totals[j - 1]
                        }
                        7 -> {
                            obtainedScore = player7Totals[j - 1]
                        }
                        8 -> {
                            obtainedScore = player8Totals[j - 1]
                        }
                        9 -> {
                            obtainedScore = player9Totals[j - 1]
                        }
                    }

                    if (obtainedScore != 0) {
                        playerScoreButton.text = obtainedScore.toString()
                    }

                    if (obtainedScore == 0) {
                        playerScoreButton.setBackgroundResource(R.drawable.empty_scorecell_background)
                        playerScoreButton.setTextColor(Color.BLACK)
                        }
                    if (obtainedScore == 1) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_1_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                        }
                    if (obtainedScore == 2) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_2_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                    }
                    if (obtainedScore == 3) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_3_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                    }
                    if (obtainedScore == 4) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_4_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                    }
                    if (obtainedScore == 5) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_5_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                    }
                    if (obtainedScore == 6) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_6_background_cell)
                        playerScoreButton.setTextColor(Color.BLACK)
                    }
                    if (obtainedScore > 6) {
                        playerScoreButton.setBackgroundResource(R.drawable.score_7_and_up_background)
                        playerScoreButton.setTextColor(Color.WHITE)
                    }

                }
                playerScoreButton.gravity = android.view.Gravity.CENTER
                playerScoreButton.setTag(R.id.player_num, i)
                playerScoreButton.setTag(R.id.player_hole, j)

                // Get the ID of the cell
                playerTag = "${i}${j}"

                try {
                    myNum = playerTag.toInt()
                } catch (nfe: NumberFormatException) {
                    println("Could not parse $nfe")
                }
                playerScoreButton.id = myNum

                // add an event listener to the button
                playerScoreButton.setOnClickListener {

                    // Set the current cell
                    currentPlayer = playerScoreButton.getTag(R.id.player_num) as Int
                    currentHole = playerScoreButton.getTag(R.id.player_hole) as Int
                    println("Current Player: $currentPlayer")
                    println("Current Hole: $currentHole")

                    // If the cell is empty, prompt the enterScoreView
                    // Get the ID of the cell
                    playerTag = "$currentPlayer$currentHole"

                    try {
                        myNum = playerTag.toInt()
                        println("Player ID: $myNum")
                    } catch (nfe: NumberFormatException) {
                        println("Could not parse $nfe")
                    }
                    val scoreCell: Button = findViewById(myNum)
                    if (scoreCell.text == "") {
                        presentEnterScoreView()
                    }
                    else {
                        // Present the "do you want to overwrite the cell" label
                        presentOverwriteView()
                    }
                }
                // This is to make the weight of an element "1" so that it is evenly spaced in a linear layout
                val param = LinearLayout.LayoutParams(
                    android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                    android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                    1.0f
                )

                when (i) {
                    1 -> {

                        player1ScoreRow.layoutParams = param

                        // player1ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)

                         player1ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                        //player1ScoreRow.addView(playerScoreButton)
                    }
                    2 -> {
                       // player2ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player2ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                      //  player2ScoreRow.layoutParams = param
                    }
                    3 -> {
                       // player3ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player3ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                        //player3ScoreRow.layoutParams = param
                    }
                    4 -> {
                       // player4ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player4ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                       // player4ScoreRow.layoutParams = param
                    }
                    5 -> {
                        //player5ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player5ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                       // player5ScoreRow.layoutParams = param
                    }
                    6 -> {
                        //player6ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player6ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                       // player6ScoreRow.layoutParams = param
                    }
                    7 -> {
                        //player7ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player7ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                        //player7ScoreRow.layoutParams = param
                    }
                    8 -> {
                        //player8ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player8ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                        //player8ScoreRow.layoutParams = param
                    }
                    9 -> {
                        //player9ScoreRow.addView(playerScoreButton, scoreCellWidth, playerNameStackviewHeight / numberOfPlayers)
                        player9ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                        //player9ScoreRow.layoutParams = param
                    }
                }

                // Hide the rows the are not needed
                val nonPlayerStart = numberOfPlayers + 1

                if (nonPlayerStart == 10) {
                    // Do nothing
                }
                else {
                    for(k in nonPlayerStart..9) {
                        placeholderArray[k - 1].alpha = 0F
                        placeholderArray[k - 1].isVisible = false
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun addPlayerNames(numberOfPlayers: Int) {
        val totalScoreStackView: LinearLayout = findViewById(R.id.totalScoreStackview)
        val playerNameStackView: LinearLayout = findViewById(R.id.playerNameStackview)
        val font = resources.getFont(R.font.chalkboard_se_light_)

        playerNameStackView.orientation = LinearLayout.VERTICAL

        // This is to make the weight of an element "1" so that it is evenly spaced in a linear layout
        val param = LinearLayout.LayoutParams(
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            1.0f
        )

        // Add textview for each player
        for (i in 0 until numberOfPlayers) {
            val playerNameTextView = EditText(this)
            playerNameTextView.width = playerLabelWidth
            //playerNameTextView.height = totalLabelHeight - 25
            playerNameTextView.typeface = font
            playerNameTextView.textSize = 12F
            playerNameTextView.maxLines = 1
            playerNameTextView.inputType = (InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            playerNameTextView.isCursorVisible = false

            playerNameTextView.setText(playerNamesArray[i])

            if (playerNameTextView.text.toString() == "") {
                playerNameTextView.hint = "Player ${i + 1}"
                playerNamesArray[i] = "Player ${i + 1}"
                playerNameTextView.setHintTextColor(Color.parseColor("#AAAAAA"))
            }

            else {
                playerNameTextView.setTextColor(Color.WHITE)
            }

            playerNameTextView.gravity = android.view.Gravity.CENTER
            playerNameTextView.layoutParams = param
            playerNameTextView.setBackgroundResource(R.drawable.player_name_textview_background)
            playerNameTextView.isEnabled = true
            playerNameTextView.tag = i

            // Add a listener
            playerNameTextView.addTextChangedListener {
                //saveNames(playerNamesArray, playerNameTextView, playerNameTextView.tag as Int)
                Log.d("PLAYER_TEXT_VIEW", "Change Listener Heard")
                Log.d("PLAYER_TEXT_VIEW", "Tag = ${playerNameTextView.tag as Int}")
                Log.d("PLAYER_TEXT_VIEW", "Player Name = ${playerNameTextView.text}")
                Log.d("PLAYER_TEXT_VIEW", "Size of player array = ${playerNamesArray.size}")

                if (playerNameTextView.text.toString() == "") {
                    playerNameTextView.hint = "Player ${i + 1}"
                    playerNamesArray[i] = "Player ${i + 1}"
                    playerNameTextView.setHintTextColor(Color.parseColor("#AAAAAA"))
                }

                else {
                    playerNameTextView.setTextColor(Color.WHITE)
                }
                playerNamesArray[playerNameTextView.tag as Int] = playerNameTextView.text.toString()

                for (j in 1..numberOfPlayers) {
                    Log.d("PLAYER_TEXT_VIEW", "Player $j  = ${playerNamesArray[j - 1]}")
                }
                // Save the game so the player names are saved
                saveGame()
            }

            playerNameStackView.addView(playerNameTextView)
        }
        //playerNameStackviewHeight = playerNameStackView.height
        playerNameStackviewHeight = h * numberOfPlayers

    }

    fun addBallColors(numberOfPlayers: Int) {
        val ballColorStackView: LinearLayout = findViewById(R.id.ballColorStackview)
        ballColorStackView.orientation = LinearLayout.VERTICAL

        // This is to make the weight of an element "1" so that it is evenly spaced in a linear layout
        val param = LinearLayout.LayoutParams(
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            1.0f
        )

        // Add button for each player
        for (i in 0 until numberOfPlayers) {
            val ballImageButton = ImageButton(this)
            val ballShadeBackground = TextView(this)

            ballImageButton.layoutParams = param
            ballImageButton.setImageResource(R.drawable.transparent)
            ballImageButton.scaleType = ImageView.ScaleType.FIT_XY
            ballImageButton.setPadding(0)
            ballImageButton.tag = i

            // Update the default color
            defaultBallColor = ContextCompat.getColor(this, R.color.white);

            // populate the text
            if (gameInProgress == false  || golfBallColorArray[i] == 0) {
                ballImageButton.setBackgroundColor(Color.WHITE)
            }
            else {
                ballImageButton.setBackgroundColor(golfBallColorArray[i])
            }

            ballImageButton.setOnClickListener() {
                // Set the ball that we are updating
                ballColorToUpdate = i

                openColorPicker()
            }

            ballColorStackView.addView(ballImageButton)
        }
    }

    private fun openColorPicker() {
        // Get the ballcolor stackview
        val ballColorSV:LinearLayout = findViewById(R.id.ballColorStackview)

        val ambilWarnaDialog = AmbilWarnaDialog(this, defaultBallColor, object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {

            }

            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                val ballToUpdate:ImageButton = ballColorSV.getChildAt(ballColorToUpdate) as ImageButton
                ballToUpdate.setBackgroundColor(color)

                // Save the golfball colors
                saveGolfBallColors(ballColorToUpdate, color)
            }
        })
        ambilWarnaDialog.show()
    }

    private fun saveGolfBallColors(ballToUpdate: Int, colorToSave: Int) {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Update the ball color array
        golfBallColorArray[ballToUpdate] = colorToSave

        editor.apply {

            // First, clear the JSON
            colorValuesJSON = JSONArray()

            // Second, add the colors to the JSON
            for (i in 0..numberOfPlayers - 1) {
                colorValuesJSON.put(golfBallColorArray[i])
            }

            // Add the number of players JSON to the scorecard history JSON
            gameCardJSON.put("color_array", colorValuesJSON)
            putString("colorValues", colorValuesJSON.toString())

            // save the whole scorecard to the history collection
            gameHistoryJSON.put("game#${gameNumber}", gameCardJSON)

            apply() // asynchronously saves data, where as commit() synchronously saves, blocking main thread
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTotalScoreStackView(numberOfPlayers: Int) {
        val font = resources.getFont(R.font.chalkboard_se_bold_)
        val totalScoreStackView: LinearLayout = findViewById(R.id.totalScoreStackview)
        totalScoreStackView.orientation = LinearLayout.VERTICAL

        // This is to make the weight of an element "1" so that it is evenly spaced in a linear layout
        val param = LinearLayout.LayoutParams(
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            android.app.ActionBar.LayoutParams.WRAP_CONTENT,
            1.0f
        )

        // Add textview for each player
        for (i in 1..numberOfPlayers) {
            val playerTotalScore = Button(this)
            playerTotalScore.text = ("0")
            playerTotalScore.textSize = 12F
            playerTotalScore.typeface = font
            playerTotalScore.layoutParams = param
            playerTotalScore.gravity = android.view.Gravity.CENTER
            playerTotalScore.setBackgroundResource(R.drawable.player_totalscore_background)
            playerTotalScore.id = 900 + i
            totalScoreStackView.addView(playerTotalScore)
        }
    }
    @SuppressLint("ResourceType")
    private fun calculateTotalScore() {
        for (i in 0 until numberOfPlayers) {
            if (i == 0) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player1Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(901)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]

                // Add the totalScore to the totalScore array
            }

            else if (i == 1) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player2Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(902)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 2) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player3Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(903)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 3) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player4Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(904)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 4) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player5Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(905)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 5) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player6Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(906)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 6) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player7Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(907)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 7) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player8Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(908)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }

            else if (i == 8) {
                var totalScore = 0

                for (j in 0..numberOfHoles - 1) {
                    val score = player9Totals[j]
                    totalScore += score
                }
                val totalScoreLabel: Button = findViewById(909)
                totalScoreLabel.text = "$totalScore"

                // Determine if the total score is less than the current winning score
                if ((totalScore < winningScore) || (winningScore == 0)) {
                    winningScore = totalScore
                    winningPlayer = playerNamesArray[i]
                }
            }
        }
    }
    private fun loadGame() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        gameInProgress = sharedPref.getBoolean("gameInProgress", false)

        // Get the game #
        gameNumber = sharedPref.getInt("game_Number",0)

        if (gameInProgress) {
            // Get the number of players
            numberOfPlayers = sharedPref.getInt("numberOfPlayers", 1)
            Log.d("NUMBER_OF_PLAYERS", "Number of players = $numberOfPlayers")

            // Get the location
            location = sharedPref.getString("location", "").toString()

            // Get the players names and update the name tags
            playerNamesArray = Array(numberOfPlayers) {""}

            // Get the number of Holes
            numberOfHoles = sharedPref.getInt("numberOfHoles", 18)

            // populate the Par Array
            val strParValuesJSON = sharedPref.getString("parValues", "")
            if (strParValuesJSON != null) {
                try {
                    val response = JSONArray(strParValuesJSON)
                    //val tempParArray = response.getJSONArray("par_array")
                    for (j in 0..numberOfHoles - 1) {
                        val par = Integer.parseInt(response[j].toString())
                        parArray[j] = par
                    }
                } catch (e: JSONException) {
                    Log.d("JSON_EXCEPTION", "Idk why it didn't work")
                }
            }

            // Update course Par
            updateCoursePar()

            // Populate the colorArray
            val strColorsValuesJSON = sharedPref.getString("colorValues", "")
            if (strColorsValuesJSON != null) {
                try {
                    val response = JSONArray(strColorsValuesJSON)
                    for (j in 0..numberOfPlayers - 1) {
                        val colorInt = Integer.parseInt(response[j].toString())
                        golfBallColorArray[j] = colorInt
                    }
                } catch (e: JSONException) {
                    Log.d("JSON_EXCEPTION", "Idk why it didn't work")
                }
            }

            var playerNamesString = sharedPref.getString("playerNames", "").toString()
            val tokens = StringTokenizer(playerNamesString, ",")

            for (i in 0 until numberOfPlayers) {
                playerNamesArray[i] = tokens.nextToken()
            }
            //val strNamesJson = sharedPref.getString("playerNames", "")

            Log.d("PLAYER_TEXT_VIEW", "Size of player array = ${playerNamesArray.size}")

            // Get the players scores and update the scorecard
            val strPlayerScoresJson = sharedPref.getString("playerTotalScores", "")
            Log.d("PLAYER_SCORES", "Player Scores = $strPlayerScoresJson")
            if (strPlayerScoresJson != null) {
                try {
                    val response = JSONObject(strPlayerScoresJson)
                    // Now add the scores to the player totals
                    for (i in 0 until numberOfPlayers) {
                        when (i) {
                            0 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    val score = Integer.parseInt(scoreArray[j].toString())
                                    player1Totals[j] = score
                                    tempScore += score
                                }
                                totalScoreArray[i] = tempScore
                            }
                            1 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player2Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            2 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player3Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            3 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player4Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            4 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player5Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            5 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player6Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            6 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player7Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            7 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player8Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                            8 -> {
                                val scoreArray = response.getJSONArray("player${i + 1}Totals")
                                var tempScore = 0
                                for (j in 0..numberOfHoles - 1) {
                                    player9Totals[j] = Integer.parseInt(scoreArray[j].toString())
                                    tempScore += Integer.parseInt(scoreArray[j].toString())
                                }
                                totalScoreArray[i] = tempScore
                            }
                        }
                    }

                } catch (e: JSONException) {
                    Log.d("JSON_EXCEPTION", "Idk why it didn't work")
                }
            }
            // Now, update the completed holes array
            for (i in 1..numberOfHoles) {
                updateCompletedHoleTracker(i)
            }
            // Get the x-position
            xValue = sharedPref.getInt("xPosition", 0)
            Log.d("X_VALUE", "X-Position = $xValue")

            sortTotalScores()

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun configureButtons() {
        val font = resources.getFont(R.font.chalkboard_se_light_)

        // Set up the end game early button
        val endGameButton: Button = findViewById(R.id.endGameButton)
        endGameButton.setOnClickListener {
            // Prompt the endgame early view
            presentEndGameEarlyView()
        }

        // Set up the "don't end game early" button
        val doNotEndGameButton: Button = findViewById(R.id.noEndGameButton)
        doNotEndGameButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEndGameEarlyView()
        }

        // Set up the "don't end game early" button
        val doEndGameButton: Button = findViewById(R.id.yesEndGameButton)
        doEndGameButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEndGameEarlyView()

            // Present the save to history confirmation view
            presentSaveToHistoryConfirmationView()
        }

        // Set up the exit scoreView button
        val exitScoreViewButton: Button = findViewById(R.id.exitScoreViewButton)
        exitScoreViewButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEnterScoreView()

            // Change the ouch label back to 7
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextView)
            ouchScoreTextView.text = "7"
        }

        // Set up the score 1 entered button
        val score1Button: ImageButton = findViewById(R.id.score1Button)
        score1Button.setOnClickListener {
            // Make the cell purple

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score1Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_1_background_cell)
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.typeface = font
            scoreCell.text = "1"
            // Add the score to the totalPlayerScore array
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 1)
            calculateTotalScore()

        }

        // Set up the score 2 entered button
        val score2Button: ImageButton = findViewById(R.id.score2Button)
        score2Button.setOnClickListener {
            // Make the cell blue

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score2Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_2_background_cell)
            scoreCell.typeface = font
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.text = "2"
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 2)
            calculateTotalScore()

        }

        // Set up the score 3 entered button
        val score3Button: ImageButton = findViewById(R.id.score3Button)
        score3Button.setOnClickListener {
            // Make the cell green

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score3Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_3_background_cell)
            scoreCell.typeface = font
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.text = "3"
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 3)
            calculateTotalScore()

        }

        // Set up the score 4 entered button
        val score4Button: ImageButton = findViewById(R.id.score4Button)
        score4Button.setOnClickListener {
            // Make the cell yellow

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score4Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_4_background_cell)
            scoreCell.typeface = font
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.text = "4"
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 4)
            calculateTotalScore()

        }

        // Set up the score 5 entered button
        val score5Button: ImageButton = findViewById(R.id.score5Button)
        score5Button.setOnClickListener {
            // Make the cell orange

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score5Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_5_background_cell)
            scoreCell.typeface = font
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.text = "5"
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 5)
            calculateTotalScore()

        }

        // Set up the score 5 entered button
        val score6Button: ImageButton = findViewById(R.id.score6Button)
        score6Button.setOnClickListener {
            // Make the cell red

            // Get the ID of the cell
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            score6Button.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.score_6_background_cell)
            scoreCell.typeface = font
            scoreCell.setTextColor(Color.BLACK)
            scoreCell.text = "6"
            dismissEnterScoreView()
            addToTotalScoreArray(currentPlayer, currentHole, 6)
            calculateTotalScore()

        }

        // Set up the clear score button
        val clearScoreButton: Button = findViewById(R.id.clearScoreButton)
        clearScoreButton.setOnClickListener {
            // Make the cell blank

            // Get the ID of the cell we wish to clear
            val playerTag = "${currentPlayer}${currentHole}"

            try {
                myNum = playerTag.toInt()
                println("Player ID: $myNum")
            } catch (nfe: NumberFormatException) {
                println("Could not parse $nfe")
            }

            clearScoreButton.tag
            val scoreCell: Button = findViewById(myNum)
            scoreCell.setBackgroundResource(R.drawable.empty_scorecell_background)
            scoreCell.typeface = font
            scoreCell.text = ""
            dismissEnterScoreView()
            completedHoleArray[currentHole - 1] = 0
            addToTotalScoreArray(currentPlayer, currentHole, 0)
            calculateTotalScore()

        }

        // Set up the ouch button
        val ouchButton: Button = findViewById(R.id.ouchButton)
        ouchButton.setOnClickListener {
            // Present the ouch scoreView
            val ouchScoreEntrySubview: androidx.constraintlayout.widget.ConstraintLayout =
                findViewById(R.id.ouchConstraintLayout)

            // Dismiss the other buttons
            score1Button.alpha = 0.0F
            score2Button.alpha = 0.0F
            score3Button.alpha = 0.0F
            score4Button.alpha = 0.0F
            score5Button.alpha = 0.0F
            score6Button.alpha = 0.0F
            clearScoreButton.alpha = 0.0F
            ouchButton.alpha = 0.0F

            ouchScoreEntrySubview.alpha = 1.0F
            ouchScoreEntrySubview.isVisible = true
        }

        // Set up the plus ouch button
        val plusOuchButton: Button = findViewById(R.id.plusOuchButton)
        plusOuchButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextView)

            // Get the value of the textview
            var ouchScore:Int = Integer.parseInt(ouchScoreTextView.text.toString())

            // Make sure the value isn't 99
            if (ouchScore < 99) {
                ouchScore += 1
            }

            // Update the ouch score Textview
            ouchScoreTextView.text = "$ouchScore"
        }

        // Set up the minus ouch button
        val minusOuchButton: Button = findViewById(R.id.minusOuchButton)
        minusOuchButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextView)

            // Get the value of the textview
            var ouchScore:Int = Integer.parseInt(ouchScoreTextView.text.toString())

            // Make sure the value isn't 99
            if (ouchScore > 1) {
                ouchScore -= 1
            }

            // Update the ouch score Textview
            ouchScoreTextView.text = "$ouchScore"
        }

        // Set up the minus ouch button
        val acceptOuchScoreButton: Button = findViewById(R.id.enterOuchScoreButton)
        acceptOuchScoreButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextView)

            // Get the value of the textview
            val ouchScore:Int = Integer.parseInt(ouchScoreTextView.text.toString())

            // Set this value to the scorecard
                // Get the ID of the cell
                val playerTag = "$currentPlayer$currentHole"

                try {
                    myNum = playerTag.toInt()
                    println("Player ID: $myNum")
                } catch (nfe: NumberFormatException) {
                    println("Could not parse $nfe")
                }

                val scoreCell: Button = findViewById(myNum)

            if (ouchScore == 1) {
                scoreCell.setBackgroundResource(R.drawable.score_1_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore == 2) {
                scoreCell.setBackgroundResource(R.drawable.score_2_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore == 3) {
                scoreCell.setBackgroundResource(R.drawable.score_3_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore == 4) {
                scoreCell.setBackgroundResource(R.drawable.score_4_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore == 5) {
                scoreCell.setBackgroundResource(R.drawable.score_5_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore == 6) {
                scoreCell.setBackgroundResource(R.drawable.score_6_background_cell)
                scoreCell.setTextColor(Color.BLACK)
            }
            if (ouchScore > 6) {
                scoreCell.setBackgroundResource(R.drawable.score_7_and_up_background)
                scoreCell.setTextColor(Color.WHITE)

                if (alreadyMercy == 0) {
                    noMercy()
                }
            }
                scoreCell.typeface = font
                scoreCell.text = "$ouchScore"
                dismissEnterScoreView()
                addToTotalScoreArray(currentPlayer, currentHole, ouchScore)
                calculateTotalScore()

            // Update the ouch score Textview back to 7
            ouchScoreTextView.text = "7"
        }

        // Set up the do update score button
        val doUpdateScoreButton: Button = findViewById(R.id.doUpdateScore)
        doUpdateScoreButton.setOnClickListener {
            // Dismiss the overwrite confirmation view
            dismissOverwriteView()

            // Prompt the endgame early view
            presentEnterScoreView()

        }

        // Set up the do not update score button
        val doNotUpdateScoreButton: Button = findViewById(R.id.doNotUpdateScore)
        doNotUpdateScoreButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissOverwriteView()
        }

        // Set up the do not save game to history button
        val doNotSaveGameToHistoryButton: Button = findViewById(R.id.doNotSaveGameToHistoryButton)
        doNotSaveGameToHistoryButton.setOnClickListener {

            // Set the game in progress to false
            gameInProgress = false
            // Save the game to your history
            saveGame()

            // Go to the main menu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up the do save game to history button
        val doSaveGameToHistoryButton: Button = findViewById(R.id.doSaveGameToHistoryButton)
        doSaveGameToHistoryButton.setOnClickListener {

            // Present the final score screen
            val finalTable: RelativeLayout = findViewById(R.id.viewRankingView)
            val podiumDonebutton: Button = findViewById(R.id.podiumDoneButton)
            val saveGameToHistoryConfirmationView:FrameLayout = findViewById(R.id.saveGameToHistoryConfirmationView)

            createViewRankingTable(true)
            sortTotalScores()
            calculateTotalScore()
            populateViewRankingTable(true)
            finalTable.alpha = 1.0F
            finalTable.isVisible = true

            podiumDonebutton.alpha = 1.0F
            podiumDonebutton.isVisible = true

            saveGameToHistoryConfirmationView.alpha = 0.0F

            // Set the game in progress to false
            gameInProgress = false

            // Increment the game #
            updateGameNumber()

            // Save the game to your history
            saveGame()
            saveGameToHistory()
        }

        // Set up the editScore button
        val editScoreButton: Button = findViewById(R.id.editScoreButton)
        editScoreButton.setOnClickListener {
            // Dismiss the game done view
            dismissGameDoneView()
            presentEndGameButton()
        }

        // Set up the game done button
        val gameDoneButton: Button = findViewById(R.id.gameDoneButton)
        gameDoneButton.setOnClickListener {
            val viewRankingButton: Button = findViewById(R.id.viewRankingButton)
            val ballColorLabel: TextView = findViewById(R.id.ballColorLabel)
            val playerLabel: TextView = findViewById(R.id.playerLabel)
            val totalLabel: TextView = findViewById(R.id.totalLabel)
            val horizontalScrollView: HorizontalScrollView = findViewById(R.id.horizontalScrollView)
            val ballColorSV: LinearLayout = findViewById(R.id.ballColorStackview)
            val playerNameSV: LinearLayout = findViewById(R.id.playerNameStackview)
            val totalScoreSV: LinearLayout = findViewById(R.id.totalScoreStackview)
            dismissGameDoneView()
            viewRankingButton.alpha = 0.0F
            viewRankingButton.isEnabled = false
            endGameButton.alpha = 0.0F
            endGameButton.isEnabled = false

            ballColorLabel.alpha = 0.0F
            playerLabel.alpha = 0.0F
            totalLabel.alpha = 0.0F
            horizontalScrollView.alpha = 0.0F
            horizontalScrollView.isEnabled = false
            ballColorSV.alpha = 0.0F
            playerNameSV.alpha = 0.0F
            totalScoreSV.alpha = 0.0F


            if (holesCompleted > 14) {
                presentInterstitial()
            }

            // Present the final score screen
            val finalTable: RelativeLayout = findViewById(R.id.viewRankingView)
            val podiumDonebutton: Button = findViewById(R.id.podiumDoneButton)

            createViewRankingTable(true)
            populateViewRankingTable(true)
            finalTable.alpha = 1.0F
            finalTable.isVisible = true

            podiumDonebutton.alpha = 1.0F
            podiumDonebutton.isVisible = true

            checkCourseParAchievements()

            // Increment the game number
            updateGameNumber()

            // Set the game in progress to false
            gameInProgress = false
            saveGame()

            // Save all elements
            saveGameToHistory()
        }

        // Set up the "done with podium" button
        val doneWithPodiumButton: Button = findViewById(R.id.podiumDoneButton)
        doneWithPodiumButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up the "viewRanking" button
        val viewRankingButton: Button = findViewById(R.id.viewRankingButton)
        val playerStandings: RelativeLayout = findViewById(R.id.viewRankingView)
        viewRankingButton.setOnTouchListener() { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // button pressed
                Log.d("button pressed", "It's pressed")

                // Dim the button

                // Sort the total scores
                sortTotalScores()
                // Populate the table
                populateViewRankingTable(false)

                // Present the table
                playerStandings.alpha = 1.0F
                playerStandings.isVisible = true

                // get rid of the view ranking button
                viewRankingButton.alpha = 0.0F

                // Hide the game done button
                endGameButton.alpha = 0.0F
                endGameButton.isVisible = false

            } else if (event.action == MotionEvent.ACTION_UP) {
                // button relased.
                Log.d("button released", "It's released")
                // Remove the standings
                playerStandings.alpha = 0.0F
                playerStandings.isVisible = false

                // Bring back the end game button
                endGameButton.alpha = 1.0F
                endGameButton.isVisible = true
                // Brink back the view ranking button
                viewRankingButton.alpha = 1.0F
            }
            false
        }

        // Set up the exitParViewButton
        val exitParViewButton: Button = findViewById(R.id.exitEnterParViewButton)
        val enterParReminderLabel: TextView = findViewById(R.id.enterParHoleReminderLabel)

        exitParViewButton.height = enterParReminderLabel.height
        exitParViewButton.setOnClickListener() {
            val enterParView: FrameLayout = findViewById(R.id.enterParView)
            enterParView.alpha = 0.0f
            enterParView.isVisible = false

            val viewRankingButton: Button = findViewById(R.id.viewRankingButton)
            val endGameButton: Button = findViewById(R.id.endGameButton)

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true
            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            enableScoreCells()
        }

        val parValueRow:TableRow = findViewById(R.id.parValueRow)
        // Set up the par 1 button
        val par1Button:Button = findViewById(R.id.par1Button)
        par1Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 1"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)
            //parButton.

            // Enable the buttons
            enableScoreCells()
            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 1

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 2 button
        val par2Button:Button = findViewById(R.id.par2Button)
        par2Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 2"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 2

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 3 button
        val par3Button:Button = findViewById(R.id.par3Button)
        par3Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 3"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 3

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 4 button
        val par4Button:Button = findViewById(R.id.par4Button)
        par4Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 4"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 4

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 5 button
        val par5Button:Button = findViewById(R.id.par5Button)
        par5Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 5"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 5

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 6 button
        val par6Button:Button = findViewById(R.id.par6Button)
        par6Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 6"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            endGameButton.alpha = 1.0F
            endGameButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 6

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the do update par button
        val doUpdateParButton: Button = findViewById(R.id.doUpdatePar)
        doUpdateParButton.setOnClickListener {
            // Dismiss the par confirmation view
            val parUpdateConfirmationView:FrameLayout = findViewById(R.id.parUpdateConfirmationView)
            parUpdateConfirmationView.alpha = 0.0F

            // Prompt the endgame early view
            val enterParView: FrameLayout = findViewById(R.id.enterParView)
            enterParView.alpha = 1.0F
            enterParView.isVisible = true

        }

        // Set up the do not update par button
        val doNotUpdateParButton: Button = findViewById(R.id.doNotUpdatePar)
        doNotUpdateParButton.setOnClickListener {
            // Dismiss the par confirmation view
            dismissParEntry()

        }
    }

    private fun updateCoursePar() {
        coursePar = 0

        for (i in 0..numberOfHoles - 1) {
            coursePar = coursePar + parArray[i]
        }

        // Update the coursePar header
        val courseParHeaderLabel:TextView = findViewById(R.id.courseParHeaderLabel)
        if (coursePar == 0) {
            courseParHeaderLabel.text = "Course Par\n(TBD)"
        }
        else {
            courseParHeaderLabel.text = "Course Par\n($coursePar)"
        }

        // Save course par to history
        saveParValues()
    }

    fun saveParValues() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {

            // Clear the JSON
            parValuesJSON = JSONArray()

            // Now, add the scores to the JSON
            for (i in 0..numberOfHoles - 1) {
                parValuesJSON.put(parArray[i])
            }

            // Add the number of players JSON to the scorecard history JSON
            gameCardJSON.put("par_array", parValuesJSON)
            putString("parValues", parValuesJSON.toString())

            // save the whole scorecard to the history collection
            gameHistoryJSON.put("game#${gameNumber}", gameCardJSON)

            apply() // asynchronously saves data, where as commit() synchronously saves, blocking main thread
        }
    }

    var sortedTotalScores = IntArray(numberOfPlayers) { 0 }
    var sortedPlayers = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    private fun sortTotalScores() {
        sortedTotalScores = totalScoreArray.clone()
        Arrays.sort(sortedTotalScores)

        var tempArray = totalScoreArray.clone()
        // Now, find the player that corresponds w/ the score
        for (i in 0 until numberOfPlayers) {
            val scoreToFind = sortedTotalScores[i]
            var found = false

            while (found == false) {
                for (j in 0 until tempArray.size) {
                    if (tempArray[j] == scoreToFind) {
                        sortedPlayers[i] = j

                        // "Remove" this element from the temp array so that the same name doesn't come up if there is a tie
                        tempArray[j] = 999

                        found = true
                        break
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun populateViewRankingTable(gameDone: Boolean) {
        val viewRankingTable:TableLayout = findViewById(R.id.viewRankingTable)

            for (i in 0 until numberOfPlayers) {
                val currentRow: TableRow = viewRankingTable.getChildAt(i + 1) as TableRow
                        if (i == 0) {
                            if (gameDone == false) {
                                // Populate the rank with i + 1
                                val rankTV: TextView = currentRow.getChildAt(0) as TextView
                                rankTV.setText("1st")
                            }
                            else {
                                // Populate the rank with i + 1
                                val rankImageview: ImageView = currentRow.getChildAt(0) as ImageView
                                rankImageview.setImageResource(R.drawable.gold)

                            // Populate the rank with i + 1
                                /*val rankImage: TextView = currentRow.getChildAt(0) as TextView

                                rankTV.setText("")
                                rankTV.getViewTreeObserver()
                                    .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        override fun onGlobalLayout() {
                                            val img: Drawable = getResources().getDrawable(R.drawable.gold)
                                            img.setBounds(0, 0, img.intrinsicWidth * rankTV.getMeasuredHeight() / img.intrinsicHeight, rankTV.getMeasuredHeight())
                                            rankTV.setCompoundDrawables(img, null, null, null)
                                            rankTV.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                                        }
                                    }) */
                            }
                        }
                        else if (i == 1) {
                            if (gameDone == false) {
                                val rankTV: TextView = currentRow.getChildAt(0) as TextView
                                rankTV.setText("2nd")
                            }
                            else {
                                val rankImageview: ImageView = currentRow.getChildAt(0) as ImageView
                                rankImageview.setImageResource(R.drawable.silver)
                            }
                        }
                        else if (i == 2) {
                            if (gameDone == false) {
                                val rankTV: TextView = currentRow.getChildAt(0) as TextView
                                rankTV.setText("3rd")
                            }

                            else {
                                val rankImageview: ImageView = currentRow.getChildAt(0) as ImageView
                                rankImageview.setImageResource(R.drawable.bronze)
                            }
                        }
                        else if (i == 3) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("4th")
                        }
                        else if (i == 4) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("5th")
                        }
                        else if (i == 5) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("6th")
                        }
                        else if (i == 6) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("7th")
                        }
                        else if (i == 7) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("8th")
                        }
                        else if (i == 8) {
                            val rankTV: TextView = currentRow.getChildAt(0) as TextView
                            rankTV.setText("9th")
                        }

                    val playerTV: TextView = currentRow.getChildAt(1) as TextView
                        if (playerNamesArray[sortedPlayers[i]] == "") {
                            playerTV.setText("Player ${i + 1}")
                            playerNamesArray[i] = "Player ${i + 1}"
                        }
                        else {
                            playerTV.setText("${playerNamesArray[sortedPlayers[i]]}")
                        }

                    val scoreTV: TextView = currentRow.getChildAt(2) as TextView
                        scoreTV.setText("${sortedTotalScores[i]}")

                    val strokesBehindTV: TextView = currentRow.getChildAt(3) as TextView
                        if (i == 0) {
                            strokesBehindTV.setText("-")
                        }

                        else {
                            var strokesBehind = 0
                            strokesBehind = sortedTotalScores[i] - sortedTotalScores[0]

                            if (strokesBehind == 0) {
                                strokesBehindTV.setText("-")
                            }
                            else {
                                strokesBehindTV.setText("${strokesBehind}")
                            }
                        }

                    val courseParBehindTV: TextView = currentRow.getChildAt(4) as TextView

                if (coursePar == 0) {
                    courseParBehindTV.setText("TBD")
                }

                else {
                    var strokesAgainstCoursePar = 0
                    strokesAgainstCoursePar = sortedTotalScores[i] - coursePar

                    if (strokesAgainstCoursePar == 0) {
                        courseParBehindTV.setText("-")
                    } else if (strokesAgainstCoursePar > 0) {
                        courseParBehindTV.setText("+${strokesAgainstCoursePar}")
                    } else if (strokesAgainstCoursePar < 0) {
                        courseParBehindTV.setText("${strokesAgainstCoursePar}")
                    }
                }
            }
    }

    private fun presentEndGameEarlyView() {
        val endGameSubview: FrameLayout = findViewById(R.id.endGameSubview)

        endGameSubview.alpha = 1.0F
        endGameSubview.isVisible = true
        dismissEnterScoreView()

        dismissEndGameButton()
    }

    private fun dismissEndGameEarlyView() {
        val endGameSubview: FrameLayout = findViewById(R.id.endGameSubview)

        presentEndGameButton()

        endGameSubview.alpha = 0.0F
        endGameSubview.isVisible = false

    }

    private fun dismissEnterScoreView() {
        val enterScoreSubview: FrameLayout = findViewById(R.id.enterScoreSubview)

        presentEndGameButton()

        enterScoreSubview.alpha = 0.0F
        enterScoreSubview.isVisible = false

        // enable the table
        enableScoreCells()
    }

    private fun presentEnterScoreView() {
        val enterScoreSubview: FrameLayout = findViewById(R.id.enterScoreSubview)
        val scoreEntryReminderLabel: TextView = findViewById(R.id.scoreEntryOwnerLabel)

        dismissEndGameButton()
        dismissOuchScoreView()
        scoreEntryReminderLabel.text = "Hole $currentHole: ${playerNamesArray[currentPlayer - 1]}"

        enterScoreSubview.alpha = 1.0F
        enterScoreSubview.isVisible = true
        val score1Button: ImageButton = findViewById(R.id.score1Button)
        score1Button.alpha = 1.0F
        val score2Button: ImageButton = findViewById(R.id.score2Button)
        score2Button.alpha = 1.0F
        val score3Button: ImageButton = findViewById(R.id.score3Button)
        score3Button.alpha = 1.0F
        val score4Button: ImageButton = findViewById(R.id.score4Button)
        score4Button.alpha = 1.0F
        val score5Button: ImageButton = findViewById(R.id.score5Button)
        score5Button.alpha = 1.0F
        val score6Button: ImageButton = findViewById(R.id.score6Button)
        score6Button.alpha = 1.0F
        val clearScoreButton: Button = findViewById(R.id.clearScoreButton)
        clearScoreButton.alpha = 1.0F
        val ouchButton: Button = findViewById(R.id.ouchButton)
        ouchButton.alpha = 1.0F

        // disable the score table
        disableScoreCells()

    }
    var allScoreTouchables = ArrayList<View>();
    var allPlayerNames = ArrayList<View>();

    private fun disableScoreCells() {
        val scoreTable = findViewById<View>(R.id.scoreTableLinearLayout) as LinearLayout
        val layoutButtons: ArrayList<View> = scoreTable.touchables

        allScoreTouchables = layoutButtons

        for (v in layoutButtons) {
            if (v is Button) {
                v.setEnabled(false)
            }
        }

        // disable player text boxes too
        val playerNameStackView = findViewById<View>(R.id.playerNameStackview) as LinearLayout
        val playerNames: ArrayList<View> = playerNameStackView.touchables
        allPlayerNames = playerNames
        for (n in playerNames) {
            if (n is EditText) {
                n.setEnabled(false)
            }
        }
    }

    private fun enableScoreCells() {

        for (v in allScoreTouchables) {
            if (v is Button) {
                v.setEnabled(true)
            }
        }

        for (n in allPlayerNames) {
            if (n is EditText) {
                n.setEnabled(true)
            }
        }
    }

    private fun presentOverwriteView() {
        val overwriteConfirmationView: FrameLayout = findViewById(R.id.scoreUpdateConfirmationView)

        dismissEndGameButton()

        overwriteConfirmationView.alpha = 1.0F
        overwriteConfirmationView.isVisible = true
    }

    private fun dismissOverwriteView() {
        val overwriteConfirmationView: FrameLayout = findViewById(R.id.scoreUpdateConfirmationView)

        presentEndGameButton()

        overwriteConfirmationView.alpha = 0.0F
        overwriteConfirmationView.isVisible = false
    }

    private fun presentGameDoneView() {
        val gameDoneConfirmationView: FrameLayout = findViewById(R.id.gameDoneConfirmationView)

        gameDoneConfirmationView.alpha = 1.0F
        gameDoneConfirmationView.isVisible = true

        dismissEndGameButton()
    }

    private fun dismissGameDoneView() {
        val gameDoneConfirmationView: FrameLayout = findViewById(R.id.gameDoneConfirmationView)

        presentEndGameButton()

        gameDoneConfirmationView.alpha = 0.0F
        gameDoneConfirmationView.isVisible = false
    }

    private fun presentEndGameButton() {
        val endGameButton: Button = findViewById(R.id.endGameButton)
        val viewRankingButton: Button = findViewById(R.id.viewRankingButton)

        endGameButton.alpha = 1.0F
        endGameButton.isVisible = true

        viewRankingButton.alpha = 1.0F
        viewRankingButton.isVisible = true
    }

    private fun dismissEndGameButton() {
        val endGameButton: Button = findViewById(R.id.endGameButton)
        val viewRankingButton: Button = findViewById(R.id.viewRankingButton)

        endGameButton.alpha = 0.0F
        endGameButton.isVisible = false

        viewRankingButton.alpha = 0.0F
        viewRankingButton.isVisible = false
    }

    private fun dismissOuchScoreView() {
        val ouchScoreEntrySubview: androidx.constraintlayout.widget.ConstraintLayout =
            findViewById(R.id.ouchConstraintLayout)

        ouchScoreEntrySubview.alpha = 0.0F
        ouchScoreEntrySubview.isVisible = false
    }

    private fun dismissAllSubviews() {
        dismissOverwriteView()
        dismissEndGameEarlyView()
        dismissEnterScoreView()
        dismissPresentSaveGameToHistoryConfirmationView()
        dismissGameDoneView()
        dismissOuchScoreView()
        dismissViewRanking()
        dismissParEntry()
    }

    private fun dismissParEntry() {
        val parEntryView: FrameLayout = findViewById(R.id.enterParView)
        val parUpdateConfirmationView: FrameLayout = findViewById(R.id.parUpdateConfirmationView)

        parEntryView.alpha = 0.0F
        parEntryView.isVisible = false

        parUpdateConfirmationView.alpha = 0.0F
        parUpdateConfirmationView.isVisible = false
    }

    private fun dismissViewRanking() {
        val viewRankingView: RelativeLayout = findViewById(R.id.viewRankingView)
        val podiumDonebutton:Button = findViewById(R.id.podiumDoneButton)

        viewRankingView.alpha = 0.0F
        viewRankingView.isVisible = false

        podiumDonebutton.alpha = 0.0F
        podiumDonebutton.isVisible = false

    }

    @SuppressLint("ResourceType")
    private fun addToTotalScoreArray(player: Int, hole: Int, score: Int) {
        when (player) {
            1 -> {
                val totalScoreLabel: Button = findViewById(901)
                player1Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player1Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player1ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player1ScoresJSON.put(player1Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player1Totals")
                    playerTotalsJSON.put("player1Totals", player1ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            2 -> {
                val totalScoreLabel: Button = findViewById(902)
                player2Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player2Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player2ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player2ScoresJSON.put(player2Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player2Totals")
                    playerTotalsJSON.put("player2Totals", player2ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }

            }
            3 -> {
                val totalScoreLabel: Button = findViewById(903)
                player3Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player3Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player3ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player3ScoresJSON.put(player3Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player3Totals")
                    playerTotalsJSON.put("player3Totals", player3ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            4 -> {
                val totalScoreLabel: Button = findViewById(904)
                player4Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player4Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player4ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player4ScoresJSON.put(player4Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player4Totals")
                    playerTotalsJSON.put("player4Totals", player4ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            5 -> {
                val totalScoreLabel: Button = findViewById(905)
                player5Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player5Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player5ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player5ScoresJSON.put(player5Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player5Totals")
                    playerTotalsJSON.put("player5Totals", player5ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            6 -> {
                val totalScoreLabel: Button = findViewById(906)
                player6Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player6Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player6ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player6ScoresJSON.put(player6Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player6Totals")
                    playerTotalsJSON.put("player6Totals", player6ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            7 -> {
                val totalScoreLabel: Button = findViewById(907)
                player7Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player7Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player7ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player7ScoresJSON.put(player7Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player7Totals")
                    playerTotalsJSON.put("player7Totals", player7ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            8 -> {
                val totalScoreLabel: Button = findViewById(908)
                player8Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player8Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player8ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player8ScoresJSON.put(player8Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player8Totals")
                    playerTotalsJSON.put("player8Totals", player8ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
            9 -> {
                val totalScoreLabel: Button = findViewById(909)
                player9Totals[hole - 1] = score
                // Now add all the holes together
                var totalScore = 0
                for (i in 0..numberOfHoles - 1) {
                    totalScore += player9Totals[i]
                }
                // Add this to the button
                totalScoreLabel.text = "$totalScore"

                // Add this to totalScoreArray
                totalScoreArray[player - 1] = totalScore

                // Save the player1Scores
                val player9ScoresJSON = JSONArray()
                // First, add the scores to the JSON
                for (i in 0..numberOfHoles - 1) {
                    player9ScoresJSON.put(player9Totals[i])
                }

                // Second, add the player1ScoresJSON to the totalScoresJSON

                // Convert the player1ScoresJSON to string, and save it to shared pref
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.apply {
                    playerTotalsJSON.remove("player9Totals")
                    playerTotalsJSON.put("player9Totals", player9ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
                    remove("playerTotalScores")
                    putString("playerTotalScores", playerTotalsJSON.toString())
                    apply()
                }
            }
        }

        updateCompletedHoleTracker(currentHole)

        if (gameInProgress) {
            // Check to see if the scrollview needs to be updated
            updateScrollViewOffSet(xValue, currentHole)
        }

        sortTotalScores()
    }

    private fun presentSaveToHistoryConfirmationView() {
        val saveToHistorySubview: FrameLayout = findViewById(R.id.saveGameToHistoryConfirmationView)

        dismissEndGameButton()

        saveToHistorySubview.alpha = 1.0F
        saveToHistorySubview.isVisible = true
    }

    private fun dismissPresentSaveGameToHistoryConfirmationView() {
        val saveToHistorySubview: FrameLayout = findViewById(R.id.saveGameToHistoryConfirmationView)

        presentEndGameButton()

        saveToHistorySubview.alpha = 0.0F
        saveToHistorySubview.isVisible = false
    }

    @SuppressLint("ResourceType")
    private fun updateScrollViewOffSet(xPosition: Int, holeEntered: Int) {
        val playerLabel: TextView = findViewById(R.id.playerLabel)
        val totalLabel: TextView = findViewById(R.id.totalLabel)
        val scoreTableLayout: LinearLayout = findViewById(R.id.scoreTableLinearLayout)
        val horizontalScrollView: HorizontalScrollView = findViewById(R.id.horizontalScrollView)
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)

        // Check if all of the players have finished the hole
        for (playerScore in playerTotals) {

            if (playerScore[holeEntered - 1] == 0) {
                // Then all players have not scored on this hole
                if (holesCompleted == numberOfHoles - 1) {

                    val displayMetrics = DisplayMetrics()
                    val tableviewWidth: Int = scoreTableLayout.width
                    val safeAreaWidth: Int = displayMetrics.widthPixels

                    val playerAndTotalLabelWidth: Int = playerLabel.width + totalLabel.width

                    val finalX:Int = tableviewWidth - (safeAreaWidth - playerAndTotalLabelWidth)
                    horizontalScrollView.smoothScrollTo(finalX, 0)
                }
                else {
                    // Set the scrollview back to the cells for the players that have not entered their score
                    horizontalScrollView.smoothScrollTo(xValue, 0)
                }
                return
            }
        }
        // If you get here, each player has completed the hole
        completedHoleArray[holeEntered - 1] = 1

        // Now check if all players have completed all previous holes
        for (i in 0 until holeEntered) {
            if (completedHoleArray[i] == 0) {
                // Set the scrollview back to the cells for the players that haven't entered
                horizontalScrollView.smoothScrollTo(xPosition, 0)
                return
            }
        }

        // Check to see if the game is between hole #2 and hole #16
        if (holesCompleted >= 2) {
            val playerScoreCell: Button = findViewById(11)
            if (holesCompleted < numberOfHoles - 1) {
                // Add the cells width the x-position of the scrollview
                    val test = numberOfHolesCompleted()
                Log.d("PLAYER_SCORE_WIDTH", "Player score width = ${playerScoreCell.width}")

                if (playerScoreCell.width != 0 && sharedPref.getInt("cellWidth", 0) == 0) {
                    val editor = sharedPref.edit()

                    editor.apply {
                        putInt("cellWidth", playerScoreCell.width)
                        apply()
                    }
                }
                if (playerScoreCell.width != 0) {
                    xValue = playerScoreCell.width /*+ 0.75*/ * (test - 1)
                    //  Set the scrollview to the new x-position (moves to the right)
                    horizontalScrollView.smoothScrollTo(xValue, 0)
                }
                else {

                    val width = sharedPref.getInt("cellWidth", 222)
                    xValue = width /*+ 0.75*/ * (test - 1)
                    //  Set the scrollview to the new x-position (moves to the right)
                    window.decorView.post {
                        horizontalScrollView.smoothScrollTo(xValue, 0)
                    }

                }
            }

            // Present an interstitial after hole number 9
            if (holesCompleted == 9) {
              //  if adsRemoved == false {
                    presentInterstitial()
              //  }
            }

            if (holesCompleted == 10) {
              //  if adsRemoved == false {
                    setUpInterstitialAd()
               // }
            }

            else if (holesCompleted == numberOfHoles) {
                // Prompt to the user that the game is over
                //backToMainMenu.alpha = 0
                //newGameButton.alpha = 0
           //     viewRankingButton.alpha = 0
                presentGameDoneView()
            }
        }
        saveXValue()
    }

    private fun presentInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun updateCompletedHoleTracker(holeEntered: Int) {
        // Check to see if all of the players have completed that specific hole
        for (playerScore in playerTotals) {

            // all players have not scored on that hole
            if (playerScore[holeEntered - 1] == 0) {
                completedHoleArray[holeEntered - 1] = 0
                return
            }
        }
        completedHoleArray[holeEntered - 1] = 1

        holesCompleted = numberOfHolesCompleted()
    }

    private fun numberOfHolesCompleted(): Int {
        var tempNum = 0

        for (i in 0 until numberOfHoles) {
            if (completedHoleArray[i] == 1) {
                tempNum += 1
            }
        }
        return tempNum
    }

    private fun scoreCardVisited() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("scoreCardVisited", 1)
            apply()
        }
    }

    private fun noMercy() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("noMercy", 1)
            apply()
        }
        alreadyMercy = 1
    }

    private fun checkCourseParAchievements() {
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // See if any player hit a score of at least par

        // first, check if all of the par cells are entered
        var allEntered = 1
        for (i in 0..numberOfHoles - 1) {
            if (parArray[i] == 0) {
                allEntered = 0
            }
        }

        // Now, get the winning score
        val winningArray = determineWinningArray()
        val winningScore = winningArray[0]

        // Check if the winning score is <= par
        if (winningScore <= coursePar) {
            editor.apply {
                // Save the number of games completed
                putInt("parAchieved", 1)
                apply()
            }
        }

        // Check if the winning score is <= birdie
        if (winningScore <= (coursePar - 1)) {
            editor.apply {
                // Save the number of games completed
                putInt("birdieAchieved", 1)
                apply()
            }
        }

        // Check if the winning score is <= eagle
        if (winningScore <= (coursePar - 2)) {
            editor.apply {
                // Save the number of games completed
                putInt("eagleAchieved", 1)
                apply()
            }
        }
    }
}