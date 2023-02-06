package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityEncBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ENC : AppCompatActivity() {
    private lateinit var binding: ActivityEncBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.imageViewBackENC.setOnClickListener {
            intent = Intent(this, Services::class.java)
            finish()
            startActivity(intent)
        }

        binding.buttonAddToEnc.setOnClickListener{
            val amount = binding.editTextEnc.text.toString()
            if(amount.isEmpty()){
                Toast.makeText(this, "Enter all required information", Toast.LENGTH_LONG).show()
            }
            else if(amount.toInt()<1){
                Toast.makeText(this, "Amount needs to be greater than 0", Toast.LENGTH_SHORT).show()
            }
            else{
                Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener{task->
                        if (task.isSuccessful) {
                            var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                            if (amount.toInt() > currentAmount!!) {
                                Toast.makeText(this, "Not enough money on your account", Toast.LENGTH_LONG).show()
                            }
                            else{
                                performTransaction(amount.toInt())
                                Toast.makeText(this, "Money added to your ENC!", Toast.LENGTH_LONG).show()
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
                    Firebase.firestore.collection("users").document("0ENC123")
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