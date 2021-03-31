package com.pdtrung.sudoku.base

import com.pdtrung.sudoku.base.`interface`.Command
import com.pdtrung.sudoku.base.`interface`.UndoableCommand
import java.util.*

class CommandManager {
    private val undoableStack = Stack<UndoableCommand>()

    fun executeCommand(command: Command) {
        command.execute()

        if (command is UndoableCommand)
            undoableStack.push(command)
    }

    fun undo() {
        if (undoableStack.size > 0) {
            val command = undoableStack.pop()
            command.undo()
        }
    }

}