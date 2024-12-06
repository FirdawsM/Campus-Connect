package com.kotlin.campusconnect.models

data class Source(
    val id: String? = null,
    val name: String? = null
) {
    // Empty constructor required for Firestore
    constructor() : this(null, null)
}