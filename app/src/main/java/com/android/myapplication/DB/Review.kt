package com.android.myapplication.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "review",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val reviewId: Int,
    val userId: Int,
    val isbn: String,  // bookId 대신 isbn 저장
    val starRate: Float,
    val review: String,
    val favoriteLine: String,
    val createdAt: String,
    val like: Int
)