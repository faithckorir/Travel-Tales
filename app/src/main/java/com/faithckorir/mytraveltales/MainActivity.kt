package com.faithckorir.mytraveltales

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.faithckorir.mytraveltales.apputils.GlobalFunctions
import com.faithckorir.mytraveltales.apputils.GlobalFunctions.sweetProgressBar
import com.faithckorir.mytraveltales.apputils.toastMessage
import com.faithckorir.mytraveltales.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


/**
 * The main activity of the MyTravelTales app.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth  // Firebase Authentication instance
    private lateinit var binding: ActivityMainBinding // View Binding instance
    private lateinit var email: String // User email
    private lateinit var progressDialog: SweetAlertDialog

    private var password: String? = null // User password
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeApp()

    }

    private fun initializeApp() {
        progressDialog = sweetProgressBar()
        auth = FirebaseAuth.getInstance()
        setUpClickListeners()


    }

    private fun setUpClickListeners() {
        binding.signUpButton.setOnClickListener { _ ->
            if (validateLogin()) {
                //doSignUp() // Proceed with sign-up
                doLogin()
            }
        }
    }

    private fun resetPassword() {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Password reset email sent.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Failed to send password reset email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * Attempts to create a new user account with the provided email and password.
     */
    private fun doSignUp() {
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password!!)
            .addOnCompleteListener { task: Task<AuthResult> ->
                progressDialog.dismiss()
                if (task.isSuccessful && task.result.user != null) {
                    // Registration successful
                    // You can perform additional actions here
                } else {
                    // Registration failed

                    Toast.makeText(applicationContext, "Registration failed. ${task.exception}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
    private fun doLogin() {
        auth.signInWithEmailAndPassword(email, password!!)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    toastMessage("success login")
                    // You can perform additional actions here, like navigating to a new activity
                } else {
                    // Login failed
                    Toast.makeText(applicationContext, "Login failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Validates the login credentials before attempting to sign up.
     *
     * @return true if the login credentials are valid, false otherwise.
     */
    private fun validateLogin(): Boolean {
        email = binding.emailEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()
        if (TextUtils.isEmpty(email) || !GlobalFunctions. isEmailValid(email)) {
            binding.tilEmailAddress.error = "Please enter a valid email address"
            binding.tilEmailAddress.error
            return false
        }
        if (TextUtils.isEmpty(password) || password!!.length < MIN_PASSWORD_LENGTH) {
            binding.tilPassword.error = getString(R.string.error_invalid_password)
            return false
        }
        return true
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6 // Minimum length for passwords
    }
}