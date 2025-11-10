package com.pinwheel.tictactoe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.pinwheel.tictactoe.theme.AppTheme

class LauncherActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(dynamicColor = false) {
                TicTacToeMainActivityContent()
            }
        }
    }
}