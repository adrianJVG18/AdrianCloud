package com.example.adriancloud.home.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.adriancloud.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UpdateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val user = FirebaseAuth.getInstance().currentUser
        val editDisplayName = findViewById<EditText>(R.id.edit_display_name)
        val bttnUpdateProfile = findViewById<Button>(R.id.bttn_update_profile)

        if (user?.displayName == null)
            editDisplayName.hint = "No hay"
        else
            editDisplayName.hint = user.displayName


        bttnUpdateProfile.setOnClickListener{
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(editDisplayName.text.toString())
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo actualizar perfil", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}
