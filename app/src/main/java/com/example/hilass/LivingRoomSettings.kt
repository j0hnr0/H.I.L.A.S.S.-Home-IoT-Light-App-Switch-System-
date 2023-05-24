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
import java.util.*

class LivingRoomSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var sw_livingroom_customize = false
    private var sw_livingroom_movement_only = false
    private var sw_livingroom_person = false
    private var sw_livingroom_mode = false
    private var sw_livingroom_ambient_lighting = false
    private var sw_livingroom_night_light = false
    private var sw_livingroom_notification = false

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_room_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Living Room"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)


        swLivingRoomMovementOnly.isEnabled = false
        swLivingRoomAmbientLighting.isEnabled = false
        swLivingRoomNightLight.isEnabled = false
        swLivingRoomNotification.isEnabled = false

        swLivingRoomCustomize.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sw_livingroom_customize = true
                swLivingRoomMode.isChecked = false
                sw_livingroom_mode = false
                swLivingRoomAmbientLighting.isEnabled = false
                swLivingRoomNightLight.isEnabled = false

                swLivingRoomAmbientLighting.isChecked = false
                swLivingRoomNightLight.isChecked = false
                sw_livingroom_ambient_lighting = false
                sw_livingroom_night_light = false

                swLivingRoomMovementOnly.isEnabled = true

                swLivingRoomMovementOnly.isChecked = true
                sw_livingroom_movement_only = true

                swLivingRoomNotification.isEnabled = false
                swLivingRoomNotification.isChecked = false
                sw_livingroom_notification = false

            } else {
                sw_livingroom_customize = false
                swLivingRoomMode.isChecked = true
            }
        }

        swLivingRoomMovementOnly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sw_livingroom_movement_only = true
                sw_livingroom_person = false
            } else {

            }
        }

        swLivingRoomMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                val timeNow = getCurrentHour()

                sw_livingroom_mode = true
                swLivingRoomCustomize.isChecked = false
                sw_livingroom_customize = false
                swLivingRoomMovementOnly.isEnabled = false

                swLivingRoomMovementOnly.isChecked = false
                sw_livingroom_movement_only = false
                sw_livingroom_person = false

                swLivingRoomAmbientLighting.isChecked = true
                sw_livingroom_ambient_lighting = true

                swLivingRoomAmbientLighting.isEnabled = false
                swLivingRoomNightLight.isEnabled = false

                if (timeNow >= 6 && timeNow <= 17) {
                    swLivingRoomAmbientLighting.isChecked = true
                    sw_livingroom_ambient_lighting = true

                    swLivingRoomNightLight.isChecked = false
                    sw_livingroom_night_light = false
                } else {

                    swLivingRoomNightLight.isChecked = true
                    sw_livingroom_night_light = true

                    swLivingRoomAmbientLighting.isChecked = false
                    sw_livingroom_ambient_lighting = false
                }

                swLivingRoomNotification.isEnabled = true
            } else {
                sw_livingroom_mode = false
                swLivingRoomCustomize.isChecked = true
            }
        }



        swLivingRoomNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sw_livingroom_notification = true
            } else {
                sw_livingroom_notification = false
            }
        }

        btnLivingRoomSave.setOnClickListener {
            saveValue()
            finish()
        }

        getRealtimeUpdates()
    }

    private fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }


    private fun getRealtimeUpdates() {
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        listenerRegistration =
            userRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    return@EventListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val livingRoomCustomize = snapshot.get("swLivingRoomCustomize").toString().toBoolean()
                    val livingRoomMovementOnly = snapshot.get("swLivingRoomMovementOnly").toString().toBoolean()
                    val livingRoomPerson = snapshot.get("swLivingRoomPerson").toString().toBoolean()
                    val livingRoomMode = snapshot.get("swLivingRoomMode").toString().toBoolean()
                    val livingRoomAmbientLighting = snapshot.get("swLivingRoomAmbientLighting").toString().toBoolean()
                    val livingRoomNightLight = snapshot.get("swLivingRoomNightLight").toString().toBoolean()
                    val livingRoomNotification = snapshot.get("swLivingRoomNotification").toString().toBoolean()

                    when(livingRoomCustomize){
                        true ->{
                            sw_livingroom_customize = livingRoomCustomize
                            swLivingRoomCustomize.isChecked = true
                        }
                        false ->{
                            sw_livingroom_customize = livingRoomCustomize
                            swLivingRoomCustomize.isChecked = false
                        }
                    }

                    when(livingRoomMovementOnly){
                        true ->{
                            sw_livingroom_movement_only = livingRoomMovementOnly
                            swLivingRoomMovementOnly.isChecked = true
                        }
                        false ->{
                            sw_livingroom_movement_only = livingRoomMovementOnly
                            swLivingRoomMovementOnly.isChecked = false
                        }
                    }

                    when(livingRoomPerson){
                        true ->{
                            sw_livingroom_person = livingRoomPerson
                        }
                        false ->{
                            sw_livingroom_person = livingRoomPerson
                        }
                    }

                    when(livingRoomMode){
                        true ->{
                            sw_livingroom_mode = livingRoomMode
                            swLivingRoomMode.isChecked = true
                        }
                        false ->{
                            sw_livingroom_mode = livingRoomMode
                            swLivingRoomMode.isChecked = false
                        }
                    }

                    when(livingRoomAmbientLighting){
                        true ->{
                            sw_livingroom_ambient_lighting = livingRoomAmbientLighting
                            swLivingRoomAmbientLighting.isChecked = true
                        }
                        false ->{
                            sw_livingroom_ambient_lighting = livingRoomAmbientLighting
                            swLivingRoomAmbientLighting.isChecked = false
                        }
                    }


                    when(livingRoomNightLight){
                        true ->{
                            sw_livingroom_night_light = livingRoomNightLight
                            swLivingRoomNightLight.isChecked = true
                        }
                        false ->{
                            sw_livingroom_night_light = livingRoomNightLight
                            swLivingRoomNightLight.isChecked = false
                        }
                    }

                    when(livingRoomNotification){
                        true ->{
                            sw_livingroom_notification = livingRoomNotification
                            swLivingRoomNotification.isChecked = true
                        }
                        false ->{
                            sw_livingroom_notification = livingRoomNotification
                            swLivingRoomNotification.isChecked = false
                        }
                    }


                } else {
                    // Do nothing
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }

    private fun saveValue() {
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        btnLivingRoomSave.setBackgroundResource(R.drawable.btn_shape_round_disable)
        btnLivingRoomSave.setTextColor(Color.GRAY)
        btnLivingRoomSave.isClickable = false

        when(sw_livingroom_customize){
            true -> {
                userRef.update("swLivingRoomCustomize", true)
            }
            false -> {
                userRef.update("swLivingRoomCustomize", false)
            }
        }

        when(sw_livingroom_movement_only){
            true -> {
                userRef.update("swLivingRoomMovementOnly", true)
            }
            false -> {
                userRef.update("swLivingRoomMovementOnly", false)
            }
        }

        when(sw_livingroom_person){
            true -> userRef.update("swLivingRoomPerson", true)
            false -> userRef.update("swLivingRoomPerson", false)
        }

        when(sw_livingroom_mode){
            true -> userRef.update("swLivingRoomMode", true)
            false -> userRef.update("swLivingRoomMode", false)
        }

        when(sw_livingroom_ambient_lighting){
            true -> userRef.update("swLivingRoomAmbientLighting", true)
            false -> userRef.update("swLivingRoomAmbientLighting", false)
        }

        when(sw_livingroom_night_light){
            true -> userRef.update("swLivingRoomNightLight", true)
            false -> userRef.update("swLivingRoomNightLight", false)
        }

        when(sw_livingroom_notification){
            true -> userRef.update("swLivingRoomNotification", true)
            false -> userRef.update("swLivingRoomNotification", false)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please Select and Save your preferences", Toast.LENGTH_LONG).show()
    }
}