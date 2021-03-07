package com.pdtrung.sudoku.base

import com.pdtrung.sudoku.game.SudokuGame

class EraseCommand(game: SudokuGame) : UndoableGameCommand(game) {

    override fun execute() {
        mGame.erase()
    }
}