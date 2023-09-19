package com.example.tiptop

import android.widget.Toast
import androidx.lifecycle.ViewModel
import java.util.*

class GameViewModel: ViewModel() {

    //Locale Languages
    private val RUSSIAN_LANGUAGE = "ru"
    private val ENGLISH_LANGUAGE = "en"

    private var _currentLanguage = RUSSIAN_LANGUAGE
    val currentLanguage: String
        get() = _currentLanguage

    private var _numberOfPlayers = 6
    val numberOfPlayers: Int
        get() = _numberOfPlayers

    private var _taskList = listOf<String>()
    val taskList: List<String> get() = _taskList

    private var _taskCounter = 0
    val taskCounter: Int
        get()=_taskCounter

    private var _currentTask = "Placeholder"
    val currentTask : String
        get() = _currentTask

    private var _currentRound = 1
    val currentRound: Int
        get() = _currentRound

    private var _numberOfPoo = 0
    val numberOfPoo: Int
        get() = _numberOfPoo


    private var _numberOfUnicorns = 8
    val numberOfUnicorns: Int
        get() = _numberOfUnicorns

    private var _areNumbersMatchingPlayers = false
    val areNumbersMatchingPlayers: Boolean
        get() = _areNumbersMatchingPlayers

    init {
        setLanguage()
        loadTasks()
    }

    private fun loadTasks(){

        _taskList =
            if(_currentLanguage == RUSSIAN_LANGUAGE)
                allTasksListRussian.shuffled()
            else allTasksListEnglish.shuffled()

        _currentTask = taskList[taskCounter]
    }

    private fun setLanguage(){
        _currentLanguage = if(Locale.getDefault().language==ENGLISH_LANGUAGE)
            ENGLISH_LANGUAGE
        else RUSSIAN_LANGUAGE

    }

    fun changeLanguage(selectedLanguage: String){
        _currentLanguage =
            if(selectedLanguage == ENGLISH_LANGUAGE)
                ENGLISH_LANGUAGE
            else RUSSIAN_LANGUAGE
        loadTasks()

    }

    fun getNextTask(){
        _taskCounter++
        _currentTask = taskList[taskCounter]
        if(_taskCounter==99) _taskCounter=-1
    }

    fun updateUnicorns(){
            _numberOfPoo++
            _numberOfUnicorns--
    }

    fun updateNumberOfPlayers(i: Int)
    {
        _numberOfPlayers = i
    }

    fun updateRoundNumber() {
        _currentRound++
    }

    fun reinitializeData(){
        _taskList.shuffled()
        _taskCounter = 0
        _currentTask = taskList[taskCounter]
        _currentRound = 1
        _numberOfUnicorns = 8
        _numberOfPoo = 0
    }

    fun shuffleTaskList(){
        _taskList = _taskList.shuffled()
    }

    fun updateSelection(cond: Boolean){
        _areNumbersMatchingPlayers = cond
    }






}