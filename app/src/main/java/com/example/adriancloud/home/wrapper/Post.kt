package com.example.adriancloud.home.wrapper

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Post (
    var title: String = "",
    var body: String = ""
) {


    var uid: String? = ""
    var author: String? = ""


    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "body" to body,
            "author" to author
        )
    }
}