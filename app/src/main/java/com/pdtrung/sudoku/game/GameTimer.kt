package com.patrickfeltes.sudokuandroid.common

import android.os.SystemClock
import com.pdtrung.sudoku.model.TimeInfo
import java.util.Timer
import java.util.TimerTask

class GameTimer {
    private var totalAccumulatedTime = 0L
    private var currentStartTime = 0L

    private var listener: GameTimerListener? = null
    private var currentTimer: Timer? = null

    init {
        currentStartTime = SystemClock.uptimeMillis()
        currentTimer = createTimer()
    }

    fun onResume() {
        currentStartTime = SystemClock.uptimeMillis()
        currentTimer = createTimer()
    }

    fun onPause() {
        totalAccumulatedTime += (SystemClock.uptimeMillis() - currentStartTime)
        currentTimer?.cancel()
    }

    fun registerListener(listener: GameTimerListener) {
        this.listener = listener
    }

    private fun createTimer(): Timer {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                val time = totalAccumulatedTime + (SystemClock.uptimeMillis() - currentStartTime)

                val seconds = (time / 1000) % 60
                val minutes = ((time / 1000) - seconds) / 60

                listener?.publishTime(TimeInfo(minutes, seconds))
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 500)
        return timer
    }

    interface GameTimerListener {
        fun publishTime(time: TimeInfo)
    }
}
