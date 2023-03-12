package com.example.hilass

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btnRegisterRegisterPage.setOnClickListener {
            registerUser()
        }

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun checkLoggedInState() {
        val user = auth.currentUser
        if(user == null){
            // Do Nothing
        }else{
            Intent(this, MenuActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }

    private fun registerUser(){
        val email = etRegisterEmailAddress.text.toString()
        val password = etRegisterPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            btnRegisterRegisterPage.setBackgroundResource(R.drawable.btn_shape_round_disable)
            btnRegisterRegisterPage.setTextColor(Color.GRAY)
            btnRegisterRegisterPage.isClickable = false
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    val user = auth.currentUser
                    if(user!=null){
                        val uid = user.uid
                        val userRef = personCollectionRef.document(uid)
                        val data = UserData(
                            bulbOnOffBedroom = false,
                            manualAutoBedroom = false,
                            swBedroomCustomize = false,
                            swBedroomMovementOnly = false,
                            swBedroomPerson = false,
                            swBedroomMode = false,
                            swBedroomAmbientLighting = false,
                            swBedroomNightLight = false,
                            swBedroomNotification = false,
                            bulbOnOffLivingRoom = false,
                            manualAutoLivingRoom = false,
                            swLivingRoomCustomize = false,
                            swLivingRoomMovementOnly = false,
                            swLivingRoomPerson = false,
                            swLivingRoomMode = false,
                            swLivingRoomAmbientLighting = false,
                            swLivingRoomNightLight = false,
                            swLivingRoomNotification = false,
                            bulbOnOffKitchen = false,
                            manualAutoKitchen = false,
                            swKitchenCustomize = false,
                            swKitchenMovementOnly = false,
                            swKitchenPerson = false,
                            swKitchenMode = false,
                            swKitchenAmbientLighting = false,
                            swKitchenNightLight = false,
                            swKitchenNotification = false,
                            sendNotificationBedroom = false,
                            sendNotificationLivingRoom = false,
                            sendNotificationKitchen = false,
                            fcmToken = ""
                        )
                        userRef.set(data).await()
                    }
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                } catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                        btnRegisterRegisterPage.setBackgroundResource(R.drawable.btn_shape_round)
                        btnRegisterRegisterPage.setTextColor(Color.WHITE)
                        btnRegisterRegisterPage.isClickable = true
                    }
                }
            }
        }else{
            Toast.makeText(this, "Please enter your email address and password", Toast.LENGTH_LONG).show()
        }
    }


}