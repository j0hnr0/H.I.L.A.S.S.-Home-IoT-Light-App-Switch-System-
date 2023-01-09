package com.example.hilass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
        }
        return true
    }
}