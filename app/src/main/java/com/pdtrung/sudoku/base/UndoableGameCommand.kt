package com.pdtrung.sudoku.base

import com.pdtrung.sudoku.base.`interface`.UndoableCommand
import com.pdtrung.sudoku.game.SudokuGame
import com.pdtrung.sudoku.model.Cell

abstract class UndoableGameCommand(val mGame: SudokuGame) : UndoableCommand {
    private val oldCell: Cell = mGame.getSelectedCell().copy()

    override fun undo() {
        mGame.undo(oldCell)
    }
}