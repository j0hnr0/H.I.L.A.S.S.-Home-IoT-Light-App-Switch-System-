package com.example.hilass

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
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

        tvForgotPassword.setOnClickListener {
            showResetPasswordDialog()
        }

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

    }

    private fun showResetPasswordDialog() {
        // Create an EditText view for user input
        val input = EditText(this)
        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        input.hint = "Enter your email"

        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password")
        builder.setMessage("Enter your email to reset your password")
        builder.setIcon(R.drawable.hilassicon)
        builder.setView(input)

        // Set the positive button action
        builder.setPositiveButton("Reset") { dialog, which ->
            val email = input.text.toString().trim()
            if (!TextUtils.isEmpty(email)) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        // Show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun resetPassword(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send password reset email: " + task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { e: java.lang.Exception ->
                Toast.makeText(
                    this,
                    "Error sending password reset email: " + e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }


    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun checkLoggedInState() {
        val user = auth.currentUser
        if (user == null) {
            // Do Nothing
        } else if (!user.isEmailVerified) {
            Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show()
        } else {
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
            btnLoginLoginPage.setBackgroundResource(R.drawable.btn_shape_round_disable)
            btnLoginLoginPage.setTextColor(Color.GRAY)
            btnLoginLoginPage.isClickable = false
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    val user = auth.currentUser
                    if(user != null && user.isEmailVerified) {
                        withContext(Dispatchers.Main){
                            checkLoggedInState()
                        }
                    } else {
                        throw Exception("Please verify your email address to login")
                    }
                } catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                        btnLoginLoginPage.setBackgroundResource(R.drawable.btn_shape_round)
                        btnLoginLoginPage.setTextColor(Color.WHITE)
                        btnLoginLoginPage.isClickable = true
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please enter your email address and password", Toast.LENGTH_LONG).show()
        }

    }



}