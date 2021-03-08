package com.pdtrung.sudoku.activity

import com.pdtrung.sudoku.base.BaseViewModel
import com.pdtrung.sudoku.base.CommandManager
import com.pdtrung.sudoku.base.EraseCommand
import com.pdtrung.sudoku.base.FillCommand
import com.pdtrung.sudoku.game.SudokuGame

class MainViewModel(val sudokuGame: SudokuGame) : BaseViewModel() {

    private val commandManager: CommandManager = CommandManager()

    fun erase() {
        commandManager.executeCommand(EraseCommand(sudokuGame))
    }

    fun fill(value: Int) {
        commandManager.executeCommand(FillCommand(sudokuGame, value))

    }

    fun notes() {
        sudokuGame.changeNoteState()
        //commandManager.executeCommand(EraseCommand(sudokuGame))
    }

    fun undo() {
        commandManager.undo()
    }
}