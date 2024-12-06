package com.kotlin.campusconnect.models

import java.io.Serializable

data class Poster(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val userId: String?,
    val userName: String?,
    val createdAt: Long
) : Serializable