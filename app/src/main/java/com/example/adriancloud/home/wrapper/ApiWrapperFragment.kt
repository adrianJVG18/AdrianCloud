package com.example.adriancloud.home.wrapper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class ApiWrapperFragment : Fragment() {


    // ApiWrapper Data
    val POST_FORM_TAG = "Post Form"
    var DATABASE_POSTS_REF = ""
    var userId: String? = null
    lateinit var posts: MutableList<Post>

    internal var context: Context? = null

    //Firebase
    private lateinit var dataBasePostsRef: DatabaseReference
    private lateinit var dataBase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    // Components
    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var bttnAddPost: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeData()
        activateComponents()

        setDatabaseListeners()
    }

    fun initializeData() {
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        posts = mutableListOf()

        DATABASE_POSTS_REF = "/user-posts/$userId/"
        dataBase = FirebaseDatabase.getInstance()
        dataBasePostsRef = dataBase.getReference(DATABASE_POSTS_REF)

        context = activity!!.baseContext
    }

    fun activateComponents() {

        //Activate Add Post Button
        bttnAddPost = view!!.findViewById(R.id.bttn_call_addpost_form)
        bttnAddPost.setOnClickListener {
            if (auth.currentUser?.displayName == null) {
                tostear("Para añadir un postUpdated, tienes que configurar tu nombre de usuario")
                requestToHomeActivity.callUpdateProfile()
            } else {
                requestToHomeActivity.callAddPostForm()
            }
        }

        //Configure Recycler View
        recyclerView = view!!.findViewById(R.id.apiwrapper_recyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        postAdapter = PostAdapter(context!!) {
            requestToHomeActivity.callUpdatePostForm(it)
        }

        recyclerView.adapter = postAdapter
    }

    fun setDatabaseListeners() {
        dataBasePostsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                tostear("something went wrong on getting posts!")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    posts.clear()
                    for (p in p0.children){
                        val post = p.getValue(Post::class.java)
                        posts.add(post!!)
                    }
                    postAdapter.addPosts(posts)
                }
            }

        })
    }

    fun onPostCreate(post: Post) {
        val key = dataBasePostsRef.push().key

        if (key == null) {
            tostear("algo salio mal con la key para el postUpdated")
            return
        }
        post.uid = userId
        post.id = key
        post.author = auth.currentUser!!.displayName.toString()
        val postValues = post.toMap()

        val childUpdates = HashMap<String, Any?>()
        childUpdates["$key"] = postValues

        dataBasePostsRef.updateChildren(childUpdates)
            .addOnSuccessListener {
                //Evento cuando se posteo exitosamente
            }.addOnFailureListener {
                tostear("Algo salio mal al enviar el postUpdated")
            }

    }

    fun onPostUpdate(post: Post){
        dataBasePostsRef.child("${post.id}").setValue(post)
    }

    fun tostear(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


// Declaraciones necesarias para conectar este fragment con su activity

    lateinit var requestToHomeActivity: ApiWrapperInterface

    fun setOnCalledPostFormListener(callback: ApiWrapperInterface) {
        this.requestToHomeActivity = callback
    }

    interface ApiWrapperInterface {
        fun callAddPostForm()
        fun callUpdatePostForm(post: Post)
        fun callUpdateProfile()
    }
}


