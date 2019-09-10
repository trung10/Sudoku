package com.pdtrung.sudoku.activity

import com.pdtrung.sudoku.base.BaseViewModel
import com.pdtrung.sudoku.game.SudokuGame
import io.realm.Realm

class MainViewModel(private val realm: Realm, val sudokuGame: SudokuGame) : BaseViewModel() {

}