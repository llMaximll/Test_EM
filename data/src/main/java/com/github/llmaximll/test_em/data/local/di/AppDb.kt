package com.github.llmaximll.test_em.data.local.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.llmaximll.test_em.data.BuildConfig
import com.github.llmaximll.test_em.data.local.daos.UserDao
import com.github.llmaximll.test_em.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = true
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val DB_NAME = "test_em.db"
    }
}