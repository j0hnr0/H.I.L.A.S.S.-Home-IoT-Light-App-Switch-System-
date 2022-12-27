package com.example.hilass

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bedroom.*
import kotlinx.android.synthetic.main.fragment_kitchen.*
import kotlinx.android.synthetic.main.fragment_living_room.*

class KitchenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bulbClick = 0

        ivKitchenBulb.setOnClickListener {
            if(bulbClick == 0){
                ivKitchenBulb.setImageResource(R.drawable.bulb_on)
                bulbClick = 1
            }else{
                ivKitchenBulb.setImageResource(R.drawable.bulb_off)
                bulbClick = 0
            }
        }
    }
}