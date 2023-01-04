package com.example.hilass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_bedroom.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BedroomFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var personCollectionRef = Firebase.firestore.collection("persons")

    private var bulbValue = 0

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
        val user = auth.currentUser
        val uid = user!!.uid
        val userRef = personCollectionRef.document(uid)

            userRef.addSnapshotListener(EventListener<DocumentSnapshot> {snapshot, e ->
                if(e != null){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    return@EventListener
                }
                if(snapshot != null && snapshot.exists()){
                    val test = snapshot.get("test").toString().toInt()
                        if(test == 1){
                            bulbValue = test
                            ivBedroomBulb.setImageResource(R.drawable.bulb_on)
                        }else{
                            bulbValue = test
                            ivBedroomBulb.setImageResource(R.drawable.bulb_off)
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
            0 -> userRef.update("test", 1)
            1 -> userRef.update("test", 0)

        }
    }
}