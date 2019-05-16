package com.acostadev.acostademo.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.acostadev.acostademo.R
import com.acostadev.acostademo.adapters.FirebaseDatabaseAdapter
import com.acostadev.acostademo.databinding.ActivityMainBinding
import com.acostadev.acostademo.di.injector
import com.acostadev.acostademo.models.TextProfile
import com.acostadev.acostademo.ui.settings.SettingsActivity
import com.acostadev.acostademo.utils.LocaleUtils

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.mainViewModelFactory()).get(MainViewModel::class.java)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) viewModel

        setObservers()
        setLanguageDetector()
        viewModel.getProfiles()
    }

    private fun setLanguageDetector() {
        binding.content.textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                identifyLanguage()
            }
        })
    }

    private fun setRealtimeDatabase(profiles: List<TextProfile>) {
        binding.content.rvDatabase.layoutManager = LinearLayoutManager(this)
        binding.content.rvDatabase.adapter = FirebaseDatabaseAdapter(profiles)
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
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setObservers() {
        viewModel.languageIdentificationLiveData.observe(this, Observer {
            if (it == null) return@Observer
            binding.content.txtResult.text = LocaleUtils.getDisplayName(it)
        })
        viewModel.profilesLiveData.observe(this, Observer { profiles ->
            if (profiles == null) return@Observer
            setRealtimeDatabase(profiles)
        })
    }

    private fun identifyLanguage() {
        val text = binding.content.textInput.text.toString()
        if (text.isEmpty()) {
            binding.content.txtResult.text = ""
            return
        } else {
            binding.content.textInput.error = null
            viewModel.identifyLanguage(text)
        }
    }
}
