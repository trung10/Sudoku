package com.pdtrung.sudoku.game

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.patrickfeltes.sudokuandroid.common.GameTimer
import com.pdtrung.sudoku.model.TimeInfo
import com.pdtrung.sudoku.model.Board
import com.pdtrung.sudoku.model.Cell

class SudokuGame(context: Context) : GameTimer.GameTimerListener {

    val cellsLiveData = MutableLiveData<List<Cell>>()
    val selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()
    val gameTimeLiveData = MutableLiveData<TimeInfo>()
    val isPuzzleSolved = MutableLiveData<Boolean>()

    private val board = Board.createAutoBoard(50) //Board.createBoardFromFile(context, "sampleBoard.txt")
    private val gameTimer = GameTimer()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false

    init {
        cellsLiveData.postValue(board.cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        isTakingNotesLiveData.postValue(isTakingNotes)
        gameTimer.registerListener(this)
        board.setListener(object : Board.PuzzleSolvedListener {
            override fun isPuzzleSolved(bool: Boolean) {
                isPuzzleSolved.postValue(bool)
            }
        })
    }

    fun handleInput(value: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        if (isTakingNotes) {
            val cell = board.getCell(selectedRow, selectedCol)
            if (cell.notes.contains(value)) {
                cell.notes.remove(value)
            } else {
                cell.notes.add(value)
            }
            cellsLiveData.postValue(board.cells)
            highlightedKeysLiveData.postValue(cell.notes)
        } else {
            board.updateCell(selectedRow, selectedCol, value)
            cellsLiveData.postValue(board.cells)
        }
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (!board.isStartingCell(row, col)) {
            this.selectedRow = row
            this.selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))
            if (isTakingNotes) {
                highlightedKeysLiveData.postValue(board.getCell(row, col).notes)
            }
        }
    }

    fun changeNoteState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        if (isTakingNotes) {
            highlightedKeysLiveData.postValue(board.getCell(selectedRow, selectedCol).notes)
        } else {
            highlightedKeysLiveData.postValue(setOf())
        }
    }

    fun delete() {
        if (isTakingNotes) {
            board.getCell(selectedRow, selectedCol).notes.clear()
            highlightedKeysLiveData.postValue(setOf())
        } else {
            board.getCell(selectedRow, selectedCol).value = 0
        }
        cellsLiveData.postValue(board.cells)
    }

    fun onPause() {
        gameTimer.onPause()
    }

    fun onResume() {
        gameTimer.onResume()
    }

    override fun publishTime(time: TimeInfo) {
        gameTimeLiveData.postValue(time)
    }
}