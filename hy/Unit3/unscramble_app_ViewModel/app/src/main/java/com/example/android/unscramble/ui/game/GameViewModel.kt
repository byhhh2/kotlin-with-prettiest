package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _score = 0
    val score : Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount : Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord : String
    val currentScrambledWord : String
        get() = _currentScrambledWord

    // 게임에 사용한 단어 리스트
    private var wordsList: MutableList<String> = mutableListOf()
    // 플레이어가 추측해야할 단어
    private lateinit var currentWord: String

    private var _wordtest = "test"
    val wordtest :String
        get() = _wordtest

    //객체 인스턴스가 처음 생성되어 초기화될 때 실행
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        _wordtest= currentWord
        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    //도우미 메서드
    fun nextWord() : Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        }
        else false
    }

    private fun increaseScore(){
        _score += SCORE_INCREASE
    }

    //도우미 메서드
    fun isUserWordCorrect(playerWord :String) : Boolean {
        if(playerWord.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }

    fun reinitalizeData(){
        _score =0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    // 소멸 직전 호출
    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel Destroyed!")
    }
}