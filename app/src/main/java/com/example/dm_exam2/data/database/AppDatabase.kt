package com.example.dm_exam2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteShow::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    companion object {
        private const val DATABASE_NAME = "favoritesshows_databases"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .createFromAsset("databases/favoritesshows.db")
                    .build().also { instance = it }
            }
        }
    }
}