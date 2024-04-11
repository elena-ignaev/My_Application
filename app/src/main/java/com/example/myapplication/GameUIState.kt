package com.example.myapplication

data class GameUIState(
    val currentScrambleWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isGuessedWordWrong: Boolean = false
)