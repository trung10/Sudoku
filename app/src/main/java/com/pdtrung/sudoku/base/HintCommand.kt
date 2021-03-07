package com.pdtrung.sudoku.base

import com.pdtrung.sudoku.base.`interface`.Command
import com.pdtrung.sudoku.game.SudokuGame

class HintCommand(private val game: SudokuGame) : Command {
    override fun execute() {
        game.hint()
    }
}