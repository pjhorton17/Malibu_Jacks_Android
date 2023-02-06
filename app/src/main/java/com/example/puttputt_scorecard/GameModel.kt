package com.example.puttputt_scorecard

class GameModel (gameIDTS: Int, locationTS: String, winningScoreTS: Int, winningPlayerTS: String, dateTS: String,
                 totalScoreArrayTS: String, playerNamesTS: String, playerScoreArrayTS: String) {
    // ID for the game
    var id = gameIDTS

    // Data for history summary table
    private var location = locationTS
    private var winningScore = winningScoreTS
    private var winningPlayer = winningPlayerTS
    private var date = dateTS

    // Data for full detail scorecard
    private  var totalScoreArray = totalScoreArrayTS
    private var playerNames = playerNamesTS
    private var playerScoreArray = playerScoreArrayTS

    override fun toString(): String {
        return "gameModel(id=$id, " +
                "location=$location, " +
                "winningScore=$winningScore, " +
                "winningPlayer=$winningPlayer, " +
                "date=$date, " +
                "totalScoreArray=$totalScoreArray, " +
                "playerNames=$playerNames, " +
                "playerScoreArray=$playerScoreArray)"
    }

    // Constructors

    // Getters & Setters

    // ToString

}