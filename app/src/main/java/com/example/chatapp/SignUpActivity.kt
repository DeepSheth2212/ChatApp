package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var password : EditText
    private lateinit var email : EditText
    private lateinit var name : EditText
    private lateinit var signUpButton : Button
    private lateinit var toSignIn : TextView
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)
        mAuth =  FirebaseAuth.getInstance()
        password = findViewById(R.id.Password)
        email = findViewById(R.id.email)
        name = findViewById(R.id.username)
        signUpButton = findViewById(R.id.SU)
        toSignIn = findViewById(R.id.textview)

        signUpButton.setOnClickListener {
            val emailstr = email.text.toString()
            val passstr = password.text.toString()
            val namestr = name.text.toString()
            signUp(namestr,emailstr,passstr)
        }

        toSignIn.setOnClickListener{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(name : String , email : String , password : String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this , MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this , "Something went wrong!" , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("Users").child(uid).setValue(User(name,email,uid))

    }
}