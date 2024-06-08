package com.example.dm_exam2.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dm_exam2.model.Show

@Entity(tableName = "favoritesshows")
data class FavoriteShow(
    @PrimaryKey val id: Int = 0,
    val name: String? = null,
    val image: String? = null,
    val rate: String? = null,
    val genres: String? = null,
)
