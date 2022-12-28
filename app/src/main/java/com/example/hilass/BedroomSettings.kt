package com.example.hilass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BedroomSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedroom_settings)

        supportActionBar!!.title = "Bedroom"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}