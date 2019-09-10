package com.pdtrung.sudoku.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pdtrung.sudoku.di.ViewModelFactory
import java.util.*


abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }


    private fun createViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(BaseViewModel::class.java)
    }


    abstract fun showLoading()

    abstract fun showError()

    abstract fun showDataEmpty()

    fun isHaveInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo() != null
    }


}