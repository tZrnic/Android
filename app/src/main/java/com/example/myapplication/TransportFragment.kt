package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class TransportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transport, container, false)
        val parkingButton = view.findViewById<ImageView>(R.id.imageViewParking)
        val ENCButton = view.findViewById<ImageView>(R.id.imageViewEnc)
        parkingButton.setOnClickListener{
            val intent = Intent(context, Parking::class.java)
            startActivity(intent)
        }

        ENCButton.setOnClickListener{
            val intent = Intent(context, ENC::class.java)
            startActivity(intent)
        }
        return view
    }
}