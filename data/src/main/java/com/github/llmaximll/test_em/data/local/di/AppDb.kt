package com.github.llmaximll.test_em.data.local.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.llmaximll.test_em.data.BuildConfig
import com.github.llmaximll.test_em.data.local.CommonTypeConverter
import com.github.llmaximll.test_em.data.local.daos.ItemDao
import com.github.llmaximll.test_em.data.local.daos.UserDao
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import com.github.llmaximll.test_em.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ItemEntity::class
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = true
)
@TypeConverters(CommonTypeConverter::class)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun itemDao(): ItemDao

    companion object {
        const val DB_NAME = "test_em.db"
    }
}