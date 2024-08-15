package com.faithie.ipptapp.repository

import com.faithie.ipptapp.model.User

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(username: String, email: String, password: String): User?
    suspend fun updateUser(user: User): Boolean
}