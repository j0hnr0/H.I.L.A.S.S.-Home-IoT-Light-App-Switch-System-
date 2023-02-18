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
import kotlinx.android.synthetic.main.activity_kitchen_settings.*

class KitchenSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var swAmbient = false
    private var swAutomatic = false
    private var swDayLight = false
    private var swNotification = false

    private var listenerRegistration: ListenerRegistration? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Kitchen"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swAmbientLightingKitchen.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAmbient = true
                swAutomaticLightingKitchen.isChecked = false
                swAutomatic = false
            } else {
                swAmbient = false
            }
        }

        swAutomaticLightingKitchen.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAutomatic = true
                swAmbientLightingKitchen.isChecked = false
                swAmbient = false
            } else {
                swAutomatic = false
            }
        }

        swDayLightKitchen.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swDayLight = true
            } else {
                swDayLight = false
            }
        }

        swNotificationKitchen.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swNotification = true
            } else {
                swNotification = false
            }
        }

        btnKitchenSave.setOnClickListener {
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
                val ambient = snapshot.get("ambientLightingKitchen").toString().toBoolean()
                val automatic = snapshot.get("automaticLightingKitchen").toString().toBoolean()
                val dayLight = snapshot.get("dayLightKitchen").toString().toBoolean()
                val notification = snapshot.get("notificationKitchen").toString().toBoolean()

                when(ambient){
                    true ->{
                        swAmbient = ambient
                        swAmbientLightingKitchen.isChecked = true
                    }
                    false ->{
                        swAmbient = ambient
                        swAmbientLightingKitchen.isChecked = false
                    }
                }

                when(automatic){
                    true ->{
                        swAutomatic = automatic
                        swAutomaticLightingKitchen.isChecked = true
                    }
                    false ->{
                        swAutomatic = automatic
                        swAutomaticLightingKitchen.isChecked = false
                    }
                }

                when(dayLight){
                    true ->{
                        swDayLight = dayLight
                        swDayLightKitchen.isChecked = true
                    }
                    false ->{
                        swDayLight = dayLight
                        swDayLightKitchen.isChecked = false
                    }
                }

                when(notification){
                    true ->{
                        swNotification = notification
                        swNotificationKitchen.isChecked = true
                    }
                    false ->{
                        swNotification = notification
                        swNotificationKitchen.isChecked = false
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

        btnKitchenSave.setBackgroundResource(R.drawable.btn_shape_round_disable)
        btnKitchenSave.setTextColor(Color.GRAY)
        btnKitchenSave.isClickable = false

        when(swAmbient){
            true -> {
                userRef.update("ambientLightingKitchen", true)
                userRef.update("automaticLightingKitchen", false)
            }
            false -> {
                userRef.update("ambientLightingKitchen", false)
            }
        }

        when(swAutomatic){
            true -> {
                userRef.update("automaticLightingKitchen", true)
                userRef.update("ambientLightingKitchen", false)
            }
            false -> {
                userRef.update("automaticLightingKitchen", false)
            }
        }

        when(swDayLight){
            true -> userRef.update("dayLightKitchen", true)
            false -> userRef.update("dayLightKitchen", false)
        }

        when(swNotification){
            true -> userRef.update("notificationKitchen", true)
            false -> userRef.update("notificationKitchen", false)
        }

    }
}