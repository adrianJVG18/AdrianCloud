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


class PostFormFragment(val formMode: Int) : Fragment() {

    // Asi se instancia cosas static
    companion object {
        val ADDING_POST: Int = 1
        val UPDATE_POST: Int = 2
    }

    val FORM_MODE: Int = formMode
    lateinit var postUpdated: Post

    //Components
    lateinit var actionText: TextView
    lateinit var titleEdit: EditText
    lateinit var bodyEdit: EditText
    lateinit var acceptPostButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionText = view.findViewById(R.id.postform_textV_post_action)
        acceptPostButton = view.findViewById(R.id.bttn_accept_post)
        val cancelButton: Button = view.findViewById(R.id.bttn_cancel_post)
        titleEdit = view.findViewById(R.id.postform_edit_title)
        bodyEdit = view.findViewById(R.id.postform_edit_body)

        updateUI()

        acceptPostButton.setOnClickListener {
            if (titleEdit.text.isEmpty()) {
                val errorMessage = "El post al menos debe contener un titulo!"
                Toast.makeText(context!!.applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                when (FORM_MODE) {
                    ADDING_POST -> {
                        val post = Post(titleEdit.text.toString(), bodyEdit.text.toString())
                        postResponse.onCreatedPost(post)
                    }
                    UPDATE_POST -> {
                        postUpdated.title = titleEdit.text.toString()
                        postUpdated.body = bodyEdit.text.toString()

                        postResponse.onUpdatedPost(postUpdated)
                    }
                }
            }
        }


        cancelButton.setOnClickListener {
            postResponse.onCanceledPost(FORM_MODE)
        }
    }

    private fun updateUI() {
        when (FORM_MODE) {
            ADDING_POST -> {
                actionText.text = context!!.getString(R.string.a_ada_un_nuevo_post)
            }
            UPDATE_POST -> {
                actionText.text = context!!.getString(R.string.modifique_el_post)
                acceptPostButton.text = context!!.getString(R.string.modificar_post)
                titleEdit.setText(postUpdated.title)
                bodyEdit.setText(postUpdated.body)
            }
        }
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