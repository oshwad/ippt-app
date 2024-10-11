package com.faithie.ipptapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.faithie.ipptapp.data.User
import com.faithie.ipptapp.data.UserDatabase

class UserViewModel(application: Application) :
    AndroidViewModel(application = application)
{
    private val userDao = UserDatabase.getDatabase(application).userDao()

    // Function to fetch user data
    suspend fun getUser(): User? {
    return userDao.getUser()
}

    // Function to insert user data
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}