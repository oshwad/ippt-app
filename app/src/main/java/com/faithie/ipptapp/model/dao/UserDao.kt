package com.faithie.ipptapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faithie.ipptapp.model.entity.User

@Dao
interface UserDao {
    @Query("DELETE FROM user_info")
    suspend fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_info LIMIT 1")
    suspend fun getUser(): User?
}
