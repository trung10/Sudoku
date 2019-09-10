package com.pdtrung.sudoku.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdtrung.sudoku.SudokuApplication
import com.pdtrung.sudoku.activity.MainViewModel
import com.pdtrung.sudoku.game.SudokuGame
import io.realm.Realm

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    val db = Realm.getDefaultInstance()
    val game = (activity.application as SudokuApplication).getSudokuGame()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            //val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "data").build()
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db, game) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}