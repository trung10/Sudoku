package com.pdtrung.sudoku.base

import com.pdtrung.sudoku.game.SudokuGame

class FillCommand(game: SudokuGame, private val value: Int) : UndoableGameCommand(game) {

    override fun execute() {
        mGame.handleInput(value)
    }
}