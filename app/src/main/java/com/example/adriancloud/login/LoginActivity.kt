package com.example.adriancloud.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.adriancloud.R
import com.example.adriancloud.utils.show

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editUsername = findViewById<EditText>(R.id.edit_username)
        val editPassword = findViewById<EditText>(R.id.edit_password)
        val bttnLogin = findViewById<Button>(R.id.bttn_login)
        val progressBar = findViewById<ProgressBar>(R.id.login_progress)

        show(editUsername, this, 600)

    }

}
