package com.example.adriancloud.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.adriancloud.R
import com.example.adriancloud.utils.show
import com.google.firebase.auth.FirebaseAuth
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.adriancloud.home.HomeActivity


class LoginActivity : AppCompatActivity() {

    //Firebase Connection
    private lateinit var auth: FirebaseAuth

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

        activateLoginButton()
        activateForgotPassword()
    }

    //Tap to dismiss
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun activateLoginButton() {
        val bttnLogin = findViewById<Button>(R.id.bttn_login)
        bttnLogin.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.login_progress)
            progressBar.visibility = View.VISIBLE

            val editEmail = findViewById<EditText>(R.id.edit_email)
            val editPass = findViewById<EditText>(R.id.edit_password)

            val email = editEmail.text.toString()
            val pass = editPass.text.toString()

            val isEmailValid = checkIfTextFilledCorrectly("Email", email, 6, 40)
            val isPassValid = checkIfTextFilledCorrectly("Passowrd", pass, 4, 20)

            if (isEmailValid == VALID_TEXT && isPassValid == VALID_TEXT) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            completeLogin()

                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage("Not registered email or wrong password")
                        }
                    }
            }
            progressBar.visibility = View.GONE
        }

    }
    fun activateForgotPassword(){
        val tvForgotPass : TextView = findViewById(R.id.bttn_forgot_password)
        tvForgotPass.setOnClickListener{
            val editEmail : EditText = findViewById(R.id.edit_email)
            val editPass : EditText = findViewById(R.id.edit_password)

            editEmail.setText("adrian.jvg.18@gmail.com")
            editPass.setText("123456")
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser!=null){
            completeLogin()
        } else {
            // Show Keybouard to after 0.6 secs
            val editEmail = findViewById<EditText>(R.id.edit_email)
            show(editEmail, this, 600)
        }
    }

    private fun checkIfTextFilledCorrectly(field: String, text: String, min: Int, max: Int): Int {
        if (text.isEmpty()) return showError(field, EMPTY_TEXT)
        if (text.length < min) return showError(field, TOO_SHORT_TEXT)
        if (text.length > max) return showError(field, TOO_LONG_TEXT)
        return VALID_TEXT
    }
    private fun showError(field: String, code: Int): Int {
        when (code) {
            EMPTY_TEXT -> showMessage("$field is empty")
            TOO_LONG_TEXT -> showMessage("$field is too long")
            TOO_SHORT_TEXT -> showMessage("$field is too short")
        }
        return code
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun completeLogin(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
