package com.example.hilass

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
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

class LivingRoomFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_living_room, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val addContactDialog = AlertDialog.Builder(requireContext())
            .setTitle("Automatic Settings Preferences")
            .setMessage("Please set your Automatic settings preferences")
            .setIcon(R.drawable.hilassicon)
            .setPositiveButton("Go to settings") { _, _ ->
                Intent(requireContext(), LivingRoomSettings::class.java).also {
                    startActivity(it)
                }
            }
            .setCancelable(false)
            .create()

        btnManualLivingRoom.setOnClickListener {
            btnManualAuto = false
            ivLivingRoomSettings.visibility = View.INVISIBLE
            setManualAutoValue()
        }

        btnAutomaticLivingRoom.setOnClickListener {
            btnManualAuto = true
            ivLivingRoomSettings.visibility = View.VISIBLE
            setManualAutoValue()

            addContactDialog.show()
        }

        ivLivingRoomSettings.setOnClickListener {
            setLivingRoomSettings()
            Intent(requireContext(), LivingRoomSettings::class.java).also {
                startActivity(it)
            }
        }

        ivLivingRoomBulb.setOnClickListener {
            setBulbValue()
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

                val bulb = snapshot.get("bulbOnOffLivingRoom").toString().toBoolean()
                val manualAuto = snapshot.get("manualAutoLivingRoom").toString().toBoolean()

                when(bulb){
                    true -> {
                        bulbValue = bulb
                        ivLivingRoomBulb.setImageResource(R.drawable.bulb_on)
                    }
                    false -> {
                        bulbValue = bulb
                        ivLivingRoomBulb.setImageResource(R.drawable.bulb_off)
                    }
                }
                when(manualAuto){
                    true -> {
                        btnManualAuto = manualAuto
                        btnAutomaticLivingRoom.setBackgroundResource(R.drawable.btn_shape_round_disable)
                        btnAutomaticLivingRoom.setTextColor(Color.GRAY)
                        btnAutomaticLivingRoom.isClickable = false

                        btnManualLivingRoom.setBackgroundResource(R.drawable.btn_shape_round)
                        btnManualLivingRoom.setTextColor(Color.WHITE)
                        btnManualLivingRoom.isClickable = true

                        ivLivingRoomBulb.isClickable = false
                        tvInstructionLivingRoom.text = ""
                        ivLivingRoomSettings.visibility = View.VISIBLE
                    }
                    false -> {
                        btnManualAuto = manualAuto
                        btnManualLivingRoom.setBackgroundResource(R.drawable.btn_shape_round_disable)
                        btnManualLivingRoom.setTextColor(Color.GRAY)
                        btnManualLivingRoom.isClickable = false

                        btnAutomaticLivingRoom.setBackgroundResource(R.drawable.btn_shape_round)
                        btnAutomaticLivingRoom.setTextColor(Color.WHITE)
                        btnAutomaticLivingRoom.isClickable = true


                        ivLivingRoomBulb.isClickable = true
                        tvInstructionLivingRoom.text = "Tap to manually turn on/off"

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
            false -> userRef.update("bulbOnOffLivingRoom", true)
            true -> userRef.update("bulbOnOffLivingRoom", false)

        }
    }

    private fun setManualAutoValue(){

        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        when(btnManualAuto){
            false -> {
                userRef.update("manualAutoLivingRoom", false)

                userRef.update("swLivingRoomCustomize", false)
                userRef.update("swLivingRoomMovementOnly", false)
                userRef.update("swLivingRoomPerson", false)
                userRef.update("swLivingRoomMode", false)
                userRef.update("swLivingRoomAmbientLighting", false)
                userRef.update("swLivingRoomNightLight", false)
                userRef.update("swLivingRoomNotification", false)
            }
            true -> userRef.update("manualAutoLivingRoom", true)
        }

    }

    private fun setLivingRoomSettings(){

        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        userRef.update("swLivingRoomCustomize", false)
        userRef.update("swLivingRoomMovementOnly", false)
        userRef.update("swLivingRoomPerson", false)
        userRef.update("swLivingRoomMode", false)
        userRef.update("swLivingRoomAmbientLighting", false)
        userRef.update("swLivingRoomNightLight", false)
        userRef.update("swLivingRoomNotification", false)


    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}