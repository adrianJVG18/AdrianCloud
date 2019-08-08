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
import com.google.firebase.database.*

class ApiWrapperFragment : Fragment() {

    val POST_FORM_TAG = "Post Form"

    private var dataBase: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth

    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBase = FirebaseDatabase.getInstance().reference


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
        recyclerView = view.findViewById(R.id.apiwrapper_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val posts = ArrayList<Post>()
        postAdapter = PostAdapter(posts, context)
        recyclerView.adapter = postAdapter
        loadPosts()
    }



    fun loadPosts(): ArrayList<Post> {
        val posts: ArrayList<Post> = ArrayList()
        val userId = auth.currentUser!!.uid

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(Post::class.java)
                if (post != null) {
                    posts.add(post)
                    postAdapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        dataBase!!.child("/user-posts/$userId/").addValueEventListener(postListener)
        return posts
    }

    fun onPostCreate(post: Post) {
        val userId = auth.currentUser!!.uid
        val key = dataBase!!.child("posts").child(userId).push().key

        if (key == null) {
            tostear("algo salio mal con la key para el post")
            return
        }
        post.uid = userId
        post.author = auth.currentUser!!.displayName.toString()
        val postValues = post.toMap()

        val childUpdates = HashMap<String, Any?>()
        childUpdates["/user-posts/$userId/$key"] = postValues

        dataBase!!.updateChildren(childUpdates).addOnSuccessListener {
            tostear("Se escribio exitosamente")
        }.addOnFailureListener{
            tostear("Algo definitivamente no esta saliendo bien")
        }

    }



    fun tostear (message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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


