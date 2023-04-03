package com.example.hilass

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.Duration
import java.time.Instant

class LivingRoomFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var bulbValue = false
    private var btnManualAuto = false
    private var count = 1

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_living_room, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                var timestamp: Long? = snapshot.get("timestampLivingRoom") as Long?
                var bulbLifeSpan: Long = snapshot.get("bulbLifeSpanLivingRoom").toString().toLong()

                when(bulb){
                    true -> {
                        bulbValue = bulb
                        ivLivingRoomBulb.setImageResource(R.drawable.bulb_on)


                        if(count > 1) {
                            val bulbOnTimestamp: Long = Instant.now().toEpochMilli()

                            userRef.update("timestampLivingRoom", bulbOnTimestamp)


                        }
                        count = 1


                        tvBedroomBulbLifeSpan.text = "Life Span: $bulbLifeSpan hours"
                    }
                    false -> {
                        bulbValue = bulb
                        ivLivingRoomBulb.setImageResource(R.drawable.bulb_off)

                        if(bulbLifeSpan > 0 && timestamp != null && count <= 1){

                            val durationMillis: Long =  Instant.now().toEpochMilli() - timestamp
                            val durationHours: Long = durationMillis / 3600000L
                            bulbLifeSpan -= durationHours

                            Log.d("LivingRoomFragment", "Timestamp: $timestamp")
                            Log.d("LivingRoomFragment", "Duration: $durationHours")
                            Log.d("LivingRoomFragment", "DurationMillis: $durationMillis")
                            Log.d("LivingRoomFragment", "LifeSpan: $bulbLifeSpan")

                            userRef.update("bulbLifeSpanLivingRoom", bulbLifeSpan)
                            userRef.update("timestampLivingRoom", null)



                        }
                        count = 2
                        tvBedroomBulbLifeSpan.text = "Life Span: $bulbLifeSpan hours"

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTrueDuration(booleanVariable: Boolean, trueTimestamp: Instant?): Long {
        var durationMinutes = 0L

        if (booleanVariable) {
            trueTimestamp?.let { timestamp ->
                val nowInstant = Instant.now()
                val duration = Duration.between(timestamp, nowInstant)
                durationMinutes = duration.toHours()
            }
        }

        return durationMinutes
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