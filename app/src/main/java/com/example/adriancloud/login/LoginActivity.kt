package com.example.adriancloud.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.adriancloud.R
import com.example.adriancloud.utils.show
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //Firebase Connection
    private lateinit var auth: FirebaseAuth

    //Components
    private val editUsername = findViewById<EditText>(R.id.edit_username)
    private val editPassword = findViewById<EditText>(R.id.edit_password)
    private val bttnLogin = findViewById<Button>(R.id.bttn_login)
    private val progressBar = findViewById<ProgressBar>(R.id.login_progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Show Keybouard to after 0.6 secs
        show(editUsername, this, 600)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

    }



}
