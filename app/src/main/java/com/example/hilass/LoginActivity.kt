package com.example.hilass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLoginLoginPage.setOnClickListener {
            loginUser()
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

    private fun loginUser(){
        val email = etLoginEmailAddress.text.toString()
        val password = etLoginPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                } catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Please enter your email address and password", Toast.LENGTH_LONG).show()
        }
    }



}