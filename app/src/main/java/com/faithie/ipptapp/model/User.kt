package com.faithie.ipptapp.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String? = null, // This should be handled securely in a real app
    val profileImageUrl: String? = null // Optional profile image
)