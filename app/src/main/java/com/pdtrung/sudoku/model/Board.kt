package com.pdtrung.sudoku.model

import android.content.Context
import android.util.Log
import com.pdtrung.sudoku.game.Generator

class Board(val size: Int, val cells: List<Cell>, private val solvedCells: List<Cell>) {
    private var listener: PuzzleSolvedListener? = null

    fun updateCell(row: Int, col: Int, value: Int) {
        if (row != -1 && col != -1) {
            getCell(row, col).value = value
        }

        if (isSolved()) {
            Log.d("Board", "Puzzle Solved")
            if (listener != null) {
                listener!!.isPuzzleSolved(true)
            }
        }
    }

    fun isStartingCell(row: Int, col: Int): Boolean {
        if (row != -1 && col != -1) {
            return getCell(row, col).isStartingCell
        }
        return false
    }

    private fun isSolved(): Boolean {
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (getCell(row, col).value != getSolvedCell(row, col).value) {
                    return false
                }
            }
        }

        return true
    }

    fun setListener(listener: PuzzleSolvedListener) {
        this.listener = listener
    }

    fun getCell(row: Int, col: Int) = cells[row * size + col]
    private fun getSolvedCell(row: Int, col: Int) = solvedCells[row * size + col]

    companion object {
        private var generator = Generator()

        fun createBoardFromFile(context: Context, fileName: String): Board {
            val inputStream = context.assets.open(fileName)
            val lines = inputStream.bufferedReader().use { it.readLines() }

            val size = lines[0].toInt()

            val cells = List(size * size) { i -> Cell(i / size, i % size, 0) }
            val solvedCells = List(size * size) { i -> Cell(i / size, i % size, 0) }

            // fill in unsolved board
            for (i in 1..size) {
                lines[i].forEachIndexed { index, c ->
                    cells[(i - 1) * size + index].apply {
                        if (c != '.') {
                            value = c.toString().toInt()
                            isStartingCell = (c != '.')
                        }
                    }
                }
            }

            // fill in solved board
            for (i in (size + 2)..(2 * size + 1)) {
                lines[i].forEachIndexed { stringIndex, c ->
                    solvedCells[(i - size - 2) * size + stringIndex].value = c.toString().toInt()
                }
            }

            for (y in 0..8) {
                var s = MutableList(9) {
                    0
                }
                for (w in 0..8) {
                    s[w] = cells[y * 9 + w].value

                }
                Log.e("cells", "$s)")
            }

            for (y in 0..8) {
                var s = MutableList(9) {
                    0
                }
                for (w in 0..8) {
                    s[w] = solvedCells[y * 9 + w].value

                }
                Log.e("solvedCells", "$s)")
            }

            return Board(size, cells, solvedCells)
        }

        /**
         * @param k: level of puzzles
         */
        fun createAutoBoard(k: Int): Board {
            generator.generatorA(9, k)
            generator.fillValues()
            var data = generator.puzzles

            val size = 9

            var solvedCells =
                MutableList(9 * 9) { index -> Cell(index / size, index % size, data[index / size][index % size]) }

            generator.removeKDigits()

            data = generator.puzzles

            var cells = List(9 * 9) { index -> Cell(index / size, index % size, data[index / size][index % size]).apply {
                if (data[index / size][index % size] != 0){
                    isStartingCell = true
                }
            } }


            return Board(size, cells, solvedCells)
        }
    }

    interface PuzzleSolvedListener {
        fun isPuzzleSolved(bool: Boolean)
    }
}