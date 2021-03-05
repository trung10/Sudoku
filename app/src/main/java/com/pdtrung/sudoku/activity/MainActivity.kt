package com.pdtrung.sudoku.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.pdtrung.sudoku.model.TimeInfo
import com.pdtrung.sudoku.R
import com.pdtrung.sudoku.base.BaseActivity
import com.pdtrung.sudoku.model.Cell
import com.pdtrung.sudoku.view.SudokuView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), SudokuView.SudokuBoardTouchListener {
    private val TAG = "MainActivity"
    private lateinit var numberButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView.registerListener(this)

        (viewModel as MainViewModel).sudokuGame.cellsLiveData.observe(this, Observer {
            updateCells(it)
        })
        (viewModel as MainViewModel).sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCell(it) })
        (viewModel as MainViewModel).sudokuGame.isTakingNotesLiveData.observe(this, Observer { updateNoteTakingUi(it) })
        (viewModel as MainViewModel).sudokuGame.highlightedKeysLiveData.observe(
            this,
            Observer { updateHighlightedKeys(it) })
        (viewModel as MainViewModel).sudokuGame.gameTimeLiveData.observe(this, Observer { updateTimer(it) })
        (viewModel as MainViewModel).sudokuGame.isPuzzleSolved.observe(this, Observer {
            Toast.makeText(this, "Puzzle Solved", Toast.LENGTH_LONG).show()
            // todo implement dialog solved
        })

        numberButtons = listOf(
            oneButton,
            twoButton,
            threeButton,
            fourButton,
            fiveButton,
            sixButton,
            sevenButton,
            eightButton,
            nineButton
        )
        numberButtons.forEach { button ->
            button.setOnClickListener { (viewModel as MainViewModel).sudokuGame.handleInput(button.text.toString().toInt()) }
        }

        notesButton.setOnClickListener { (viewModel as MainViewModel).sudokuGame.changeNoteState() }
        deleteButton.setOnClickListener {
            (viewModel as MainViewModel).sudokuGame.delete()
        }
    }

    override fun onPause() {
        super.onPause()
        (viewModel as MainViewModel).sudokuGame.onPause()
    }

    override fun onResume() {
        super.onResume()
        (viewModel as MainViewModel).sudokuGame.onResume()
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        sudokuBoardView.updateCells(it)
    }

    private fun updateSelectedCell(position: Pair<Int, Int>?) = position?.let {
        sudokuBoardView.updateSelectedCell(it.first, it.second)
    }

    private fun updateNoteTakingUi(isNoteTaking: Boolean?) = isNoteTaking?.let {
        val color = if (it) ContextCompat.getColor(this, R.color.colorPrimary) else Color.LTGRAY
        notesButton.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }

    private fun updateHighlightedKeys(set: Set<Int>?) = set?.let {
        numberButtons.forEachIndexed { index, button ->
            val color =
                if (set.contains(index + 1)) ContextCompat.getColor(this, R.color.colorPrimary) else Color.LTGRAY
            button.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
    }

    private fun updateTimer(time: TimeInfo?) = time?.let {
        title = "${it.minutes}m${it.seconds}s"
    }

    override fun cellSelected(row: Int, col: Int) = (viewModel as MainViewModel).sudokuGame.updateSelectedCell(row, col)


    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDataEmpty() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
