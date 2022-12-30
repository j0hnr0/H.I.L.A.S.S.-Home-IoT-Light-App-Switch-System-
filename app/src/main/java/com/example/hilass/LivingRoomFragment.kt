package com.example.hilass

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bedroom.*
import kotlinx.android.synthetic.main.fragment_living_room.*

class LivingRoomFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_living_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bulbClick = 0

        ivLivingRoomBulb.setOnClickListener {
            if(bulbClick == 0){
                ivLivingRoomBulb.setImageResource(R.drawable.bulb_on)
                bulbClick = 1
            }else{
                ivLivingRoomBulb.setImageResource(R.drawable.bulb_off)
                bulbClick = 0
            }
        }
        ivLivingRoomSettings.setOnClickListener {
            Intent(context, LivingRoomSettings::class.java).also {
                startActivity(it)
            }
        }
    }
}