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

    private var sw_bedroom_customize = false
    private var sw_bedroom_movement_only = false
    private var sw_bedroom_person = false
    private var sw_bedroom_mode = false
    private var sw_bedroom_ambient_lighting = false
    private var sw_bedroom_night_light = false
    private var sw_bedroom_notification = false

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedroom_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Bedroom"

        swBedroomMovementOnly.isEnabled = false
        swBedroomPerson.isEnabled = false
        swBedroomAmbientLighting.isEnabled = false
        swBedroomNightLight.isEnabled = false
        swBedroomNotification.isEnabled = false

        swBedroomCustomize.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                sw_bedroom_customize = true
                swBedroomMode.isChecked = false
                sw_bedroom_mode = false
                swBedroomAmbientLighting.isEnabled = false
                swBedroomNightLight.isEnabled = false

                swBedroomAmbientLighting.isChecked = false
                swBedroomNightLight.isChecked = false
                sw_bedroom_ambient_lighting = false
                sw_bedroom_night_light = false

                swBedroomMovementOnly.isEnabled = true
                swBedroomPerson.isEnabled = true

                swBedroomMovementOnly.isChecked = true
                sw_bedroom_movement_only = true

                swBedroomNotification.isEnabled = false
                swBedroomNotification.isChecked = false
                sw_bedroom_notification = false

            }else {
                sw_bedroom_customize = false
                swBedroomMode.isChecked = true
            }
        }

        swBedroomMovementOnly.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_bedroom_movement_only = true
                swBedroomPerson.isChecked = false
                sw_bedroom_person = false
            } else {

            }
        }

        swBedroomPerson.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                sw_bedroom_person = true
                swBedroomMovementOnly.isChecked = false
                sw_bedroom_movement_only = false
            } else {

            }
        }

        swBedroomMode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                sw_bedroom_mode = true
                swBedroomCustomize.isChecked = false
                sw_bedroom_customize = false
                swBedroomMovementOnly.isEnabled = false
                swBedroomPerson.isEnabled = false

                swBedroomMovementOnly.isChecked = false
                swBedroomPerson.isChecked = false
                sw_bedroom_movement_only = false
                sw_bedroom_person = false

                swBedroomAmbientLighting.isChecked = true
                sw_bedroom_ambient_lighting = true

                swBedroomAmbientLighting.isEnabled = true
                swBedroomNightLight.isEnabled = true

                swBedroomNotification.isEnabled = true
            }else {
                sw_bedroom_mode = false
                swBedroomCustomize.isChecked = true
            }
        }

        swBedroomAmbientLighting.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_bedroom_ambient_lighting = true
                swBedroomNightLight.isChecked = false
                sw_bedroom_night_light = false
            } else {

            }
        }

        swBedroomNightLight.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_bedroom_night_light = true
                swBedroomAmbientLighting.isChecked = false
                sw_bedroom_ambient_lighting = false
            } else {

            }
        }

        swBedroomNotification.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_bedroom_notification = true
            } else {
                sw_bedroom_notification = false
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
                val bedroomCustomize = snapshot.get("swBedroomCustomize").toString().toBoolean()
                val bedroomMovementOnly = snapshot.get("swBedroomMovementOnly").toString().toBoolean()
                val bedroomPerson = snapshot.get("swBedroomPerson").toString().toBoolean()
                val bedroomMode = snapshot.get("swBedroomMode").toString().toBoolean()
                val bedroomAmbientLighting = snapshot.get("swBedroomAmbientLighting").toString().toBoolean()
                val bedroomNightLight = snapshot.get("swBedroomNightLight").toString().toBoolean()
                val bedroomNotification = snapshot.get("swBedroomNotification").toString().toBoolean()

                when(bedroomCustomize){
                    true ->{
                        sw_bedroom_customize = bedroomCustomize
                        swBedroomCustomize.isChecked = true
                    }
                    false ->{
                        sw_bedroom_customize = bedroomCustomize
                        swBedroomCustomize.isChecked = false
                    }
                }

                when(bedroomMovementOnly){
                    true ->{
                        sw_bedroom_movement_only = bedroomMovementOnly
                        swBedroomMovementOnly.isChecked = true
                    }
                    false ->{
                        sw_bedroom_movement_only = bedroomMovementOnly
                        swBedroomMovementOnly.isChecked = false
                    }
                }

                when(bedroomPerson){
                    true ->{
                        sw_bedroom_person = bedroomPerson
                        swBedroomPerson.isChecked = true
                    }
                    false ->{
                        sw_bedroom_person = bedroomPerson
                        swBedroomPerson.isChecked = false
                    }
                }

                when(bedroomMode){
                    true ->{
                        sw_bedroom_mode = bedroomMode
                        swBedroomMode.isChecked = true
                    }
                    false ->{
                        sw_bedroom_mode = bedroomMode
                        swBedroomMode.isChecked = false
                    }
                }

                when(bedroomAmbientLighting){
                    true ->{
                        sw_bedroom_ambient_lighting = bedroomAmbientLighting
                        swBedroomAmbientLighting.isChecked = true
                    }
                    false ->{
                        sw_bedroom_ambient_lighting = bedroomAmbientLighting
                        swBedroomAmbientLighting.isChecked = false
                    }
                }


                when(bedroomNightLight){
                    true ->{
                        sw_bedroom_night_light = bedroomNightLight
                        swBedroomNightLight.isChecked = true
                    }
                    false ->{
                        sw_bedroom_night_light = bedroomNightLight
                        swBedroomNightLight.isChecked = false
                    }
                }

                when(bedroomNotification){
                    true ->{
                        sw_bedroom_notification = bedroomNotification
                        swBedroomNotification.isChecked = true
                    }
                    false ->{
                        sw_bedroom_notification = bedroomNotification
                        swBedroomNotification.isChecked = false
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

        when(sw_bedroom_customize){
            true -> {
                userRef.update("swBedroomCustomize", true)
            }
            false -> {
                userRef.update("swBedroomCustomize", false)
            }
        }

        when(sw_bedroom_movement_only){
            true -> {
                userRef.update("swBedroomMovementOnly", true)
            }
            false -> {
                userRef.update("swBedroomMovementOnly", false)
            }
        }

        when(sw_bedroom_person){
            true -> userRef.update("swBedroomPerson", true)
            false -> userRef.update("swBedroomPerson", false)
        }

        when(sw_bedroom_mode){
            true -> userRef.update("swBedroomMode", true)
            false -> userRef.update("swBedroomMode", false)
        }

        when(sw_bedroom_ambient_lighting){
            true -> userRef.update("swBedroomAmbientLighting", true)
            false -> userRef.update("swBedroomAmbientLighting", false)
        }

        when(sw_bedroom_night_light){
            true -> userRef.update("swBedroomNightLight", true)
            false -> userRef.update("swBedroomNightLight", false)
        }

        when(sw_bedroom_notification){
            true -> userRef.update("swBedroomNotification", true)
            false -> userRef.update("swBedroomNotification", false)
        }

    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please Select and Save your preferences", Toast.LENGTH_LONG).show()
    }
}