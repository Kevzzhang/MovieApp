package com.kevin.movieapp.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kevin.movieapp.R
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.reusable_error_layout.*

open class BaseActivity : AppCompatActivity() {

    protected fun showErrorSnackbar(activity: Activity, view: View, message: String) {
        val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val v = mSnackbar.view
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.bsp_red))
        mSnackbar.show()
    }

    protected fun showSuccessSnackbar(activity: Activity, view: View, message: String) {
        val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val v = mSnackbar.view
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        mSnackbar.show()
    }
}