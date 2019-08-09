package com.example.adriancloud.home.wrapper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adriancloud.R



class PostFormFragment : Fragment() {

    // Asi se instancia cosas static
    companion object {
        val ADDING_POST: Int = 1
        val UPDATE_POST: Int = 2
    }

    var FORM_MODE: Int = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_post_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionText: TextView = view.findViewById(R.id.postform_textV_post_action)
        val acceptPostButton: Button = view.findViewById(R.id.bttn_accept_post)
        val cancelButton: Button = view.findViewById(R.id.bttn_cancel_post)
        val titleEdit: EditText = view.findViewById(R.id.postform_edit_title)
        val bodyEdit: EditText = view.findViewById(R.id.postform_edit_body)



        acceptPostButton.setOnClickListener {
            val title: String = titleEdit.text.toString()
            val body: String = bodyEdit.text.toString()

            if (title.isNotEmpty()) {
                val post = Post(title, body)
                postResponse.onCreatedPost(post)
            } else {
                val errorMessage = "El post al menos debe contener un titulo!"
                Toast.makeText(context!!.applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            postResponse.onCanceledPost(FORM_MODE)
        }
    }



    private fun createPost(post: Post) {

    }

    // declaraciones necesarias para conectar conectar este fragment con su activity

    lateinit var postResponse: IPostFormReponse

    fun setOnPostResponseListener(callback: IPostFormReponse) {
        this.postResponse = callback
    }

    interface IPostFormReponse {
        fun onCreatedPost(post: Post)
        fun onUpdatedPost(post: Post)
        fun onCanceledPost(mode: Int)
    }
}