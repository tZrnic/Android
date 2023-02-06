package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityPayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Payout : AppCompatActivity() {
    private lateinit var binding: ActivityPayoutBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val db = Firebase.firestore
        binding.imageViewBackPayout.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val userInformation = getUserData(auth.currentUser!!.uid)
        binding.textViewPersonalId.text = userInformation?.personal_id.toString()
        binding.textViewBalance.text = userInformation?.balance.toString()
        db.collection("users").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {result ->
                val userInformation = result.toObject(User::class.java)
                binding.textViewPersonalId.text = userInformation?.personal_id.toString()
                binding.textViewBalance.text = userInformation?.balance.toString()
            }

        binding.imageViewPayout.setOnClickListener {
            val recipient = binding.editTextRecipientIdPayout.text.toString()
            val amount = binding.editTextAmountPayout.text.toString()
            if (recipient.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Enter all required information", Toast.LENGTH_LONG).show()
            } else {
                db.collection("users").document(auth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                            if (amount.toInt() > currentAmount!!) {
                                Toast.makeText(this, "Not enough money on your account", Toast.LENGTH_LONG).show()
                            }
                            else{
                                checkRecipientsID(recipient, currentAmount.toInt()-amount.toInt(), amount.toInt())
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

    private fun getUserData(uid: String): User {
        var user = User()
        Firebase.firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { result ->
                user = result.toObject(User::class.java)!!
            }
            .addOnFailureListener{e->
                Log.w("MainActivity", "Error")
            }
        return user
    }

    private fun checkRecipientsID(personal_id: String, newAmount: Int, amount: Int){
        Firebase.firestore.collection("users").whereEqualTo("personal_id", personal_id)
            .get()
            .addOnSuccessListener {
                performTransaction(newAmount, personal_id, amount)
            }
    }

    private fun performTransaction(newAmount: Int, personal_id: String, amount: Int){
        Firebase.firestore.collection("users").whereEqualTo("personal_id", personal_id)
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    if(!task.result.isEmpty) {
                        var currentAmount = task.result.documents[0].toObject(User::class.java)?.balance
                        Firebase.firestore.collection("users").document(task.result.documents[0].id)
                            .update("balance", currentAmount!! + amount)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Transaction complete!", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Transaction could not be completed!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                    else{
                        Toast.makeText(this, "User with that ID not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
            .update("balance", newAmount)
    }
}