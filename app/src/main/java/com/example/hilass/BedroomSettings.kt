package com.example.hilass

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_bedroom_settings.*
import kotlinx.android.synthetic.main.fragment_bedroom.*

class BedroomSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var swBedroomAmbient = false
    private var swBedroomAutomatic = false
    private var swBedroomDayLight = false
    private var swBedroomNotification = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedroom_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Bedroom"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swAmbientLightingBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swBedroomAmbient = true
            } else {
                swBedroomAmbient = false
            }
        }

        swAutomaticLightingBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swBedroomAutomatic = true
            } else {
                swBedroomAutomatic = false
            }
        }

        swDaylightBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swBedroomDayLight = true
            } else {
                swBedroomDayLight = false
            }
        }

        swNotificationBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swBedroomNotification = true
            } else {
                swBedroomNotification = false
            }
        }

        btnBedroomSave.setOnClickListener {
            saveValue()
            finish()
        }

        getRealtimeUpdates()

    }

    private fun getRealtimeUpdates(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        userRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if(e != null){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                return@EventListener
            }

            if(snapshot != null && snapshot.exists()){
                val ambient = snapshot.get("ambientLightingBedroom").toString().toBoolean()
                val automatic = snapshot.get("automaticLightingBedroom").toString().toBoolean()
                val dayLight = snapshot.get("dayLightBedroom").toString().toBoolean()
                val notification = snapshot.get("notificationBedroom").toString().toBoolean()

                when(ambient){
                    true ->{
                        swBedroomAmbient = ambient
                        swAmbientLightingBedroom.isChecked = true
                    }
                    false ->{
                        swBedroomAmbient = ambient
                        swAmbientLightingBedroom.isChecked = false
                    }
                }

                when(automatic){
                    true ->{
                        swBedroomAutomatic = automatic
                        swAutomaticLightingBedroom.isChecked = true
                    }
                    false ->{
                        swBedroomAutomatic = automatic
                        swAutomaticLightingBedroom.isChecked = false
                    }
                }

                when(dayLight){
                    true ->{
                        swBedroomDayLight = dayLight
                        swDaylightBedroom.isChecked = true
                    }
                    false ->{
                        swBedroomDayLight = dayLight
                        swDaylightBedroom.isChecked = false
                    }
                }

                when(notification){
                    true ->{
                        swBedroomNotification = notification
                        swNotificationBedroom.isChecked = true
                    }
                    false ->{
                        swBedroomNotification = notification
                        swNotificationBedroom.isChecked = false
                    }
                }

            } else{
                // Do nothing
            }
        })


    }

    private fun saveValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        btnBedroomSave.setBackgroundResource(R.drawable.btn_shape_round_disable)
        btnBedroomSave.setTextColor(Color.GRAY)
        btnBedroomSave.isClickable = false

        when(swBedroomAmbient){
            true -> userRef.update("ambientLightingBedroom", true)
            false -> userRef.update("ambientLightingBedroom", false)
        }

        when(swBedroomAutomatic){
            true -> userRef.update("automaticLightingBedroom", true)
            false -> userRef.update("automaticLightingBedroom", false)
        }

        when(swBedroomDayLight){
            true -> userRef.update("dayLightBedroom", true)
            false -> userRef.update("dayLightBedroom", false)
        }

        when(swBedroomNotification){
            true -> userRef.update("notificationBedroom", true)
            false -> userRef.update("notificationBedroom", false)
        }

    }
}