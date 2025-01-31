package com.android.myapplication.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val email: String,
    val nickname: String,
    val profileImage: String
)