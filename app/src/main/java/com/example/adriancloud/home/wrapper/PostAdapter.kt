package com.example.adriancloud.home.wrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import kotlinx.android.synthetic.main.wrapper_post_item.view.*
import kotlinx.android.synthetic.main.wrapper_postit_item.view.*


class PostAdapter constructor(val context: Context, val clickListener: (Post)->Unit)
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts: MutableList<Post> = ArrayList()

    fun addPosts(posts: MutableList<Post>){
        this.posts = posts
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.wrapper_postit_item, parent, false)
        return PostViewHolder(v)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], clickListener)
    }


    inner class PostViewHolder(var mView: View) : RecyclerView.ViewHolder(mView){

        fun bind(post: Post, clickListener: (Post) -> Unit) {
            mView.post_it_title.text = post.title
            mView.post_it_body.text = post.body
            mView.setOnClickListener{ clickListener(post)}
        }
    }


}