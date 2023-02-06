package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityParkingBinding
import com.example.myapplication.databinding.ActivityTcomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Parking : AppCompatActivity() {
    private lateinit var binding: ActivityParkingBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.imageViewBackParking.setOnClickListener {
            intent = Intent(this, Services::class.java)
            finish()
            startActivity(intent)
        }

        binding.buttonZone0.setOnClickListener{
            binding.textViewZoneSelected.text = "0"
        }

        binding.buttonZone1.setOnClickListener{
            binding.textViewZoneSelected.text = "1"
        }

        binding.buttonZone2.setOnClickListener{
            binding.textViewZoneSelected.text = "2"
        }

        binding.buttonZone3.setOnClickListener{
            binding.textViewZoneSelected.text = "3"
        }

        binding.buttonBuyParkingTicket.setOnClickListener{
            val zone = binding.textViewZoneSelected.text.toString()
            val hours = binding.editTextHours.text.toString()
            if(zone.isEmpty()||hours.isEmpty()){
                Toast.makeText(this, "Enter all required information", Toast.LENGTH_LONG).show()
            }
            else if(hours.toInt()<1||hours.toInt()>24){
                Toast.makeText(this, "You can enter hours between 1 and 24", Toast.LENGTH_SHORT).show()
            }
            else{
                Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener{task->
                        if (task.isSuccessful) {
                            var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                            if ((zone+1).toInt()*(hours.toInt()) > currentAmount!!) {
                                Toast.makeText(this, "Not enough money on your account", Toast.LENGTH_LONG).show()
                            }
                            else{
                                performTransaction((zone+1).toInt()*(hours.toInt()))
                                Toast.makeText(this, "Parking ticket purchased!", Toast.LENGTH_LONG).show()
                                intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
    private fun performTransaction(amount: Int){
        Firebase.firestore.collection("users").document("0Parking123")
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                    Firebase.firestore.collection("users").document("0Parking123")
                        .update("balance", currentAmount+amount)
                    Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                        .get()
                        .addOnCompleteListener { task->
                            if(task.isSuccessful){
                                var currentAmountUser = task.result.toObject(User::class.java)?.balance.toString().toInt()
                                Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                                    .update("balance", currentAmountUser-amount)
                            }
                        }
                }
                else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }
}