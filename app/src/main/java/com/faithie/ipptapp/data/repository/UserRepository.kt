package com.faithie.ipptapp.data.repository

import com.faithie.ipptapp.data.model.User

class UserRepository {
    fun getUserProfile(): User {
        // Dummy data for testing
        return User("John Doe", "john@example.com")
    }
}
