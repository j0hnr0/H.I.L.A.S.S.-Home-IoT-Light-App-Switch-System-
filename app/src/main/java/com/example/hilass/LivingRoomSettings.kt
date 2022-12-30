package com.example.hilass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LivingRoomSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_room_settings)

        supportActionBar!!.title = "Living Room"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}