package com.faithie.ipptapp.domain

import com.faithie.ipptapp.data.repository.UserRepository
import com.faithie.ipptapp.data.model.User

class ProfileUseCase(private val userRepository: UserRepository) {
    fun getUserProfile(): User {
        return userRepository.getUserProfile()
    }
}
