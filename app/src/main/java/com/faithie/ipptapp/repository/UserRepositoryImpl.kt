package com.faithie.ipptapp.repository

import com.faithie.ipptapp.model.User
import kotlinx.coroutines.delay

class UserRepositoryImpl : UserRepository {

    private val users = mutableMapOf<String, User>()

    override suspend fun getUserById(userId: String): User? {
        delay(1000) // Simulate network/database delay
        return users[userId]
    }

    override suspend fun signIn(email: String, password: String): User? {
        delay(1000)
        return users.values.find { it.email == email && it.password == password }
    }

    override suspend fun signUp(username: String, email: String, password: String): User? {
        delay(1000)
        val user = User(id = generateUserId(), username = username, email = email, password = password)
        users[user.id] = user
        return user
    }

    override suspend fun updateUser(user: User): Boolean {
        delay(1000)
        users[user.id] = user
        return true
    }

    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}"
    }
}
