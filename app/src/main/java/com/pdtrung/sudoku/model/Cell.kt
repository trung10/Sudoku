package com.pdtrung.sudoku.model

data class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var isStartingCell: Boolean = false
) {
    var notes = mutableSetOf<Int>()
}