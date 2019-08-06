package com.example.adriancloud.home.wrapper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ApiWrapperFragment : Fragment() {

    val POST_FORM_TAG = "Post Form"

    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReference = FirebaseDatabase.getInstance().getReference("message")
        auth = FirebaseAuth.getInstance()


        val bttnAddPost: FloatingActionButton = view.findViewById(R.id.bttn_call_addpost_form)
        bttnAddPost.setOnClickListener {
            if (auth.currentUser?.displayName == null) {
                Toast.makeText(
                    context!!.applicationContext,
                    "Para añadir un post, tienes que configurar tu nombre de usuario",
                    Toast.LENGTH_SHORT
                ).show()
                requestToHomeActivity.callUpdateProfile()
            } else {
                requestToHomeActivity.callAddPostForm(PostFormFragment.ADDING_POST)
            }
        }

        val context = activity!!.baseContext
        val recyclerView: RecyclerView = view.findViewById(R.id.apiwrapper_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = PostAdapter(loadPosts(), context)
    }

    fun loadPosts(): ArrayList<Post> {
        val posts: ArrayList<Post> = ArrayList()

        posts.add(Post("primer post", "un comentario"))
        posts.add(Post("algo 1", "alguito"))
        posts.add(Post("algo 2", "algodon"))

        return posts
    }

    fun onPostCreated(post: Post) {
        val user = auth.currentUser.toString()
        
        mDatabase!!.child("posts").setValue("Posts")

        mDatabase!!.child("posts").child(user)
    }


    // Declaraciones necesarias para conectar este fragment con su activity

    lateinit var requestToHomeActivity: ApiWrapperInterface

    fun setOnCalledPostFormListener(callback: ApiWrapperInterface) {
        this.requestToHomeActivity = callback
    }

    interface ApiWrapperInterface {
        fun callAddPostForm(postFormMode: Int)
        fun callUpdateProfile()
    }
}