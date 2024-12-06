package com.kotlin.campusconnect.models

import java.io.Serializable

data class Article(
    var id: Int? = 0,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
) : Serializable {
    // Empty constructor required for Firestore
    constructor() : this(null, null, null, null, null, null, null, null, null)
}