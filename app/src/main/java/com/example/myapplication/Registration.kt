package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val regMail: EditText = findViewById(R.id.editText_email_register)
        val regPassword: EditText = findViewById(R.id.editText_password_register)
        val personalID: EditText = findViewById(R.id.editText_id)

        val regtologButton: Button = findViewById<Button>(R.id.button_regtolog)
        regtologButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        val registerButton: Button = findViewById(R.id.button_register)
        registerButton.setOnClickListener{
            val e_mail = regMail.text.toString()
            val password = regPassword.text.toString()
            val personal_ID = personalID.text.toString()

            if(e_mail.isEmpty()||password.isEmpty()||personal_ID.isEmpty()){
                Toast.makeText(this, "Enter all required information", Toast.LENGTH_LONG).show()
            }
            else if(password.length<6){
                Toast.makeText(this, "Password needs to contain at least 6 symbols", Toast.LENGTH_LONG).show()
            }
            else{
                auth.createUserWithEmailAndPassword(e_mail, password)
                    .addOnCompleteListener{
                    if(it.isSuccessful){
                        val user : User = User(0, personal_ID)
                        db.collection("users").document(auth.currentUser!!.uid).set(user).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this, "Unable to perform registration", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this, "Unable to perform registration", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}