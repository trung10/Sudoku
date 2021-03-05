package com.pdtrung.sudoku.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdtrung.sudoku.SudokuApplication
import com.pdtrung.sudoku.activity.MainViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    val game = (activity.application as SudokuApplication).getSudokuGame()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            //val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "data").build()
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(game) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}