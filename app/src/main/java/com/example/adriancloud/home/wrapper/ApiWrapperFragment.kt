package com.example.adriancloud.home.wrapper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ApiWrapperFragment : Fragment() {

    val ADD_POST_TAG = "Add Post"

    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null

    interface ICallAddPostForm {
        fun callAddPostFrom()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReference = FirebaseDatabase.getInstance().getReference("message")

        val bttnAddPost : FloatingActionButton = view.findViewById(R.id.bttn_call_addpost_form)
        bttnAddPost.setOnClickListener {
            //val callAddPost = fragmentManager.beginTransaction() .add(AddPostFragment(), ADD_POST_TAG) .addToBackStack(ADD_POST_TAG) .commit()

        }

        val context = activity!!.baseContext
        val recyclerView : RecyclerView = view.findViewById(R.id.apiwrapper_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = PostAdapter(loadPosts(), context)
    }



    fun loadPosts() : ArrayList<Post>{
        val posts : ArrayList<Post> = ArrayList()

        posts.add(Post("primer post", "un comentario"))
        posts.add(Post("algo 1", "alguito"))
        posts.add(Post("algo 2", "algodon"))

        return posts
    }
}