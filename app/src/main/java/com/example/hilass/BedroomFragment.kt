package com.example.hilass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bedroom.*

class BedroomFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bedroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bulbClick = 0

        ivBedroomBulb.setOnClickListener {
            if(bulbClick == 0){
                ivBedroomBulb.setImageResource(R.drawable.bulb_on)
                bulbClick = 1
            }else{
                ivBedroomBulb.setImageResource(R.drawable.bulb_off)
                bulbClick = 0
            }
        }
        ivBedroomSettings.setOnClickListener {
            Intent(context, BedroomSettings::class.java).also {
                startActivity(it)
            }
        }
    }
}