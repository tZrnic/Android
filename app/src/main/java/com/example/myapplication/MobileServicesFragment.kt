package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class MobileServicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mobile_services, container, false)
        val tcomButton = view.findViewById<ImageView>(R.id.imageViewTcom)
        val a1Button = view.findViewById<ImageView>(R.id.imageViewA1)
        tcomButton.setOnClickListener{
            val intent = Intent(context, Tcom::class.java)
            startActivity(intent)
        }

        a1Button.setOnClickListener{
            val intent = Intent(context, A1::class.java)
            startActivity(intent)
        }
        return view
    }
}