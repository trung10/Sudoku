package com.pdtrung.sudoku.base

import androidx.lifecycle.ViewModel
import com.pdtrung.sudoku.activity.MainViewModel
import com.pdtrung.sudoku.di.component.DaggerViewModelInjector
import com.pdtrung.sudoku.di.component.ViewModelInjector
import com.pdtrung.sudoku.di.module.NetworkModule

abstract class BaseViewModel : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is MainViewModel -> injector.inject(this)
        }
    }
}