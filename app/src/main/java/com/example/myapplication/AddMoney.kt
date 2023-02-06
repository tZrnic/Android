package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAddMoneyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddMoney : AppCompatActivity() {
    private lateinit var binding: ActivityAddMoneyBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.imageViewBackAdd.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

        val userInformation = getDataFromUser(auth.currentUser!!.uid)
        Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {result ->
                val userInformation = result.toObject(User::class.java)
                binding.textViewBalance.text = userInformation?.balance.toString()
            }

        binding.imageViewUplataAdd.setOnClickListener{
            val cardNumber = binding.editTextCardNumber.text.toString()
            val cvv = binding.editTextCvv.text.toString()
            val amount = binding.editTextEnterAmountAdd.text.toString()
            if(cardNumber.isEmpty()||cvv.isEmpty()||amount.isEmpty()){
                Toast.makeText(this, "Enter required information", Toast.LENGTH_SHORT).show()
            }
            else if(cvv.length!=3){
                Toast.makeText(this, "CVV needs to contain exactly 3 numbers", Toast.LENGTH_LONG).show()
            }
            else if(amount.toInt()<=0){
                Toast.makeText(this, "Inserted amount can not be zero or less than zero", Toast.LENGTH_LONG).show()
            }
            else{
                Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            val currentAmount = task.result.toObject(User::class.java)?.balance.toString().toInt()
                            increaseBalance(currentAmount+amount.toInt())
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }

    }

    private fun increaseBalance(amount: Int){
        Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
            .update("balance", amount)
    }

    private fun getDataFromUser(uid: String):User{
        var user = User()
        Firebase.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { result ->
                user = result.toObject(User::class.java)!!
            }
        return user
    }
}
