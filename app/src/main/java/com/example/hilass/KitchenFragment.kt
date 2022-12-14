package com.example.hilass

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_bedroom.*
import kotlinx.android.synthetic.main.fragment_kitchen.*
import kotlinx.android.synthetic.main.fragment_living_room.*

class KitchenFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var bulbValue = false
    private var btnManualAuto = false

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        btnManualKitchen.setOnClickListener {
            btnManualAuto = false
            setManualAutoValue()
        }

        btnAutomaticKitchen.setOnClickListener {
            btnManualAuto = true
            setManualAutoValue()
        }

        ivKitchenBulb.setOnClickListener {
            setBulbValue()
        }

        ivKitchenSettings.setOnClickListener {
            Intent(context, KitchenSettings::class.java).also {
                startActivity(it)
            }
        }
        getRealtimeUpdates()
    }

    private fun getRealtimeUpdates(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        listenerRegistration = userRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if(e != null){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                return@EventListener
            }

            if(snapshot != null && snapshot.exists()){

                val bulb = snapshot.get("bulbOnOffKitchen").toString().toBoolean()
                val manualAuto = snapshot.get("manualAutoKitchen").toString().toBoolean()

                when(bulb){
                    true -> {
                        bulbValue = bulb
                        ivKitchenBulb.setImageResource(R.drawable.bulb_on)
                    }
                    false -> {
                        bulbValue = bulb
                        ivKitchenBulb.setImageResource(R.drawable.bulb_off)
                    }
                }
                when(manualAuto){
                    true -> {
                        btnManualAuto = manualAuto
                        btnAutomaticKitchen.setBackgroundResource(R.drawable.btn_shape_round_disable)
                        btnAutomaticKitchen.setTextColor(Color.GRAY)
                        btnAutomaticKitchen.isClickable = false

                        btnManualKitchen.setBackgroundResource(R.drawable.btn_shape_round)
                        btnManualKitchen.setTextColor(Color.WHITE)
                        btnManualKitchen.isClickable = true

                        ivKitchenBulb.isClickable = false
                        tvInstructionKitchen.text = ""
                    }
                    false -> {
                        btnManualAuto = manualAuto
                        btnManualKitchen.setBackgroundResource(R.drawable.btn_shape_round_disable)
                        btnManualKitchen.setTextColor(Color.GRAY)
                        btnManualKitchen.isClickable = false

                        btnAutomaticKitchen.setBackgroundResource(R.drawable.btn_shape_round)
                        btnAutomaticKitchen.setTextColor(Color.WHITE)
                        btnAutomaticKitchen.isClickable = true


                        ivKitchenBulb.isClickable = true
                        tvInstructionKitchen.text = "Tap to manually turn on/off"

                    }
                }

            } else{
                // Do nothing
            }
        })
    }

    private fun setBulbValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        when(bulbValue){
            false -> userRef.update("bulbOnOffKitchen", true)
            true -> userRef.update("bulbOnOffKitchen", false)

        }
    }

    private fun setManualAutoValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        when(btnManualAuto){
            false ->  userRef.update("manualAutoKitchen", false)
            true -> userRef.update("manualAutoKitchen", true)

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}