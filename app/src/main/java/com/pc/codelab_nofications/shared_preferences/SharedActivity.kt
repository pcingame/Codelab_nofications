package com.pc.codelab_nofications.shared_preferences

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pc.codelab_nofications.R
import com.pc.codelab_nofications.databinding.ActivitySharedBinding


class SharedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharedBinding
    private lateinit var sharePreference: SharedPreferences
    private var count = 0
    private var color = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mCountTextView = binding.countTextview
        color = ContextCompat.getColor(this, R.color.default_background)

        sharePreference = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        count = sharePreference.getInt(COUNT_KEY, 0)
        color = sharePreference.getInt(COLOR_KEY, color)

        mCountTextView.text = count.toString()
        mCountTextView.setBackgroundColor(color)

    }

    /**
     * Handles the onClick for the background color buttons. Gets background
     * color of the button that was clicked, and sets the TextView background
     * to that color.
     *
     * @param view The view (Button) that was clicked.
     */
    fun changeBackground(view: View) {
        val colorChange = (view.background as ColorDrawable).color
        binding.countTextview.setBackgroundColor(colorChange)
        color = colorChange
    }

    fun countUp(view: View) {
        count++
        binding.countTextview.text = count.toString()
    }

    fun reset(view: View) {
        count = 0
        binding.countTextview.text = count.toString()

        color = ContextCompat.getColor(this, R.color.default_background)
        binding.countTextview.setBackgroundColor(color)

        val preferencesEditor: SharedPreferences.Editor = sharePreference.edit()
        preferencesEditor.clear()
        preferencesEditor.apply()
    }

    override fun onPause() {
        super.onPause()
        val preferencesEditor: SharedPreferences.Editor = sharePreference.edit()
        preferencesEditor.putInt(COUNT_KEY, count)
        preferencesEditor.putInt(COLOR_KEY, color)
        preferencesEditor.apply()
    }

    companion object {
        // Key for current count
        private const val COUNT_KEY = "count"

        // Key for current color
        private const val COLOR_KEY = "color"
        private const val sharedPrefFile = "preference"
    }
}
