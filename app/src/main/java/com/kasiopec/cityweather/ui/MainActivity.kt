package com.kasiopec.cityweather.ui

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.model.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

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

    /**
     * Function to show custom dialog box with the ability to enter new city name.
     * **/
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
            //Check if edit text field is empty, on empty show error message
            if (body.text.toString().trim().isNotEmpty() ||
                body.text.toString().trim().isNotBlank()) {
                val cityName = body.text.toString()
                viewModel.loadRepositoryWeatherData(cityName)
                viewModel.isNetworkError.observe(this, Observer<Boolean>{isNetworkError ->
                    if(isNetworkError) notifyOnError()
                })
            } else {
                editTextLayout.error = "Please enter city name"
                return@setOnClickListener
            }
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
        //Resize dialog box with the current layout parameters.
        // Otherwise dialog box is rendered with wrong dimensions
        val window = dialog.window
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    /**
     * Function to display error toast message. Triggered when LiveData observable has changed
     * **/
    private fun notifyOnError(){
        if(!viewModel.isNetworkErrorShown.value!!){
            Toast.makeText(this, viewModel.networkError.value, Toast.LENGTH_SHORT).show()
            viewModel.networkErrorShown()
        }
    }

    /**
     * Function show or hide fab. Triggered from the Fragments
     * **/
    fun hideFab(bool : Boolean){
        if (bool) fab.hide() else fab.show()
    }
}