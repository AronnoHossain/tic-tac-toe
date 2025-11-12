package com.pinwheel.tictactoe.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pinwheel.tictactoe.enums.Difficulty
import com.pinwheel.tictactoe.enums.GameResult
import com.pinwheel.tictactoe.enums.PlayerMode
import com.pinwheel.tictactoe.enums.ScreenState
import com.pinwheel.tictactoe.theme.AppTheme

@Composable
fun TicTacToe() = AppTheme(dynamicColor = false) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        var screen by remember { mutableStateOf(ScreenState.MODE) }
        var playerMode by remember { mutableStateOf(PlayerMode.SINGLE) }
        var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
        var gameResult by remember { mutableStateOf<GameResult?>(null) }

        when (screen) {
            ScreenState.MODE -> ModeScreen(
                onSingle = { playerMode = PlayerMode.SINGLE; screen = ScreenState.DIFFICULTY },
                onMulti = { playerMode = PlayerMode.MULTI; screen = ScreenState.GAME }
            )

            ScreenState.DIFFICULTY -> DifficultyScreen(
                onPick = { difficulty = it; screen = ScreenState.GAME },
                onBack = { screen = ScreenState.MODE }
            )

            ScreenState.GAME -> GameScreenHost(
                playerMode = playerMode,
                difficulty = difficulty,
                onBackToMode = { screen = ScreenState.MODE },
                onGameResult = { result->
                    gameResult = result
                    screen = ScreenState.RESULT
                }
            )

            ScreenState.RESULT -> ResultScreen(
                result = gameResult ?: GameResult.DRAW,
                onPlayAgain = {
                    screen = ScreenState.GAME
                    gameResult = null
                },
                onStartOver = {
                    screen = ScreenState.MODE
                    gameResult = null
                }
            )
        }
    }
}