package com.example.myquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myquiz.databinding.ActivityMainBinding
import com.example.myquiz.modle.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //when user register yourself with SignUp button and we store his data in db
        binding.signUp.setOnClickListener {
            if (binding.userName.text.toString().equals("") ||
                binding.userMail.text.toString().equals("") ||
                binding.userPassword.text.toString().equals("")
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            //when user enter correct details
            else {
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.userMail.text.toString(),
                    binding.userPassword.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //Here we store the user information in database for future reference
                            var user = User(
                                binding.userName.text.toString(),
                                binding.userMail.text.toString(),
                                binding.userPassword.text.toString()
                            )

                            Toast.makeText(
                                this, "Sign up once, and enjoy seamless access " +
                                        "without the hassle of repeated logins.", Toast.LENGTH_LONG
                            ).show()

                            //here we write user details in our db -
                            Firebase.database.reference.child("Users")
                                .child(Firebase.auth.currentUser!!.uid)
                                .setValue(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }
                        } else {
                            //otherwise we throw system generated exception message
                            Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }//SignUp button tasks completed
    }

    //if user is already register then redirected to the HomeActivity
    override fun onStart() {
        super.onStart()
        //when currentUser is not null
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

}
