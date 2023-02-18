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
import kotlinx.android.synthetic.main.activity_living_room_settings.*

class LivingRoomSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var swAmbient = false
    private var swAutomatic = false
    private var swDayLight = false
    private var swNotification = false

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_room_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Living Room"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swAmbientLightingLivingRoom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAmbient = true
                swAutomaticLightingLivingRoom.isChecked = false
                swAutomatic = false
            } else {
                swAmbient = false
            }
        }

        swAutomaticLightingLivingRoom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swAutomatic = true
                swAmbientLightingLivingRoom.isChecked = false
                swAmbient = false
            } else {
                swAutomatic = false
            }
        }

        swDayLightLivingRoom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swDayLight = true
            } else {
                swDayLight = false
            }
        }

        swNotificationLivingRoom.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                swNotification = true
            } else {
                swNotification = false
            }
        }

        btnLivingRoomSave.setOnClickListener {
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
                val ambient = snapshot.get("ambientLightingLivingRoom").toString().toBoolean()
                val automatic = snapshot.get("automaticLightingLivingRoom").toString().toBoolean()
                val dayLight = snapshot.get("dayLightLivingRoom").toString().toBoolean()
                val notification = snapshot.get("notificationLivingRoom").toString().toBoolean()

                when(ambient){
                    true ->{
                        swAmbient = ambient
                        swAmbientLightingLivingRoom.isChecked = true
                    }
                    false ->{
                        swAmbient = ambient
                        swAmbientLightingLivingRoom.isChecked = false
                    }
                }

                when(automatic){
                    true ->{
                        swAutomatic = automatic
                        swAutomaticLightingLivingRoom.isChecked = true
                    }
                    false ->{
                        swAutomatic = automatic
                        swAutomaticLightingLivingRoom.isChecked = false
                    }
                }

                when(dayLight){
                    true ->{
                        swDayLight = dayLight
                        swDayLightLivingRoom.isChecked = true
                    }
                    false ->{
                        swDayLight = dayLight
                        swDayLightLivingRoom.isChecked = false
                    }
                }

                when(notification){
                    true ->{
                        swNotification = notification
                        swNotificationLivingRoom.isChecked = true
                    }
                    false ->{
                        swNotification = notification
                        swNotificationLivingRoom.isChecked = false
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

        btnLivingRoomSave.setBackgroundResource(R.drawable.btn_shape_round_disable)
        btnLivingRoomSave.setTextColor(Color.GRAY)
        btnLivingRoomSave.isClickable = false

        when(swAmbient){
            true -> {
                userRef.update("ambientLightingLivingRoom", true)
                userRef.update("automaticLightingLivingRoom", false)
            }
            false -> {
                userRef.update("ambientLightingLivingRoom", false)
            }
        }

        when(swAutomatic){
            true -> {
                userRef.update("automaticLightingLivingRoom", true)
                userRef.update("ambientLightingLivingRoom", false)
            }
            false -> {
                userRef.update("automaticLightingLivingRoom", false)
            }
        }

        when(swDayLight){
            true -> userRef.update("dayLightLivingRoom", true)
            false -> userRef.update("dayLightLivingRoom", false)
        }

        when(swNotification){
            true -> userRef.update("notificationLivingRoom", true)
            false -> userRef.update("notificationLivingRoom", false)
        }

    }
}