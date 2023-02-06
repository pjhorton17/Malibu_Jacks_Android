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
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.File
import java.io.FileOutputStream
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Details : AppCompatActivity() {
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
    private var testWidth = 0
    private var totalScoreArray = intArrayOf(999, 999, 999, 999, 999, 999, 999, 999, 999)
    private var alreadyMercy = 0
    private var defaultBallColor = 0
    private var ballColorToUpdate = 0
    private var golfBallColorArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var gameToLoad = 0
    private var editFlag = ""
    val h = 115
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setUpAdaptiveAd()

        totalScoreArray = IntArray(numberOfPlayers) { 0 }

        // See if there is a game in progress
        loadGame()

        // Dismiss all subviews
        dismissAllSubviews()

        // Configure the buttons
        configureButtons()

        // SaveGame
        //saveGame()

        // Update the viewRanking table
        createViewRankingTable(false)

        // Check the achievement
        historicGame()

    }

    private fun setUpAdaptiveAd() {
        adContainerView = findViewById(R.id.adViewFrameLayoutDetails)
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
        private val AD_UNIT_ID = "ca-app-pub-5910555078334309/4722455432"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val width: Int = android.content.res.Resources.getSystem().displayMetrics.widthPixels
        safeAreaWidth = width

        val playerLabel: TextView = findViewById(R.id.playerLabelDetails)
        playerLabelWidth = playerLabel.width

        val totalLabel: TextView = findViewById(R.id.totalLabelDetails)
        totalLabelWidth = totalLabel.width
        totalLabelHeight = totalLabel.height

        val ballLabel: TextView = findViewById(R.id.ballColorLabelDetails)
        ballLabelWidth = ballLabel.width

        // Check to see if the player names have already been loaded
        val playerNameStackView: LinearLayout = findViewById(R.id.playerNameStackviewDetails)
        if (playerNameStackView.childCount == 0) {
            addTotalScoreStackView(numberOfPlayers)
            addPlayerNames(numberOfPlayers)
            addPlayerRows(numberOfPlayers, numberOfHoles)
            addBallColors(numberOfPlayers)
            populateParValues(numberOfHoles)
            populateHoleNumbers(numberOfHoles)

            // Update the SV
            if (holesCompleted != 0) {
                Log.d("X_Position", "x-position = $xValue")
            }
            val horizontalScrollView: HorizontalScrollView = findViewById(R.id.horizontalScrollViewDetails)
            horizontalScrollView.smoothScrollTo(xValue, 0)
        }
        calculateTotalScore()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createViewRankingTable(gameDone: Boolean) {
        var viewRankingTable: TableLayout = findViewById(R.id.viewRankingTableDetails)
        val font = resources.getFont(R.font.chalkboard_se_light_)

        if (gameDone == true) {
            // Clear table
            clearTable()
        }

        // Update the coursePar header
        val courseParHeaderLabel:TextView = findViewById(R.id.courseParHeaderLabelDetails)
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
                rankTV.minHeight = 125
                tableRow.addView(rankTV)
            }

            else {
                if (i < 3) {
                    val rankImageView = ImageView(this)
                    rankImageView.setBackgroundResource(R.drawable.view_ranking_cell_background)
                    rankImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    rankImageView.adjustViewBounds = true

                    rankImageView.maxHeight = 125
                    rankImageView.maxWidth = 125
                    rankImageView.setPadding(20)
                    tableRow.addView(rankImageView)
                }

                else {
                    val rankTV = TextView(this)
                    rankTV.setTextColor(Color.BLACK)
                    rankTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
                    rankTV.gravity = Gravity.CENTER
                    rankTV.typeface = font
                    rankTV.minHeight = 125
                    tableRow.addView(rankTV)
                }
            }

            val playerTV = TextView(this)
            playerTV.setTextColor(Color.BLACK)
            playerTV.typeface = font
            playerTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
            playerTV.gravity = Gravity.CENTER
            playerTV.minHeight = 125

            val scoreTV = TextView(this)
            scoreTV.setTextColor(Color.BLACK)
            scoreTV.typeface = font
            scoreTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
            scoreTV.gravity = Gravity.CENTER
            scoreTV.minHeight = 125

            val strokesBehindTV = TextView(this)
            strokesBehindTV.setTextColor(Color.BLACK)
            strokesBehindTV.typeface = font
            strokesBehindTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
            strokesBehindTV.gravity = Gravity.CENTER
            strokesBehindTV.minHeight = 125

            val courseParBehindTV = TextView(this)
            courseParBehindTV.setTextColor(Color.BLACK)
            courseParBehindTV.typeface = font
            courseParBehindTV.setBackgroundResource(R.drawable.view_ranking_cell_background)
            courseParBehindTV.gravity = Gravity.CENTER
            courseParBehindTV.minHeight = 125

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
        val viewRankingTable:TableLayout = findViewById(R.id.viewRankingTableDetails)
        val childCount = viewRankingTable.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            viewRankingTable.removeViews(1, childCount - 1);
        }
    }

    private fun saveGameToHistory() {
        // Get all of the properties to save to the gameModel
        val gameIDTS = gameNumber + 1
        val locationTS = location
        val dateTS = date
        val playerScoreArrayTS = playerTotalsJSON.toString()

        // Get the winning score + index of winning player
        val totalScoreArrayTS = getTotalScoreArray()
        val winningArray = determineWinningArray()
        val winningScoreTS = winningArray[0]
        val winningPlayerTS = playerNamesArray[winningArray[1]]
        val parValuesTS = parValuesJSON.toString()
        val colorValuesTS = colorValuesJSON.toString()

        playerNamesJSON = JSONArray()
        // Get the names
        for (element in playerNamesArray) {
            playerNamesJSON.put(element)
        }

        val playerNamesTS = playerNamesJSON.toString()

        // Create a game model as a JSON Object
        val gameDetailsTS = JSONObject()
        gameDetailsTS.put("gameId", gameIDTS)
        gameDetailsTS.put("location", locationTS)
        gameDetailsTS.put("numberOfPlayers", numberOfPlayers)
        gameDetailsTS.put("winningScore", winningScoreTS)
        gameDetailsTS.put("winningPlayer", winningPlayerTS)
        gameDetailsTS.put("date", dateTS)
        gameDetailsTS.put("totalScoreArray", totalScoreArrayTS)
        gameDetailsTS.put("playerNames", playerNamesTS)
        gameDetailsTS.put("numberOfHoles", numberOfHoles)
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
           // addToCompletedGameTracker()
        }catch (e: Exception){
            e.printStackTrace()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun populateParValues(numberOfHoles: Int) {
        val parvalueCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        testWidth = parvalueCellWidth
        val parValueRow: TableRow = findViewById(R.id.parValueRowDetails)
        val totalLabel: TextView = findViewById(R.id.totalLabelDetails)
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
            if (parArray[i - 1] == 0) {
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
                // Set the flag for the edit to "par"
                editFlag = "par"
                currentHole = parValueButton.tag as Int
               presentOverwriteSavedGameConfirmationView()
            }
            // Add this hole label to the tableRow
            parValueRow.addView(parValueButton)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun populateHoleNumbers(numberOfHoles: Int) {
        val holeNumberCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        testWidth = holeNumberCellWidth
        val holeNumberRow: TableRow = findViewById(R.id.holeNumberRowDetails)
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
    @SuppressLint("SuspiciousIndentation")
    private fun addPlayerRows(numberOfPlayers: Int, numberOfHoles: Int) {

        val totalLabel: TextView = findViewById(R.id.totalLabelDetails)
        totalLabelHeight = totalLabel.height

        val scoreCellWidth:Int = (safeAreaWidth - ballLabelWidth - playerLabelWidth - totalLabelWidth) / 3
        val player1ScoreRow: TableRow = findViewById(R.id.player1ScoreRowDetails)
        val player2ScoreRow: TableRow = findViewById(R.id.player2ScoreRowDetails)
        val player3ScoreRow: TableRow = findViewById(R.id.player3ScoreRowDetails)
        val player4ScoreRow: TableRow = findViewById(R.id.player4ScoreRowDetails)
        val player5ScoreRow: TableRow = findViewById(R.id.player5ScoreRowDetails)
        val player6ScoreRow: TableRow = findViewById(R.id.player6ScoreRowDetails)
        val player7ScoreRow: TableRow = findViewById(R.id.player7ScoreRowDetails)
        val player8ScoreRow: TableRow = findViewById(R.id.player8ScoreRowDetails)
        val player9ScoreRow: TableRow = findViewById(R.id.player9ScoreRowDetails)

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

        for (i in 1..numberOfPlayers) {
            // Now add buttons for each hole for that player
            for (j in 1..numberOfHoles) {
                val playerScoreButton = Button(this)
                var playerTag:String
                playerScoreButton.height = totalLabelHeight - 25
                playerScoreButton.width = scoreCellWidth
                playerScoreButton.minWidth = 0
                playerScoreButton.textSize = 11F
                playerScoreButton.maxWidth = 40
              //  if (strPlayerScoresJson == "") {
                    playerScoreButton.setBackgroundResource(R.drawable.empty_scorecell_background)
               // }
               // else {
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

                //}
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
                        presentEnterScoreView()
                }

                when (i) {
                    1 -> {
                        //player1ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player1ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    2 -> {
                        //player2ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player2ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    3 -> {
                        //player3ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player3ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    4 -> {
                        //player4ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player4ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    5 -> {
                        //player5ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player5ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    6 -> {
                        //player6ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player6ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    7 -> {
                        //player7ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player7ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    8 -> {
                        //player8ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player8ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
                    }
                    9 -> {
                       // player9ScoreRow.addView(playerScoreButton, scoreCellWidth, totalLabelHeight - 25)
                        player9ScoreRow.addView(playerScoreButton, scoreCellWidth, h)
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
        val totalScoreStackView: LinearLayout = findViewById(R.id.totalScoreStackviewDetails)
        val playerNameStackView: LinearLayout = findViewById(R.id.playerNameStackviewDetails)
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
            playerNameTextView.height = totalLabelHeight - 25
            playerNameTextView.typeface = font
            playerNameTextView.textSize = 12F
            playerNameTextView.maxLines = 1
            playerNameTextView.inputType = (InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            playerNameTextView.isCursorVisible = false
            playerNameTextView.isEnabled = false
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
            playerNameTextView.tag = i

            // Add a listener
           /* playerNameTextView.addTextChangedListener {
                //saveNames(playerNamesArray, playerNameTextView, playerNameTextView.tag as Int)
                Log.d("PLAYER_TEXT_VIEW", "Change Listener Heard")
                Log.d("PLAYER_TEXT_VIEW", "Tag = ${playerNameTextView.tag as Int}")
                Log.d("PLAYER_TEXT_VIEW", "Player Name = ${playerNameTextView.text}")
                Log.d("PLAYER_TEXT_VIEW", "Size of player array = ${playerNamesArray.size}")

                if (playerNameTextView.text.toString() == "") {
                    playerNameTextView.hint = "Player ${i + 1}"
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
                //saveGame()
            } */

            playerNameStackView.addView(playerNameTextView)
        }
    }

    fun addBallColors(numberOfPlayers: Int) {
        val ballColorStackView: LinearLayout = findViewById(R.id.ballColorStackviewDetails)
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
            if (golfBallColorArray[i] == 0) {
                ballImageButton.setBackgroundColor(Color.WHITE)
            }
            else {
                ballImageButton.setBackgroundColor(golfBallColorArray[i])
            }

            ballImageButton.setOnClickListener() {
                // Set the ball that we are updating
                ballColorToUpdate = i

                //openColorPicker()
            }
            ballColorStackView.addView(ballImageButton)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTotalScoreStackView(numberOfPlayers: Int) {
        val font = resources.getFont(R.font.chalkboard_se_bold_)
        val totalScoreStackView: LinearLayout = findViewById(R.id.totalScoreStackviewDetails)
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
            playerTotalScore.typeface = font
            playerTotalScore.textSize = 12F
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

        saveGameToHistory()
        println("test")
    }
    private fun loadGame() {

        // Get the game #
        gameNumber = intent.getIntExtra("gameNumber", 1)

        // Get the game file to display
        val file = File(filesDir, "game_${gameNumber + 1}")
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

        // Now, get the data for the game
            // Get the number of players
            numberOfPlayers = jsonObject.getInt("numberOfPlayers")
            Log.d("NUMBER_OF_PLAYERS", "Number of players = $numberOfPlayers")
            totalScoreArray = IntArray(numberOfPlayers) { 0 }

            // Get the location
            location = jsonObject.getString("location")

            // Get the players names and update the name tags
            playerNamesArray = Array(numberOfPlayers) {""}

            // Get the number of Holes
            numberOfHoles = jsonObject.getInt("numberOfHoles")

            // Get the date
            date =  jsonObject.getString("date").toString()

            // populate the Par Array
            val strParValuesJSON = jsonObject.getString("parValuesArray")
            if (strParValuesJSON != null) {
                try {
                    val response = JSONArray(strParValuesJSON)
                    for (j in 0..numberOfHoles - 1) {
                        val par = Integer.parseInt(response[j].toString())
                        parArray[j] = par
                    }
                } catch (e: JSONException) {
                    Log.d("JSON_EXCEPTION", "Idk why it didn't work")
                }
            }

        // Clear the JSON
        parValuesJSON = JSONArray()

        // Now, add the scores to the JSON
        for (i in 0..numberOfHoles - 1) {
            parValuesJSON.put(parArray[i])
        }
            // Update course Par
            updateCoursePar()

            // Populate the colorArray
            val strColorsValuesJSON = jsonObject.getString("golfBallColorArray")
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

            var playerNamesString = jsonObject.getString("playerNames")
            val tokens = StringTokenizer(playerNamesString, ",")

            for (i in 0 until numberOfPlayers) {
                var tokenPlaceHolder = tokens.nextToken()

                if (numberOfPlayers == 1) {
                    // Remove the [
                    tokenPlaceHolder = tokenPlaceHolder.substring(1)

                    // Remove the " at the beginning
                    tokenPlaceHolder = tokenPlaceHolder.substring(1)

                    // Remove the " at the end
                    tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 2)
                }

                else if (numberOfPlayers == 2) {
                    if (i == 0) {
                        // Remove the [
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the " at the beginning
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the " at the end
                        tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 2)

                    }
                    else if (i == numberOfPlayers - 1) {
                        // Remove the " at the beginning
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the "] at the end
                        tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 2);
                    }
                }

                else {
                    if (i == 0) {
                        // Remove the [
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the " at the beginning
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the " at the end
                        tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 1)

                    }
                    else if (i == numberOfPlayers - 1) {
                        // Remove the " at the beginning
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the "] at the end
                        tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 2);
                    }

                    else {
                        // Remove the " at the beginning
                        tokenPlaceHolder = tokenPlaceHolder.substring(1)

                        // Remove the " at the end
                        tokenPlaceHolder = tokenPlaceHolder.substring(0, tokenPlaceHolder.length - 1);
                    }
                }
                playerNamesArray[i] = tokenPlaceHolder
            }

            Log.d("PLAYER_TEXT_VIEW", "Size of player array = ${playerNamesArray.size}")

            // Get the players scores and update the scorecard
            val strPlayerScoresJson = jsonObject.getString("playerScoreArray")
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

        // update scoreJSONs
        updateScoresJSON()

            // Now, update the completed holes array
            for (i in 1..numberOfHoles) {
                updateCompletedHoleTracker(i)
            }
            // Get the x-position
            xValue = 0
            Log.d("X_VALUE", "X-Position = $xValue")

            sortTotalScores()
    }

    fun updateScoresJSON() {
        for (i in 0 until numberOfPlayers) {
            val playerScoresJSON = JSONArray()
            for (j in 0..numberOfHoles - 1) {
                if (i == 0) {
                    playerScoresJSON.put(player1Totals[j])
                }
                else if (i == 1) {
                    playerScoresJSON.put(player2Totals[j])
                }
                else if (i == 2) {
                    playerScoresJSON.put(player3Totals[j])
                }
                else if (i == 3) {
                    playerScoresJSON.put(player4Totals[j])
                }
                else if (i == 4) {
                    playerScoresJSON.put(player5Totals[j])
                }
                else if (i == 5) {
                    playerScoresJSON.put(player6Totals[j])
                }
                else if (i == 6) {
                    playerScoresJSON.put(player7Totals[j])
                }
                else if (i == 7) {
                    playerScoresJSON.put(player8Totals[j])
                }
                else if (i == 8) {
                    playerScoresJSON.put(player9Totals[j])
                }
                playerTotalsJSON.put("player${i + 1}Totals", playerScoresJSON)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun configureButtons() {
        val font = resources.getFont(R.font.chalkboard_se_light_)

        // Set up the "don't end game early" button
        val doNotEndGameButton: Button = findViewById(R.id.noEndGameButtonDetails)
        doNotEndGameButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEndGameEarlyView()
        }

        // Set up the "don't end game early" button
        val doEndGameButton: Button = findViewById(R.id.yesEndGameButtonDetails)
        doEndGameButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEndGameEarlyView()
        }

        // Set up the exit scoreView button
        val exitScoreViewButton: Button = findViewById(R.id.exitScoreViewButtonDetails)
        exitScoreViewButton.setOnClickListener {
            // Dismiss the endgame confirmation view
            dismissEnterScoreView()

            // Change the ouch label back to 7
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextViewDetails)
            ouchScoreTextView.text = "7"
        }

        // Set up the score 1 entered button
        val score1Button: ImageButton = findViewById(R.id.score1ButtonDetails)
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
        val score2Button: ImageButton = findViewById(R.id.score2ButtonDetails)
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
        val score3Button: ImageButton = findViewById(R.id.score3ButtonDetails)
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
        val score4Button: ImageButton = findViewById(R.id.score4ButtonDetails)
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
        val score5Button: ImageButton = findViewById(R.id.score5ButtonDetails)
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
        val score6Button: ImageButton = findViewById(R.id.score6ButtonDetails)
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
        val clearScoreButton: Button = findViewById(R.id.clearScoreButtonDetails)
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
        val ouchButton: Button = findViewById(R.id.ouchButtonDetails)
        ouchButton.setOnClickListener {
            // Present the ouch scoreView
            val ouchScoreEntrySubview: androidx.constraintlayout.widget.ConstraintLayout =
                findViewById(R.id.ouchConstraintLayoutDetails)

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
        val plusOuchButton: Button = findViewById(R.id.plusOuchButtonDetails)
        plusOuchButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextViewDetails)

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
        val minusOuchButton: Button = findViewById(R.id.minusOuchButtonDetails)
        minusOuchButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextViewDetails)

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
        val acceptOuchScoreButton: Button = findViewById(R.id.enterOuchScoreButtonDetails)
        acceptOuchScoreButton.setOnClickListener {
            val ouchScoreTextView: TextView = findViewById(R.id.ouchScoreTextViewDetails)

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

        // Set up the "done with podium" button
        val doneWithPodiumButton: Button = findViewById(R.id.podiumDoneButtonDetails)
        doneWithPodiumButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up the "viewRanking" button
        val viewRankingButton: Button = findViewById(R.id.viewRankingButtonDetails)
        val playerStandings: FrameLayout = findViewById(R.id.viewRankingViewDetails)
        viewRankingButton.setOnTouchListener() { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // button pressed
                Log.d("button pressed", "It's pressed")

                // Sort the total scores
                sortTotalScores()
                // Populate the table
                populateViewRankingTable(false)

                // Present the table
                playerStandings.alpha = 1.0F
                playerStandings.isVisible = true

                // Hide the view ranking button
                viewRankingButton.alpha = 0.0F

            } else if (event.action == MotionEvent.ACTION_UP) {
                // button relased.
                Log.d("button released", "It's released")
                // Remove the standings
                playerStandings.alpha = 0.0F
                playerStandings.isVisible = false

                // Brink back the view ranking button
                viewRankingButton.alpha = 1.0F
            }
            false
        }

        // Set up the exitParViewButton
        val exitParViewButton: Button = findViewById(R.id.exitEnterParViewButtonDetails)
        val enterParReminderLabel: TextView = findViewById(R.id.enterParHoleReminderLabelDetails)

        exitParViewButton.height = enterParReminderLabel.height
        exitParViewButton.setOnClickListener() {
            val enterParView: FrameLayout = findViewById(R.id.enterParViewDetails)
            enterParView.alpha = 0.0f
            enterParView.isVisible = false

            val viewRankingButton: Button = findViewById(R.id.viewRankingButtonDetails)

            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            enableScoreCells()
        }

        val parValueRow:TableRow = findViewById(R.id.parValueRowDetails)
        // Set up the par 1 button
        val par1Button:Button = findViewById(R.id.par1ButtonDetails)
        par1Button.setOnClickListener() {
            // Set the par value for the appropriate hole
            val parButton: Button
            parButton = parValueRow.getChildAt(currentHole - 1) as Button
            parButton.text = "Par: 1"
            parButton.setTextColor(Color.BLACK)
            parButton.setBackgroundResource(R.drawable.par_entered_background)

            // Enable the buttons
            enableScoreCells()
            viewRankingButton.alpha = 1.0F
            viewRankingButton.isVisible = true

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 1

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 2 button
        val par2Button:Button = findViewById(R.id.par2ButtonDetails)
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

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 2

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 3 button
        val par3Button:Button = findViewById(R.id.par3ButtonDetails)
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

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 3

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 4 button
        val par4Button:Button = findViewById(R.id.par4ButtonDetails)
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

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 4

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 5 button
        val par5Button:Button = findViewById(R.id.par5ButtonDetails)
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

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 5

            // Update the coursePar
            updateCoursePar()
        }

        // Set up the par 6 button
        val par6Button:Button = findViewById(R.id.par6ButtonDetails)
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

            // Dismiss the par entry view
            dismissParEntry()

            // Add this par value to the par totals array
            parArray[currentHole - 1] = 6

            // Update the coursePar
            updateCoursePar()
        }

        val doUpdateSavedGameButton:Button = findViewById(R.id.doUpdateSavedGame)
        doUpdateSavedGameButton.setOnClickListener {
            if (editFlag == "par") {
                // Dismiss the confirmation view
                dismissSavedGameEditConfirmationView()

                // Bring up the enter par view
                val enterParView: FrameLayout = findViewById(R.id.enterParViewDetails)
                enterParView.alpha = 1.0F
                enterParView.isVisible = true

                // Set the par reminder label
                val parReminderLabel:TextView = findViewById(R.id.enterParHoleReminderLabelDetails)
                parReminderLabel.text = "Enter par for hole $currentHole"

                // Set the current hole
                //currentHole = i

                // Dismiss the view ranking button and end game button
                val viewRankingButton: Button = findViewById(R.id.viewRankingButtonDetails)

                viewRankingButton.alpha = 0.0F
                viewRankingButton.isVisible = false

                disableScoreCells()
            }
            else if (editFlag == "score") {
                // Dismiss the confirmation view
                dismissSavedGameEditConfirmationView()

                val enterScoreSubview: FrameLayout = findViewById(R.id.enterScoreSubviewDetails)
                val scoreEntryReminderLabel: TextView = findViewById(R.id.scoreEntryOwnerLabelDetails)

                dismissEndGameButton()
                dismissOuchScoreView()
                scoreEntryReminderLabel.text = "Hole $currentHole: ${playerNamesArray[currentPlayer - 1]}"

                enterScoreSubview.alpha = 1.0F
                enterScoreSubview.isVisible = true
                val score1Button: ImageButton = findViewById(R.id.score1ButtonDetails)
                score1Button.alpha = 1.0F
                val score2Button: ImageButton = findViewById(R.id.score2ButtonDetails)
                score2Button.alpha = 1.0F
                val score3Button: ImageButton = findViewById(R.id.score3ButtonDetails)
                score3Button.alpha = 1.0F
                val score4Button: ImageButton = findViewById(R.id.score4ButtonDetails)
                score4Button.alpha = 1.0F
                val score5Button: ImageButton = findViewById(R.id.score5ButtonDetails)
                score5Button.alpha = 1.0F
                val score6Button: ImageButton = findViewById(R.id.score6ButtonDetails)
                score6Button.alpha = 1.0F
                val clearScoreButton: Button = findViewById(R.id.clearScoreButtonDetails)
                clearScoreButton.alpha = 1.0F
                val ouchButton: Button = findViewById(R.id.ouchButtonDetails)
                ouchButton.alpha = 1.0F

                // disable the score table
                disableScoreCells()
            }
        }

        val doNotUpdateSavedGameButton:Button = findViewById(R.id.doNotUpdateSavedGame)
        doNotUpdateSavedGameButton.setOnClickListener {
            dismissSavedGameEditConfirmationView()

            val viewRankingButton:Button = findViewById(R.id.viewRankingButtonDetails)
            viewRankingButton.alpha = 1.0f
            viewRankingButton.isVisible = true
        }
    }

    private fun updateCoursePar() {
        coursePar = 0

        for (i in 0..numberOfHoles - 1) {
            coursePar = coursePar + parArray[i]
        }

        // Update the coursePar header
        val courseParHeaderLabel:TextView = findViewById(R.id.courseParHeaderLabelDetails)
        if (coursePar == 0) {
            courseParHeaderLabel.text = "Course Par\n(TBD)"
        }
        else {
            courseParHeaderLabel.text = "Course Par\n($coursePar)"
        }

        // Clear the JSON
        parValuesJSON = JSONArray()

        // Now, add the scores to the JSON
        for (i in 0..numberOfHoles - 1) {
            parValuesJSON.put(parArray[i])
        }

        // Save course par to history
        saveGameToHistory()
        println("test")
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
        val viewRankingTable:TableLayout = findViewById(R.id.viewRankingTableDetails)

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
        val endGameSubview: FrameLayout = findViewById(R.id.endGameSubviewDetails)

        endGameSubview.alpha = 1.0F
        endGameSubview.isVisible = true
        dismissEnterScoreView()

        dismissEndGameButton()
    }

    private fun dismissEndGameEarlyView() {
        val endGameSubview: FrameLayout = findViewById(R.id.endGameSubviewDetails)

        presentEndGameButton()

        endGameSubview.alpha = 0.0F
        endGameSubview.isVisible = false

    }

    private fun dismissEnterScoreView() {
        val enterScoreSubview: FrameLayout = findViewById(R.id.enterScoreSubviewDetails)

        presentEndGameButton()

        enterScoreSubview.alpha = 0.0F
        enterScoreSubview.isVisible = false

        // enable the table
        enableScoreCells()
    }

    private fun presentEnterScoreView() {
        editFlag = "score"
        presentOverwriteSavedGameConfirmationView()
    }
    var allScoreTouchables = ArrayList<View>();
    var allPlayerNames = ArrayList<View>();

    private fun disableScoreCells() {
        val scoreTable = findViewById<View>(R.id.scoreTableLinearLayoutDetails) as LinearLayout
        val layoutButtons: ArrayList<View> = scoreTable.touchables

        allScoreTouchables = layoutButtons

        for (v in layoutButtons) {
            if (v is Button) {
                v.setEnabled(false)
            }
        }

        // disable player text boxes too
        val playerNameStackView = findViewById<View>(R.id.playerNameStackviewDetails) as LinearLayout
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

    /*private fun presentOverwriteView() {
        val overwriteConfirmationView: FrameLayout = findViewById(R.id.scoreUpdateConfirmationViewDetails)

        dismissEndGameButton()

        overwriteConfirmationView.alpha = 1.0F
        overwriteConfirmationView.isVisible = true
    } */

    /* private fun dismissOverwriteView() {
        val overwriteConfirmationView: FrameLayout = findViewById(R.id.scoreUpdateConfirmationViewDetails)

        presentEndGameButton()

        overwriteConfirmationView.alpha = 0.0F
        overwriteConfirmationView.isVisible = false
    } */

    /*private fun presentGameDoneView() {
        val gameDoneConfirmationView: FrameLayout = findViewById(R.id.gameDoneConfirmationViewDetails)

        gameDoneConfirmationView.alpha = 1.0F
        gameDoneConfirmationView.isVisible = true

        dismissEndGameButton()
    } */

    /*private fun dismissGameDoneView() {
        val gameDoneConfirmationView: FrameLayout = findViewById(R.id.gameDoneConfirmationViewDetails)

        presentEndGameButton()

        gameDoneConfirmationView.alpha = 0.0F
        gameDoneConfirmationView.isVisible = false
    }*/

    private fun presentEndGameButton() {
        val viewRankingButton: Button = findViewById(R.id.viewRankingButtonDetails)

        viewRankingButton.alpha = 1.0F
        viewRankingButton.isVisible = true
    }

    private fun dismissEndGameButton() {
        val viewRankingButton: Button = findViewById(R.id.viewRankingButtonDetails)

        viewRankingButton.alpha = 0.0F
        viewRankingButton.isVisible = false
    }

    private fun dismissOuchScoreView() {
        val ouchScoreEntrySubview: androidx.constraintlayout.widget.ConstraintLayout =
            findViewById(R.id.ouchConstraintLayoutDetails)

        ouchScoreEntrySubview.alpha = 0.0F
        ouchScoreEntrySubview.isVisible = false
    }

    private fun dismissAllSubviews() {

        dismissEndGameEarlyView()
        dismissEnterScoreView()

        dismissOuchScoreView()
        dismissViewRanking()
        dismissParEntry()
        dismissSavedGameEditConfirmationView()
    }

    private fun dismissParEntry() {
        val parEntryView: FrameLayout = findViewById(R.id.enterParViewDetails)

        parEntryView.alpha = 0.0F
        parEntryView.isVisible = false
    }

    private fun dismissSavedGameEditConfirmationView() {
        val overwriteSavedGameConfirmationView: FrameLayout = findViewById(R.id.overwriteSavedGameConfirmationView)

        overwriteSavedGameConfirmationView.alpha = 0.0F
        overwriteSavedGameConfirmationView.isVisible = false

    }

    private fun dismissViewRanking() {
        val viewRankingView: FrameLayout = findViewById(R.id.viewRankingViewDetails)
        val podiumDonebutton:Button = findViewById(R.id.podiumDoneButtonDetails)

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

                // Convert the player1ScoresJSON to string
                playerTotalsJSON.remove("player1Totals")
                    playerTotalsJSON.put("player1Totals", player1ScoresJSON)
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

                    playerTotalsJSON.remove("player2Totals")
                    playerTotalsJSON.put("player2Totals", player2ScoresJSON)
                    Log.d("PLAYER_SCORES", "Player Scores JSON = $playerTotalsJSON")
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
            }
        }

        updateCompletedHoleTracker(currentHole)

        sortTotalScores()
    }

    private fun presentOverwriteSavedGameConfirmationView() {
        val overwriteSavedGameConfirmationView: FrameLayout = findViewById(R.id.overwriteSavedGameConfirmationView)

        dismissEndGameButton()

        overwriteSavedGameConfirmationView.alpha = 1.0F
        overwriteSavedGameConfirmationView.isVisible = true
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

    private fun historicGame() {
        // See if the historic game achievement needs to be completed
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var historicValue = 0
        historicValue = sharedPref.getInt("historicGameVisited", 0)

        if (historicValue != 1) {
            editor.apply {
                // Save the number of games completed
                putInt("historicGameVisited", 1)
                apply()
            }
        }
    }
}