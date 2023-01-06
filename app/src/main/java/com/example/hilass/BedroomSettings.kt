package com.example.hilass

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_bedroom_settings.*
import kotlinx.android.synthetic.main.fragment_bedroom.*

class BedroomSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var swAmbient = false
    private var swAutomatic = false
    private var swDayLight = false
    private var swNotification = false

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedroom_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Bedroom"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swAmbientLightingBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAmbient = true
            } else {
                swAmbient = false
            }
        }

        swAutomaticLightingBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAutomatic = true
            } else {
                swAutomatic = false
            }
        }

        swDaylightBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swDayLight = true
            } else {
                swDayLight = false
            }
        }

        swNotificationBedroom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swNotification = true
            } else {
                swNotification = false
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

        listenerRegistration = userRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
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
                        swAmbient = ambient
                        swAmbientLightingBedroom.isChecked = true
                    }
                    false ->{
                        swAmbient = ambient
                        swAmbientLightingBedroom.isChecked = false
                    }
                }

                when(automatic){
                    true ->{
                        swAutomatic = automatic
                        swAutomaticLightingBedroom.isChecked = true
                    }
                    false ->{
                        swAutomatic = automatic
                        swAutomaticLightingBedroom.isChecked = false
                    }
                }

                when(dayLight){
                    true ->{
                        swDayLight = dayLight
                        swDaylightBedroom.isChecked = true
                    }
                    false ->{
                        swDayLight = dayLight
                        swDaylightBedroom.isChecked = false
                    }
                }

                when(notification){
                    true ->{
                        swNotification = notification
                        swNotificationBedroom.isChecked = true
                    }
                    false ->{
                        swNotification = notification
                        swNotificationBedroom.isChecked = false
                    }
                }

            } else{
                // Do nothing
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }

    private fun saveValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        btnBedroomSave.setBackgroundResource(R.drawable.btn_shape_round_disable)
        btnBedroomSave.setTextColor(Color.GRAY)
        btnBedroomSave.isClickable = false

        when(swAmbient){
            true -> userRef.update("ambientLightingBedroom", true)
            false -> userRef.update("ambientLightingBedroom", false)
        }

        when(swAutomatic){
            true -> userRef.update("automaticLightingBedroom", true)
            false -> userRef.update("automaticLightingBedroom", false)
        }

        when(swDayLight){
            true -> userRef.update("dayLightBedroom", true)
            false -> userRef.update("dayLightBedroom", false)
        }

        when(swNotification){
            true -> userRef.update("notificationBedroom", true)
            false -> userRef.update("notificationBedroom", false)
        }

    }
}