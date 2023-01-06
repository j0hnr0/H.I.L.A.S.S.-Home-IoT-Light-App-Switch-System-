package com.example.hilass

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_bedroom.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BedroomFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_bedroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        btnManualBedroom.setOnClickListener {
            btnManualAuto = false
            setManualAutoValue()
        }

        btnAutomaticBedroom.setOnClickListener {
            btnManualAuto = true
            setManualAutoValue()
        }


        ivBedroomBulb.setOnClickListener {
            setBulbValue()
        }
        ivBedroomSettings.setOnClickListener {
            Intent(context, BedroomSettings::class.java).also {
                startActivity(it)
            }
        }
        getRealtimeUpdates()
    }

    private fun getRealtimeUpdates(){

        if (btnAutomaticBedroom == null) {
            Log.d("TAG", "btnAutomaticBedroom is null")
        }

        if (btnManualBedroom == null) {
            Log.d("TAG", "btnManualBedroom is null")
        }

        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

            listenerRegistration = userRef.addSnapshotListener(EventListener<DocumentSnapshot> {snapshot, e ->
                if(e != null){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    return@EventListener
                }

                if(snapshot != null && snapshot.exists()){

                    val bulb = snapshot.get("bulbOnOffBedroom").toString().toBoolean()
                    val manualAuto = snapshot.get("manualAutoBedroom").toString().toBoolean()

                    when(bulb){
                        true -> {
                            bulbValue = bulb
                            ivBedroomBulb.setImageResource(R.drawable.bulb_on)
                        }
                        false -> {
                            bulbValue = bulb
                            ivBedroomBulb.setImageResource(R.drawable.bulb_off)
                        }
                    }
                    when(manualAuto){
                        true -> {
                            btnManualAuto = manualAuto
                            btnAutomaticBedroom.setBackgroundResource(R.drawable.btn_shape_round_disable)
                            btnAutomaticBedroom.setTextColor(Color.GRAY)
                            btnAutomaticBedroom.isClickable = false

                            btnManualBedroom.setBackgroundResource(R.drawable.btn_shape_round)
                            btnManualBedroom.setTextColor(Color.WHITE)
                            btnManualBedroom.isClickable = true

                            ivBedroomBulb.isClickable = false
                            tvInstructionBedroom.text = ""
                        }
                        false -> {
                            btnManualAuto = manualAuto
                            btnManualBedroom.setBackgroundResource(R.drawable.btn_shape_round_disable)
                            btnManualBedroom.setTextColor(Color.GRAY)
                            btnManualBedroom.isClickable = false

                            btnAutomaticBedroom.setBackgroundResource(R.drawable.btn_shape_round)
                            btnAutomaticBedroom.setTextColor(Color.WHITE)
                            btnAutomaticBedroom.isClickable = true


                            ivBedroomBulb.isClickable = true
                            tvInstructionBedroom.text = "Tap to manually turn on/off"

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
            false -> userRef.update("bulbOnOffBedroom", true)
            true -> userRef.update("bulbOnOffBedroom", false)

        }
    }

    private fun setManualAutoValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        when(btnManualAuto){
            false ->  userRef.update("manualAutoBedroom", false)
            true -> userRef.update("manualAutoBedroom", true)

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}