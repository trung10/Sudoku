package com.pdtrung.sudoku.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.pdtrung.sudoku.model.Cell

class SudokuView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private val TAG = "SudokuView"

    private val sqrtSize = 3
    private val size = sqrtSize * sqrtSize
    private var selectedRow = -1
    private var selectedCol = -1

    private var cellWidth = (width / size).toFloat()
    private var cellHeight = (height / size).toFloat()

    private var cells: List<Cell>? = null
    private var listener: SudokuBoardTouchListener? = null

    private var noteWidth = (width / size) / sqrtSize.toFloat()

    private val regularLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val selectedPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
    }

    private val selectedSameRowColPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    private val noteTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)

        updatePaints(size)
    }

    private fun updatePaints(width: Int) {
        val cellWidth = width / size

        noteTextPaint.textSize = cellWidth / sqrtSize.toFloat()
        textPaint.textSize = cellWidth / 1.5F
        startingCellTextPaint.textSize = cellWidth / 1.5F
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        cellWidth = (width / size.toFloat())
        cellHeight = (height / size.toFloat())
        noteWidth = cellWidth / sqrtSize.toFloat()

        fillCells(canvas)
        drawLines(canvas)
        drawNumbers(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        cells?.forEach { cell ->
            val row = cell.row
            val col = cell.col

            if (cell.isStartingCell) {
                fillCell(canvas, row, col, startingCellPaint)
            } else if (selectedRow == -1 || selectedCol == -1) {
                // do nothing
            } else if (row == selectedRow && col == selectedCol) {
                fillCell(canvas, row, col, selectedPaint)
            } else if (row == selectedRow || col == selectedCol) {
                fillCell(canvas, row, col, selectedSameRowColPaint)
            } else if (row / sqrtSize == selectedRow / sqrtSize && col / sqrtSize == selectedCol / sqrtSize) {
                fillCell(canvas, row, col, selectedSameRowColPaint)
            }
        }
    }

    private fun fillCell(canvas: Canvas, row: Int, col: Int, paint: Paint) = canvas.drawRect(
        col * cellWidth,
        row * cellHeight,
        (col + 1) * cellWidth,
        (row + 1) * cellHeight,
        paint
    )

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        (1 until size).forEach {
            val paintToUse = when (it % sqrtSize) {
                0 -> thickLinePaint
                else -> regularLinePaint
            }
            canvas.drawLine(it * cellWidth, 0F, it * cellWidth, height.toFloat(), paintToUse)
            canvas.drawLine(0F, it * cellHeight, width.toFloat(), it * cellHeight, paintToUse)
        }
    }

    private fun drawNumbers(canvas: Canvas) {
        cells?.forEach { cell ->
            drawCell(canvas, cell)
        }
    }

    private fun drawCell(canvas: Canvas, cell: Cell) {
        val value = cell.value
        if (value == 0) {
            // draw note numbers
            cell.notes.forEach {
                val noteValue = it
                val rowInCell = (it - 1) / sqrtSize
                val colInCell = (it - 1) % sqrtSize

                val valueString = noteValue.toString()
                val textBounds = Rect()

                noteTextPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = noteTextPaint.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(
                    valueString,
                    (cell.col * cellWidth) + (colInCell * noteWidth) + noteWidth / 2 - (textWidth / 2f),
                    (cell.row * cellHeight) + (rowInCell * noteWidth) + noteWidth / 2 + (textHeight / 2f),
                    noteTextPaint
                )
            }
        } else {
            // draw regular numbers
            val row = cell.row
            val col = cell.col
            val valueString = value.toString()

            val paintToUse = when (cell.isStartingCell) {
                true -> startingCellTextPaint
                false -> textPaint
            }

            val textBounds = Rect()
            paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
            val textWidth = paintToUse.measureText(valueString)
            val textHeight = textBounds.height()

            canvas.drawText(
                valueString,
                (col * cellWidth) + cellWidth / 2 - (textWidth / 2f),
                (row * cellHeight) + cellHeight / 2 + (textHeight / 2f),
                paintToUse
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val rowTouched = (y / cellHeight).toInt()
        val colTouched = (x / cellWidth).toInt()
        listener?.cellSelected(rowTouched, colTouched)
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    fun updateSelectedCell(row: Int, col: Int) {
        this.selectedRow = row
        this.selectedCol = col
        invalidate()
    }

    fun registerListener(listener: SudokuBoardTouchListener) {
        this.listener = listener
    }

    interface SudokuBoardTouchListener {
        fun cellSelected(row: Int, col: Int)
    }
}