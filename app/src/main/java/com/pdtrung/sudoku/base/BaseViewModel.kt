package com.pdtrung.sudoku.base

import androidx.lifecycle.ViewModel
import com.pdtrung.sudoku.activity.MainViewModel

abstract class BaseViewModel : ViewModel() {

    /*private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    *//**
     * Injects the required dependencies
     *//*
    private fun inject() {
        when (this) {
            is MainViewModel -> injector.inject(this)
        }
    }*/
}