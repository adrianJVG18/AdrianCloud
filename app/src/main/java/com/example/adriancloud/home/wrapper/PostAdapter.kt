package com.example.adriancloud.home.wrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import com.example.adriancloud.home.wrapper.model.Post
import kotlinx.android.synthetic.main.wrapper_post_item.view.*


class PostAdapter constructor(val context: Context, val clickListener: (Post)->Unit)
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts: MutableList<Post> = ArrayList()

    fun addPosts(posts: MutableList<Post>){
        this.posts = posts
        notifyDataSetChanged()
    }
    fun addPost(post: Post){
        this.posts.add(post)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.wrapper_post_item, parent, false)
        return PostViewHolder(v)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], clickListener)
    }


    inner class PostViewHolder(var mView: View) : RecyclerView.ViewHolder(mView){

        fun bind(post: Post, clickListener: (Post) -> Unit) {
            mView.wrapper_post_title.text = post.title
            mView.wrapper_post_body.text = post.body
            mView.setOnClickListener{ clickListener(post)}
        }
    }


}