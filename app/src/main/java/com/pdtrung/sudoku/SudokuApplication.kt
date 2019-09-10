package com.pdtrung.sudoku

import android.app.Application
import com.pdtrung.sudoku.game.SudokuGame
import io.realm.Realm

@Suppress("unused")
class SudokuApplication : Application() {
    private lateinit var game: SudokuGame

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        game = SudokuGame(this)
    }

    fun getSudokuGame(): SudokuGame{
        return  game
    }
}