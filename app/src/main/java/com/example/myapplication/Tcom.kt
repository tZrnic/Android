package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityTcomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Tcom : AppCompatActivity() {
    private lateinit var binding: ActivityTcomBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTcomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.imageViewBackTcom.setOnClickListener {
            intent = Intent(this, Services::class.java)
            finish()
            startActivity(intent)
        }

        binding.button5e.setOnClickListener{
            binding.textViewVoucher.text = "5"
        }

        binding.button10e.setOnClickListener{
            binding.textViewVoucher.text = "10"
        }

        binding.button20e.setOnClickListener{
            binding.textViewVoucher.text = "20"
        }

        binding.button30e.setOnClickListener{
            binding.textViewVoucher.text = "30"
        }

        binding.buttonBuyVoucher.setOnClickListener{
            val voucher = binding.textViewVoucher.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            if(voucher.isEmpty()||phoneNumber.isEmpty()){
                Toast.makeText(this, "Enter all required information", Toast.LENGTH_LONG).show()
            }
            else if(phoneNumber.length!=10){
                Toast.makeText(this, "Phone number needs to contain 10 numbers", Toast.LENGTH_SHORT).show()
            }
            else{
                Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener{task->
                        if (task.isSuccessful) {
                            var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                            if (voucher.toInt() > currentAmount!!) {
                                Toast.makeText(this, "Not enough money on your account", Toast.LENGTH_LONG).show()
                            }
                            else{
                                performTransaction(voucher.toInt())
                                Toast.makeText(this, "Voucher purchased!", Toast.LENGTH_LONG).show()
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
        Firebase.firestore.collection("users").document("0T_com123")
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                    Firebase.firestore.collection("users").document("0T_com123")
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