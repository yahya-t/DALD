package com.example.dald.Game

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dald.MainActivity
import com.example.dald.R
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class GameMainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var playerOneScore: TextView
    lateinit var playerTwoScore: TextView
    lateinit var playerStatus: TextView

    lateinit var resetGame: Button

    private val buttons = arrayOfNulls<Button>(9)

    var playerOneScoreCount by Delegates.notNull<Int>()
    var playerTwoScoreCount by Delegates.notNull<Int>()
    var roundCount by Delegates.notNull<Int>()

    var activePlayer by Delegates.notNull<Boolean>()

    var gameState = arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    var winningPositions = arrayOf(intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6))

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_main)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        playerOneScore = findViewById(R.id.playerOneScore)
        playerTwoScore = findViewById(R.id.playerTwoScore)
        playerStatus = findViewById(R.id.playerStatus)

        resetGame = findViewById(R.id.restGame)

        for (i in buttons.indices) {
            var buttonID = "btn_$i"
            var resourceID = resources.getIdentifier(buttonID, "id", packageName)
            buttons[i] = findViewById(resourceID)
            buttons[i]?.setOnClickListener(this)
        }

        roundCount = 0
        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        activePlayer = true

    }

    override fun onClick(v: View?) {
        if (!(v as Button).text.equals("")) {
            return
        }
        var buttonID = v.resources.getResourceEntryName(v.id)
        var gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

        if (activePlayer == true) {
            v.text = "X"
            v.setTextColor(Color.parseColor("#FFC34A"))
            gameState[gameStatePointer] = 0
            activePlayer = false
        } else {
            v.text = "O"
            v.setTextColor(Color.parseColor("#70FFEA"))
            gameState[gameStatePointer] = 1
            activePlayer = true
        }
        roundCount++

        if (checkWinner()) {
            if (activePlayer == false) {
                playerOneScoreCount++
                updatePlayerScore()
                Toast.makeText(this, "X is the winner!", Toast.LENGTH_LONG).show()
                TimeUnit.SECONDS.sleep(1)
                playAgain()
            } else {
                playerTwoScoreCount++
                updatePlayerScore()
                Toast.makeText(this, "O is the winner!", Toast.LENGTH_LONG).show()
                playAgain()
            }
        } else if (roundCount == 9) {
            playAgain()
            Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show()
        } else {
            activePlayer != activePlayer
        }

        if(playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.text = "X is Winning!"
        } else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.text = "O is Winning!"
        } else {
            playerStatus.text = ""
        }

        resetGame.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                playAgain()
                playerOneScoreCount = 0
                playerTwoScoreCount = 0
                playerStatus.text = ""
                updatePlayerScore()
            }

        })
    }

    fun checkWinner(): Boolean {
        var winnerResult = false
        for (winningPosition in winningPositions) {
            if (gameState[winningPosition[0]] == gameState.get(winningPosition[1]) && gameState.get(
                    winningPosition[1]) == gameState[winningPosition[2]] && gameState.get(
                    winningPosition[0]) != 2
            ) {
                winnerResult = true
            }
        }
        return winnerResult
    }

    fun updatePlayerScore() {
        playerOneScore.text = playerOneScoreCount.toString()
        playerTwoScore.text = playerTwoScoreCount.toString()
    }

    fun playAgain() {
        roundCount = 0
        activePlayer = true


        for (i in 0 until buttons.size) {
            gameState[i] = 2
            buttons[i]?.text = ""
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun returnHomeFromNotes(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}