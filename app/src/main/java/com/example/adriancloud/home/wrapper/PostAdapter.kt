package com.example.adriancloud.home.wrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adriancloud.R
import kotlinx.android.synthetic.main.wrapper_post_item.view.*

class PostAdapter(val posts: ArrayList<Post>, val context: Context) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wrapper_post_item, parent, false)
        return PostViewHolder(v)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bindPost(post)
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bindPost(post : Post) = with(itemView){
            wrapper_post_title.text = post.title
            wrapper_post_body.text = post.body
        }
    }
}