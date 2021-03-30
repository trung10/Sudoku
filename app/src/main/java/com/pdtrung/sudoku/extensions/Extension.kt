package com.pdtrung.sudoku.extensions

import android.os.Handler

fun postDelay100ms(unit: () -> Unit) {
    Handler().postDelayed({
        unit.invoke()
    }, 100)
}