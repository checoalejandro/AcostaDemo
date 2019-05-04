package com.acostadev.acostademo.main

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.acostadev.acostademo.R
import com.acostadev.acostademo.databinding.ActivityMainBinding
import com.acostadev.acostademo.di.ViewModelFactory
import com.acostadev.acostademo.di.injector
import com.acostadev.acostademo.hideKeyboard
import com.acostadev.acostademo.utils.LocaleUtils
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.mainViewModelFactory()).get(MainViewModel::class.java)
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) viewModel

        binding.content.btnFind.setOnClickListener {
            identifyLanguage()
        }

        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setObservers() {
        viewModel.languageIdentificationLiveData.observe(this, Observer {
            if (it == null) return@Observer

            Snackbar.make(binding.coordinatorMain, getString(R.string.main_language_result, LocaleUtils.getDisplayName(it)), Snackbar.LENGTH_LONG).show()
        })
    }

    private fun identifyLanguage() {
        hideKeyboard()
        val text = binding.content.textInput.text.toString()
        if (text.isEmpty()) {
            binding.content.textInput.error = "Can't be empty"
        } else {
            binding.content.textInput.error = null
            viewModel.identifyLanguage(text)
        }
    }
}
