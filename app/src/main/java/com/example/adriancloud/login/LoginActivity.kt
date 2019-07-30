package com.example.adriancloud.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.adriancloud.R
import com.example.adriancloud.utils.show
import com.google.firebase.auth.FirebaseAuth
import android.view.MotionEvent
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    //Firebase Connection
    private lateinit var auth: FirebaseAuth

    //Components
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var bttnLogin: Button
    private lateinit var progressBar: ProgressBar

    //Codes
    private val VALID_TEXT = 1
    private val EMPTY_TEXT = -1
    private val TOO_LONG_TEXT = -2
    private val TOO_SHORT_TEXT = -3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Obtener referencias de los componentes
        activateComponents()

        // Show Keybouard to after 0.6 secs
        show(editEmail, this, 600)


    }

    //Tap to dismiss
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun activateComponents() {
        editEmail = findViewById<EditText>(R.id.edit_username)
        editPassword = findViewById<EditText>(R.id.edit_password)
        bttnLogin = findViewById<Button>(R.id.bttn_login)
        bttnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val pass = editPassword.text.toString()

            val isEmailValid = checkIfTextFilledCorrectly(email, 6, 40)
            val isPassValid = checkIfTextFilledCorrectly(pass, 4, 20)

            if (isEmailValid == VALID_TEXT && isPassValid == VALID_TEXT) {
                showMessage("Attempting to Login")
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser

                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage("Not registered email or wrong password")
                        }
                    }
            } else {
                showMessageError("Email", isPassValid)
                showMessageError("Password", isPassValid)
            }
        }

        progressBar = findViewById<ProgressBar>(R.id.login_progress)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    private fun checkIfTextFilledCorrectly(text: String, min: Int, max: Int): Int {
        if (text.isEmpty()) return EMPTY_TEXT
        if (text.length < min) return TOO_SHORT_TEXT
        if (text.length > max) return TOO_LONG_TEXT
        return VALID_TEXT
    }

    private fun showMessageError(field: String, code: Int) {
        when (code) {
            EMPTY_TEXT -> showMessage(field + " is empty")
            TOO_LONG_TEXT -> showMessage(field + " is too long")
            TOO_SHORT_TEXT -> showMessage(field + "is too short")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun completeLogin(user: FirebaseUser){

    }
}
