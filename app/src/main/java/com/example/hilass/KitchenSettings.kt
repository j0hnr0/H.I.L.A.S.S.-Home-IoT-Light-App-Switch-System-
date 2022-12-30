package com.example.hilass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class KitchenSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen_settings)

        supportActionBar!!.title = "Kitchen"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}