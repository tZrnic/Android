package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        auth = Firebase.auth

        binding.buttonMainToServices.setOnClickListener{
            intent = Intent(this, Services::class.java)
            startActivity(intent)
            finish()
        }

        binding.imageViewIsplataMain.setOnClickListener {
            intent = Intent(this, Payout::class.java)
            startActivity(intent)
            finish()
        }

        binding.imageViewUplataMain.setOnClickListener {
            intent = Intent(this, AddMoney::class.java)
            startActivity(intent)
            finish()
        }


        binding.imageViewLogOut.setOnClickListener{
            auth.signOut()
            intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


        val userInformation = getDataFromUser(auth.currentUser!!.uid)
        db.collection("users").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {result ->
                val userInformation = result.toObject(User::class.java)
                binding.textViewPersonalId.text = userInformation?.personal_id.toString()
                binding.textViewBalance.text = userInformation?.balance.toString()
            }
    }

    private fun getDataFromUser(uid: String):User{
        var user = User()
        Firebase.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { result ->
                user = result.toObject(User::class.java)!!
            }
            .addOnFailureListener{e->
                Log.w("MainActivity", "Error")
            }
        return user
    }
}