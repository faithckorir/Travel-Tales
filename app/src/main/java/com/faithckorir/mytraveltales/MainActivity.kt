package com.faithckorir.mytraveltales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.faithckorir.mytraveltales.apputils.isEmailValid
import com.faithckorir.mytraveltales.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMainBinding
    private lateinit var email: String
    private lateinit var passWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener {
            if (validateLogin()){
                doSignUp()
            }
        }
    }

    private fun doSignUp() {

        auth.createUserWithEmailAndPassword(email, passWord)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser

                    // You can perform additional actions here
                } else {
                    // Registration failed
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateLogin(): Boolean {
         email = binding.emailEditText.text.toString().trim()
         passWord = binding.passwordEditText.text.toString().trim()

        if (email.isBlank() || !isEmailValid(email)) {
            binding.tilEmailAddress.error = "Please enter a valid email address"
            binding.tilEmailAddress.error
            return false
        }
        if (passWord.isBlank() || passWord.length < 6) {
            binding.tilPassword.error = "Valid Pin is Required"
            binding.tilPassword.error
            return false
        }
        return true
    }
}