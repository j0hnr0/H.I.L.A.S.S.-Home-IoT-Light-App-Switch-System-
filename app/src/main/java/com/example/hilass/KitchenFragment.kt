package com.example.hilass

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
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

class KitchenFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
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
                Intent(requireContext(), KitchenSettings::class.java).also {
                    startActivity(it)
                }
            }
            .setCancelable(false)
            .create()

        btnManualKitchen.setOnClickListener {
            btnManualAuto = false
            ivKitchenSettings.visibility = View.INVISIBLE
            setManualAutoValue()
        }

        btnAutomaticKitchen.setOnClickListener {
            btnManualAuto = true
            ivKitchenSettings.visibility = View.VISIBLE
            setManualAutoValue()

            addContactDialog.show()
        }


        ivKitchenSettings.setOnClickListener {
            setKitchenSettings()
            Intent(requireContext(), KitchenSettings::class.java).also {
                startActivity(it)
            }
        }

        ivKitchenBulb.setOnClickListener {
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

                val bulb = snapshot.get("bulbOnOffKitchen").toString().toBoolean()
                val manualAuto = snapshot.get("manualAutoKitchen").toString().toBoolean()
                var timestamp: Long? = snapshot.get("timestampKitchen") as Long?
                var bulbLifeSpan: Long = snapshot.get("bulbLifeSpanKitchen").toString().toLong()

                when(bulb){
                    true -> {
                        bulbValue = bulb
                        ivKitchenBulb.setImageResource(R.drawable.bulb_on)


                        if(count > 1) {
                            val bulbOnTimestamp: Long = Instant.now().toEpochMilli()

                            userRef.update("timestampKitchen", bulbOnTimestamp)


                        }
                        count = 1


                        tvKitchenBulbLifeSpan.text = "Life Span: $bulbLifeSpan hours"
                    }
                    false -> {
                        bulbValue = bulb
                        ivKitchenBulb.setImageResource(R.drawable.bulb_off)

                        if(bulbLifeSpan > 0 && timestamp != null && count <= 1){

                            val durationMillis: Long =  Instant.now().toEpochMilli() - timestamp
                            val durationHours: Long = durationMillis / 3600000L
                            bulbLifeSpan -= durationHours

                            Log.d("KitchenFragment", "Timestamp: $timestamp")
                            Log.d("KitchenFragment", "Duration: $durationHours")
                            Log.d("KitchenFragment", "DurationMillis: $durationMillis")
                            Log.d("KitchenFragment", "LifeSpan: $bulbLifeSpan")

                            userRef.update("bulbLifeSpanKitchen", bulbLifeSpan)
                            userRef.update("timestampKitchen", null)



                        }
                        count = 2
                        tvKitchenBulbLifeSpan.text = "Life Span: $bulbLifeSpan hours"
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
                        ivKitchenSettings.visibility = View.VISIBLE
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
            false -> userRef.update("bulbOnOffKitchen", true)
            true -> userRef.update("bulbOnOffKitchen", false)

        }
    }

    private fun setManualAutoValue(){
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        when(btnManualAuto){
            false -> {
                userRef.update("manualAutoKitchen", false)

                userRef.update("swKitchenCustomize", false)
                userRef.update("swKitchenMovementOnly", false)
                userRef.update("swKitchenPerson", false)
                userRef.update("swKitchenMode", false)
                userRef.update("swKitchenAmbientLighting", false)
                userRef.update("swKitchenNightLight", false)
                userRef.update("swKitchenNotification", false)
            }
            true -> userRef.update("manualAutoKitchen", true)

        }
    }

    private fun setKitchenSettings() {
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

        userRef.update("swKitchenCustomize", false)
        userRef.update("swKitchenMovementOnly", false)
        userRef.update("swKitchenPerson", false)
        userRef.update("swKitchenMode", false)
        userRef.update("swKitchenAmbientLighting", false)
        userRef.update("swKitchenNightLight", false)
        userRef.update("swKitchenNotification", false)

    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}