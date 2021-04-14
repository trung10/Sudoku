package com.pdtrung.sudoku.model

data class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var solvedValue: Int,
    var isStartingCell: Boolean = false
) {
    var notes = mutableSetOf<Int>()

    var isSolved = solvedValue == value

    private var _isAnimation: Boolean = false

    fun setAnimation(b: Boolean) {
        this._isAnimation = b
    }

    fun getAnimation(): Boolean {
        if (!_isAnimation)
            return _isAnimation

        _isAnimation = false
        return true
        
    }

}