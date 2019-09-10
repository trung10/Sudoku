package com.pdtrung.sudoku.model

import androidx.room.Entity

@Entity
data class ResponseCategory(
    val status: Int,
    val message: String,
    val content: List<String>
)