package com.pdtrung.sudoku.game

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.patrickfeltes.sudokuandroid.common.GameTimer
import com.pdtrung.sudoku.model.Board
import com.pdtrung.sudoku.model.Cell
import com.pdtrung.sudoku.model.TimeInfo

class SudokuGame(context: Context) : GameTimer.GameTimerListener {

    val cellsLiveData = MutableLiveData<List<Cell>>()
    val selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()
    val gameTimeLiveData = MutableLiveData<TimeInfo>()
    val isPuzzleSolved = MutableLiveData<Boolean>()

    // all data in here
    private val board =
        Board.createAutoBoard(30) //Board.createBoardFromFile(context, "sampleBoard.txt")
    private val gameTimer = GameTimer()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false

    init {
        cellsLiveData.postValue(board.cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        isTakingNotesLiveData.postValue(isTakingNotes)
        gameTimer.registerListener(this)
        board.setListener(object : Board.PuzzleListener {
            override fun isPuzzleSolved(bool: Boolean) {
                isPuzzleSolved.postValue(bool)
            }

            override fun isGameOVer() {

            }
        })
    }

    fun getSelectedCell(): Cell = board.getCell(selectedRow, selectedCol)

    fun isSelectedCell() = !(selectedRow == -1 || selectedCol == -1)

    fun handleInput(value: Int) {
        if (!isSelectedCell()) return
        if (isTakingNotes) {
            val cell = board.getCell(selectedRow, selectedCol)

            cell.value = 0 // reset value

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

    fun erase() {
        board.erase(selectedRow, selectedCol)
        cellsLiveData.postValue(board.cells)
    }

    fun hint() {
        board.hint(selectedRow, selectedCol)
        cellsLiveData.postValue(board.cells)
    }

    fun undo(cell: Cell) {
        selectedCol = cell.col
        selectedRow = cell.row

        board.updateCell(cell)

        //todo update ui
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
    }

    override fun publishTime(time: TimeInfo) {
        gameTimeLiveData.postValue(time)
    }
}