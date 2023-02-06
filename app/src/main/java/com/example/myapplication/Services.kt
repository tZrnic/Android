package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityAddMoneyBinding
import com.example.myapplication.databinding.ActivityServicesBinding

class Services : AppCompatActivity() {
    private lateinit var binding: ActivityServicesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewBackServices.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}