package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState

    private var usedWords = mutableSetOf<String>()

    // for primitive data type, dont need to create 2 objects to make setter a private function, we just need to private set
    var userInput by mutableStateOf("")
        private set

    private var currentWord: String = ""

    init {
        startGame()
    }

    fun onInputChange(newWord:String) {
        userInput = newWord
    }

    private fun startGame() {
        usedWords.clear()
        // write the whole thing to make the state value more understandable
        _uiState.value = GameUIState(
            currentScrambleWord = pickRandomWordAndShuffle() ?: "",
            isGameOver = false,
            score = 0,
            currentWordCount = 1,
            isGuessedWordWrong = false
        )
    }

    private fun shuffleWord(word: String) : String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle() : String? {
        if(usedWords.size == allWords.size) {
            return null
        }
        currentWord = allWords.random()
        while (usedWords.contains(currentWord)) {
            currentWord = allWords.random()
        }
        usedWords.add(currentWord)
        return shuffleWord(currentWord)
    }

    fun onSubmit() {
        if(userInput == currentWord) {
            _uiState.value = _uiState.value.copy(
                score = _uiState.value.score + 1,
                currentWordCount = _uiState.value.currentWordCount + 1,
                isGuessedWordWrong = false

            )
            onNextWord()
        } else {
            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = true
            )
        }
        userInput = ""
    }

    fun onSkip() {
        _uiState.value = _uiState.value.copy(
            currentWordCount = if (_uiState.value.currentWordCount < 10) _uiState.value.currentWordCount + 1 else _uiState.value.currentWordCount
        )
        onNextWord()
    }

    fun onPlayAgain() {
        startGame()
        _uiState.value = _uiState.value.copy(
            currentWordCount = 1,
            score = 0,
            isGameOver = false
        )
    }

    private fun onNextWord() {
        val nextWord = pickRandomWordAndShuffle()
        if (nextWord==null) {
            _uiState.value = _uiState.value.copy(
                isGameOver = true
            )
        } else {
            _uiState.update {
                it.copy(
                    currentScrambleWord = nextWord
                )
            }
        }
    }
}