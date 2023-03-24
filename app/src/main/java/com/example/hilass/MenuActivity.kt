package com.example.hilass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_bedroom.*

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var personCollectionRef = Firebase.firestore.collection("persons")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        FirebaseMessaging.getInstance().getToken()
            .addOnSuccessListener { token ->
                // Save the FCM token in the document
                val user = auth.currentUser
                val uid = user!!.uid
                val userRef = personCollectionRef.document(uid)
                userRef.update("fcmToken", token)
                Log.d("FCM", "Successfully retrieved the token")
            }


        val bedroomFragment = BedroomFragment()
        val kitchenFragment = KitchenFragment()
        val livingRoomFragment = LivingRoomFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, bedroomFragment)
            commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bnmBedroom -> setCurrentFragment(bedroomFragment)
                R.id.bnmLivingroom -> setCurrentFragment(livingRoomFragment)
                R.id.bnmKitchen -> setCurrentFragment(kitchenFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menupage_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuPageLogout -> {
                auth.signOut()
                finish()
            }

            R.id.menuPageChangeEmail -> {
                showChangeEmailDialog()
            }
        }
        return true
    }

    private fun showChangeEmailDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Email Address")
        builder.setMessage("Enter new email to update your current email")
        builder.setIcon(R.drawable.hilassicon)

        // Set up the input
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            val newEmail = input.text.toString().trim()
            if (newEmail.isNotEmpty()) {
                val user = auth.currentUser
                user?.updateEmail(newEmail)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Send verification email to new email
                            user.sendEmailVerification()
                                .addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "A verification email has been sent to your new email address. Please verify your email address to update it.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Failed to send verification email to new email address. ${verificationTask.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to update email address. ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Please enter a new email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }



}