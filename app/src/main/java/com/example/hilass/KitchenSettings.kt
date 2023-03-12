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

    private var sw_kitchen_customize = false
    private var sw_kitchen_movement_only = false
    private var sw_kitchen_person = false
    private var sw_kitchen_mode = false
    private var sw_kitchen_ambient_lighting = false
    private var sw_kitchen_night_light = false
    private var sw_kitchen_notification = false

    private var listenerRegistration: ListenerRegistration? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen_settings)

        auth = FirebaseAuth.getInstance()

        supportActionBar!!.title = "Kitchen"

        swKitchenMovementOnly.isEnabled = false
        swKitchenPerson.isEnabled = false
        swKitchenAmbientLighting.isEnabled = false
        swKitchenNightLight.isEnabled = false
        swKitchenNotification.isEnabled = false

        swKitchenCustomize.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                sw_kitchen_customize = true
                swKitchenMode.isChecked = false
                sw_kitchen_mode = false
                swKitchenAmbientLighting.isEnabled = false
                swKitchenNightLight.isEnabled = false

                swKitchenAmbientLighting.isChecked = false
                swKitchenNightLight.isChecked = false
                sw_kitchen_ambient_lighting = false
                sw_kitchen_night_light = false

                swKitchenMovementOnly.isChecked = true
                sw_kitchen_movement_only = true

                swKitchenMovementOnly.isEnabled = true
                swKitchenPerson.isEnabled = true

                swKitchenNotification.isEnabled = false
                swKitchenNotification.isChecked = false
                sw_kitchen_notification = false
            }else {
                sw_kitchen_customize = false
                swKitchenMode.isChecked = true
            }
        }

        swKitchenMovementOnly.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_kitchen_movement_only = true
                swKitchenPerson.isChecked = false
                sw_kitchen_person = false
            } else {

            }
        }

        swKitchenPerson.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                sw_kitchen_person = true
                swKitchenMovementOnly.isChecked = false
                sw_kitchen_movement_only = false
            } else {

            }
        }

        swKitchenMode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                sw_kitchen_mode = true
                swKitchenCustomize.isChecked = false
                sw_kitchen_customize = false
                swKitchenMovementOnly.isEnabled = false
                swKitchenPerson.isEnabled = false

                swKitchenMovementOnly.isChecked = false
                swKitchenPerson.isChecked = false
                sw_kitchen_movement_only = false
                sw_kitchen_person = false

                swKitchenAmbientLighting.isChecked = true
                sw_kitchen_ambient_lighting = true

                swKitchenAmbientLighting.isEnabled = true
                swKitchenNightLight.isEnabled = true

                swKitchenNotification.isEnabled = true
            } else {
                sw_kitchen_mode = false
                swKitchenCustomize.isChecked = true
            }
        }

        swKitchenAmbientLighting.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_kitchen_ambient_lighting = true
                swKitchenNightLight.isChecked = false
                sw_kitchen_night_light = false
            } else {

            }
        }

        swKitchenNightLight.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_kitchen_night_light = true
                swKitchenAmbientLighting.isChecked = false
                sw_kitchen_ambient_lighting = false
            } else {

            }
        }

        swKitchenNotification.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                sw_kitchen_notification = true
            } else {
                sw_kitchen_notification = false
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
                val kitchenCustomize = snapshot.get("swKitchenCustomize").toString().toBoolean()
                val kitchenMovementOnly = snapshot.get("swKitchenMovementOnly").toString().toBoolean()
                val kitchenPerson = snapshot.get("swKitchenPerson").toString().toBoolean()
                val kitchenMode = snapshot.get("swKitchenMode").toString().toBoolean()
                val kitchenAmbientLighting = snapshot.get("swKitchenAmbientLighting").toString().toBoolean()
                val kitchenNightLight = snapshot.get("swKitchenNightLight").toString().toBoolean()
                val kitchenNotification = snapshot.get("swKitchenNotification").toString().toBoolean()

                when(kitchenCustomize){
                    true ->{
                        sw_kitchen_customize = kitchenCustomize
                        swKitchenCustomize.isChecked = true
                    }
                    false ->{
                        sw_kitchen_customize = kitchenCustomize
                        swKitchenCustomize.isChecked = false
                    }
                }

                when(kitchenMovementOnly){
                    true ->{
                        sw_kitchen_movement_only = kitchenMovementOnly
                        swKitchenMovementOnly.isChecked = true
                    }
                    false ->{
                        sw_kitchen_movement_only = kitchenMovementOnly
                        swKitchenMovementOnly.isChecked = false
                    }
                }

                when(kitchenPerson){
                    true ->{
                        sw_kitchen_person = kitchenPerson
                        swKitchenPerson.isChecked = true
                    }
                    false ->{
                        sw_kitchen_person = kitchenPerson
                        swKitchenPerson.isChecked = false
                    }
                }

                when(kitchenMode){
                    true ->{
                        sw_kitchen_mode = kitchenMode
                        swKitchenMode.isChecked = true
                    }
                    false ->{
                        sw_kitchen_mode = kitchenMode
                        swKitchenMode.isChecked = false
                    }
                }

                when(kitchenAmbientLighting){
                    true ->{
                        sw_kitchen_ambient_lighting = kitchenAmbientLighting
                        swKitchenAmbientLighting.isChecked = true
                    }
                    false ->{
                        sw_kitchen_ambient_lighting = kitchenAmbientLighting
                        swKitchenAmbientLighting.isChecked = false
                    }
                }


                when(kitchenNightLight){
                    true ->{
                        sw_kitchen_night_light = kitchenNightLight
                        swKitchenNightLight.isChecked = true
                    }
                    false ->{
                        sw_kitchen_night_light = kitchenNightLight
                        swKitchenNightLight.isChecked = false
                    }
                }

                when(kitchenNotification){
                    true ->{
                        sw_kitchen_notification = kitchenNotification
                        swKitchenNotification.isChecked = true
                    }
                    false ->{
                        sw_kitchen_notification = kitchenNotification
                        swKitchenNotification.isChecked = false
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

        when(sw_kitchen_customize){
            true -> {
                userRef.update("swKitchenCustomize", true)
            }
            false -> {
                userRef.update("swKitchenCustomize", false)
            }
        }

        when(sw_kitchen_movement_only){
            true -> {
                userRef.update("swKitchenMovementOnly", true)
            }
            false -> {
                userRef.update("swKitchenMovementOnly", false)
            }
        }

        when(sw_kitchen_person){
            true -> userRef.update("swKitchenPerson", true)
            false -> userRef.update("swKitchenPerson", false)
        }

        when(sw_kitchen_mode){
            true -> userRef.update("swKitchenMode", true)
            false -> userRef.update("swKitchenMode", false)
        }

        when(sw_kitchen_ambient_lighting){
            true -> userRef.update("swKitchenAmbientLighting", true)
            false -> userRef.update("swKitchenAmbientLighting", false)
        }

        when(sw_kitchen_night_light){
            true -> userRef.update("swKitchenNightLight", true)
            false -> userRef.update("swKitchenNightLight", false)
        }

        when(sw_kitchen_notification){
            true -> userRef.update("swKitchenNotification", true)
            false -> userRef.update("swKitchenNotification", false)
        }

    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please Select and Save your preferences", Toast.LENGTH_LONG).show()
    }
}