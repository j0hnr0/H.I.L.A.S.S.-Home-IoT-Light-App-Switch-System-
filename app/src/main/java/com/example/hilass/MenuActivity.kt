package com.example.hilass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_bedroom.*

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Permission granted
                    Log.d("FCM", "Successfully subscribed to topic")
                } else {
                    // Permission denied
                    Log.e("FCM", "Failed to subscribe to topic")
                }
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