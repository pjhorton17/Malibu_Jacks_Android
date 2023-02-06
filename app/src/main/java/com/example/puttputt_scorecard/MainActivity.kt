package com.example.puttputt_scorecard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private var gameInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        // Set up all the in-app-purchases
       // setUpPurchases()

        setContentView(R.layout.activity_main)

        // Set up all of the buttons
        setUpAllButtons()

        // Load the game
        loadGame()

        // Dismiss all subviews
        dismissAllSubviews()

        // Clear all shared preferences
        clearAllSavedValues()
    }

    override fun onResume() {
        super.onResume()
        loadGame()
    }

   /* private fun setUpPurchases() {
        Purchases.debugLogsEnabled = true
        Purchases.configure(PurchasesConfiguration.Builder(this, "goog_DxwgndzueRcljJJmayCqBcjvsdH").build())
    }
    private fun makeTipPurchase(tipLevel: StoreProduct) {

        Purchases.sharedInstance.purchaseProductWith(
            this,
            tipLevel,
            onError = { error, userCancelled -> /* No purchase */println("Purchase failed") },

            onSuccess = { product, customerInfo ->
                if (customerInfo.entitlements["ads_removed_entitlement"]?.isActive == true) {
                    // Unlock that great "pro" content (i.e. remove ads)
                    println("Purchase worked")
                }
            })
    }

    private fun purchaseLevelOne() {

        Purchases.sharedInstance.getOfferingsWith({ error ->
            // An error occurred
        }) { offerings ->
            //val product = offerings.current.monthly.product.skuDetails {
                // Get the price and introductory period from the SkuDetails
                //val levelOneTip = product

                //  makeTipPurchase(levelOneTip)
                // }
            }
        } */

    private fun setUpAllButtons() {

        // Set up the achievements button
        val achievementsButton: android.widget.Button = findViewById(R.id.achievementsButton)
        achievementsButton.setOnClickListener {
            val intent = Intent(this, Achievements::class.java)
            startActivity(intent)
        }

        val newGameButton: android.widget.Button = findViewById(R.id.newGameButton)
        newGameButton.setOnClickListener {
            // Check if there is a game in progress
            if (gameInProgress) {
                // Present the newGameConfirmationView
                val newGameConfirmationView: android.widget.FrameLayout = findViewById(R.id.newGameConfirmationView)
                newGameConfirmationView.alpha = 1.0F

                val noNewGameButton: android.widget.Button = findViewById(R.id.noNewGameButton)
                noNewGameButton.alpha = 1.0F
                noNewGameButton.isEnabled = true

                val yesNewGameButton: android.widget.Button = findViewById(R.id.yesNewGameButton)
                yesNewGameButton.alpha = 1.0F
                yesNewGameButton.isEnabled = true

                // Disable all main view buttons
                disableAllMainButtons()

                // Enable the new game confirmation options
                enableNewGameConfirmationButtons()
            }

            else {
                val intent = Intent(this, Location::class.java)
                setGameInProgressToFalse()
                startActivity(intent)
            }
        }

        val continueButton: android.widget.Button = findViewById(R.id.continueButton)
        continueButton.setOnClickListener {
            val intent = Intent(this, Scorecard::class.java)
            if (gameInProgress) {
                // Check how many players
                val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
                val numOfPlayers = sharedPref.getInt("numberOfPlayers", 1)
                intent.putExtra("numberOfPlayers", numOfPlayers)

                // Get the names of the players
                val playerNames = sharedPref.getString("playerNames", "")
                intent.putExtra("playerNamesList", playerNames)
                startActivity(intent)
            }
        }

        val noNewGameButton: android.widget.Button = findViewById(R.id.noNewGameButton)
        noNewGameButton.alpha = 0.0F
        noNewGameButton.isEnabled = false
        noNewGameButton.setOnClickListener {
            // Dismiss the newGameConfirmationView
            dismissNewGameConfirmationView()

            // Disable the new game confirmation options
            disableNewGameConfirmationButtons()
        }

        val yesNewGameButton: android.widget.Button = findViewById(R.id.yesNewGameButton)
        yesNewGameButton.alpha = 0.0F
        yesNewGameButton.isEnabled = false

        yesNewGameButton.setOnClickListener {
            val intent = Intent(this, Location::class.java)
            setGameInProgressToFalse()
            dismissNewGameConfirmationView()

            // Disable the new game confirmation options
            disableNewGameConfirmationButtons()

            startActivity(intent)
        }

        val rateAppButton: android.widget.Button = findViewById(R.id.rateAppButton)
        rateAppButton.setOnClickListener {
            try {
                appRated()
                startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("market://details?id=$packageName")))

            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }

        val shareAppButton: android.widget.Button = findViewById(R.id.shareAppButton)
        shareAppButton.setOnClickListener {
            // Share the app
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "https://play.google.com/store/apps/details?id=com.patrick.puttputt_scorecard&pli=1"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "App link")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            appShared()
            startActivity(Intent.createChooser(sharingIntent, "Share App Link Via:"))

        }

        val historyButton: android.widget.Button = findViewById(R.id.historyButton)
        historyButton.setOnClickListener {
            // Navigate to the historyView
            val intent = Intent(this, History::class.java)
            startActivity(intent)
        }

        val tipButton: android.widget.ImageButton = findViewById(R.id.tipButton)
        tipButton.alpha = 0.0F
        tipButton.isEnabled = false

       /* tipButton.setOnClickListener {
            // Present tip view
            val tipView: android.widget.FrameLayout = findViewById(R.id.tipView)
            val restorePurchasesButton: android.widget.Button = findViewById(R.id.restorePurchasesButton)
            restorePurchasesButton.alpha = 1.0F
            restorePurchasesButton.isEnabled = true
            tipView.alpha = 1.0F
            tipView.isVisible = true

            Purchases.sharedInstance.getOfferingsWith({ error ->
                // An error occurred
                println("Couldn't get offerings")
            }) { offerings ->
                offerings.current?.availablePackages?.takeUnless { it.isNullOrEmpty() }?.let {
                    // Display packages for sale
                    var current = offerings.current
                    println("Offerings: $current")
                }
            }

             Purchases.sharedInstance.getOfferingsWith(
                onError = { error ->
                    /* Optional error handling */
                    println("Couldn't get offerings")
                },
                onSuccess = { offerings ->
                    // Display current offering with offerings.current
                    var current = offerings.current
                    println("Offerings: $current")
                })


           // Purchases.sharedInstance.purchasePackage(this, Package("level_one_tip", PackageType.UNKNOWN, ))

            Purchases.sharedInstance.purchaseProductWith(
                this,
                tipLevel,
                onError = { error, userCancelled -> /* No purchase */println("Purchase failed") },

                onSuccess = { product, customerInfo ->
                    if (customerInfo.entitlements["ads_removed_entitlement"]?.isActive == true) {
                        // Unlock that great "pro" content (i.e. remove ads)
                        println("Purchase worked")
                    }
                })

            // Make the main menu buttons disabled
            disableAllMainButtons()
        } */

       /* val exitTipButton: android.widget.Button = findViewById(R.id.exitTipButton)
        exitTipButton.setOnClickListener {
            // Exit tip view
            dismissTipView()

            // Enable all of the main menu buttons
            enableAllMainButtons()
        } */

        val aboutButton: android.widget.Button = findViewById(R.id.aboutButton)
        aboutButton.setOnClickListener {
            // Present the about screen
            val aboutView: android.widget.FrameLayout = findViewById(R.id.aboutView)
            aboutView.alpha = 1.0F

            disableAllMainButtons()
        }

        val exitAboutScreen: android.widget.Button = findViewById(R.id.exitAboutScreenButton)
        exitAboutScreen.setOnClickListener {
            // Present the about screen
            val aboutView: android.widget.FrameLayout = findViewById(R.id.aboutView)
            aboutView.alpha = 0.0F

            enableAllMainButtons()
        }

        // Set up the privacy policy link
        val privacyPolicyTV: android.widget.TextView = findViewById(R.id.privacyPolicyLinkTV)
        privacyPolicyTV.linksClickable = true
        privacyPolicyTV.movementMethod = LinkMovementMethod()
        val link:String = "<a href='https://minigolfscorecard.godaddysites.com/privacy-policy-android'> Tap Here </a>"
        privacyPolicyTV.text = Html.fromHtml(link)
    }

    private fun loadGame() {
        // Check if a game is in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        gameInProgress = sharedPref.getBoolean("gameInProgress", false)

        if (gameInProgress) {
            val continueButton: android.widget.Button = findViewById(R.id.continueButton)
            continueButton.isEnabled = true
            continueButton.setTextColor(Color.BLACK)
        } else {
            // Deactivate continue button
            val continueButton: android.widget.Button = findViewById(R.id.continueButton)
            continueButton.isEnabled = false
            continueButton.setTextColor(Color.GRAY)
        }
    }

    private fun clearAllSavedValues() {

        val sharedPreference =
            getSharedPreferences("GAME_IN_PROGRESS", android.content.Context.MODE_PRIVATE)
        sharedPreference.edit().clear().apply()
    }

    private fun setGameInProgressToFalse() {
        // Set the GameInProgress value to "False"
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save game in progress value
            putBoolean("gameInProgress", false)
            remove("player1Scores")
            apply() // asynchronously saves data, where as commit() synchronously saves, blocking main thread
        }
    }

    private fun dismissAllSubviews() {
        dismissNewGameConfirmationView()
        dismissTipView()
        dismissAboutView()
    }

    private fun dismissAboutView() {
        // Dismiss the newGameConfirmationView
        val aboutView: android.widget.FrameLayout = findViewById(R.id.aboutView)
        aboutView.alpha = 0.0F

        // enable all main view buttons
        enableAllMainButtons()
    }
    private fun dismissNewGameConfirmationView() {
        // Dismiss the newGameConfirmationView
        val newGameConfirmationView: android.widget.FrameLayout = findViewById(R.id.newGameConfirmationView)
        newGameConfirmationView.alpha = 0.0F

        // enable all main view buttons
        enableAllMainButtons()
    }

    private fun dismissTipView() {
        // Dismiss the newGameConfirmationView
        val tipView: android.widget.FrameLayout = findViewById(R.id.tipView)
        val restorePurchasesButton: android.widget.Button = findViewById(R.id.restorePurchasesButton)
        restorePurchasesButton.alpha = 0.0F
        restorePurchasesButton.isEnabled = false
        tipView.alpha = 0.0F
        tipView.isVisible = false
    }

    private fun appRated() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("appRated", 1)
            apply()
        }
    }

    private fun appShared() {
        // See if there is a game in progress
        val sharedPref =  getSharedPreferences("myPreferences", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            // Save the number of games completed
            putInt("appShared", 1)
            apply()
        }
    }

    private fun disableAllMainButtons() {
        val historyButton: android.widget.Button = findViewById(R.id.historyButton)
        val newGameButton: android.widget.Button = findViewById(R.id.newGameButton)
        val continueButton: android.widget.Button = findViewById(R.id.continueButton)
        val achievementsButton: android.widget.Button = findViewById(R.id.achievementsButton)
        val rateAppButton: android.widget.Button = findViewById(R.id.rateAppButton)
        val shareAppButton: android.widget.Button = findViewById(R.id.shareAppButton)

        val aboutButton: android.widget.Button = findViewById(R.id.aboutButton)

        newGameButton.isEnabled = false
        continueButton.isEnabled = false
        historyButton.isEnabled = false
        achievementsButton.isEnabled = false
        rateAppButton.isEnabled = false
        shareAppButton.isEnabled = false
        aboutButton.isEnabled = false
    }

    private fun enableNewGameConfirmationButtons() {
        val yesNewGameButton: android.widget.Button = findViewById(R.id.yesNewGameButton)
        val noNewGameButton: android.widget.Button = findViewById(R.id.noNewGameButton)

        yesNewGameButton.isEnabled = true
        noNewGameButton.isEnabled = true
    }

    private fun disableNewGameConfirmationButtons() {
        val yesNewGameButton: android.widget.Button = findViewById(R.id.yesNewGameButton)
        val noNewGameButton: android.widget.Button = findViewById(R.id.noNewGameButton)

        yesNewGameButton.isEnabled = false
        noNewGameButton.isEnabled = false
    }

    private fun enableAllMainButtons() {
        val historyButton: android.widget.Button = findViewById(R.id.historyButton)
        val newGameButton: android.widget.Button = findViewById(R.id.newGameButton)
        val continueButton: android.widget.Button = findViewById(R.id.continueButton)
        val achievementsButton: android.widget.Button = findViewById(R.id.achievementsButton)
        val rateAppButton: android.widget.Button = findViewById(R.id.rateAppButton)
        val shareAppButton: android.widget.Button = findViewById(R.id.shareAppButton)
        val aboutButton: android.widget.Button = findViewById(R.id.aboutButton)

        newGameButton.isEnabled = true
        continueButton.isEnabled = true
        historyButton.isEnabled = true
        achievementsButton.isEnabled = true
        rateAppButton.isEnabled = true
        shareAppButton.isEnabled = true
        aboutButton.isEnabled = true
    }
}