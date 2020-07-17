package com.kasiopec.cityweather.ui

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.model.CustomViewModelFactory
import com.kasiopec.cityweather.model.MainActivityViewModel
import com.kasiopec.cityweather.presenter.MainActivityPresenter

class MainActivity : AppCompatActivity(),
    Contract.MainActivityView {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
           showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_alert_dialog)
        val editTextLayout  = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
        val body = dialog.findViewById(R.id.newCityEditText) as EditText
        val yesBtn = dialog.findViewById(R.id.btnYes) as Button
        val noBtn = dialog.findViewById(R.id.btnCloseDialog) as Button
        yesBtn.setOnClickListener {
            if (body.text.toString().trim().isNotEmpty() ||
                body.text.toString().trim().isNotBlank()) {
                val cityName = body.text.toString()
                viewModel.loadRepositoryWeatherData(cityName)
            } else {
                editTextLayout.error = "Please enter city name"
                return@setOnClickListener
            }
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
        val window = dialog.window
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }
}