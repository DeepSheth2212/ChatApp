package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var password : EditText
    private lateinit var email : EditText
    private lateinit var signInButton : Button
    private lateinit var toSignUp : TextView

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide() //? for null safety
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        mAuth = FirebaseAuth.getInstance()

        password = findViewById(R.id.Password)
        email = findViewById(R.id.email)
        signInButton = findViewById(R.id.SI)
        toSignUp = findViewById(R.id.textview)

        signInButton.setOnClickListener {
            val emailstr = email.text.toString()
            val passstr = password.text.toString()
            signIn(emailstr,passstr)
        }

        toSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signIn(email: String , password : String){
        //for signin user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this , MainActivity::class.java)
                    finish()
                    startActivity(intent)


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this , "User not found!" , Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this , MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}