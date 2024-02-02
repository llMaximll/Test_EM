package com.github.llmaximll.test_em.data.local

import androidx.room.TypeConverter
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class CommonTypeConverter {

    @TypeConverter
    fun listStringToJson(value: List<String>): String {
        val gson = GsonBuilder().create()
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToListString(value: String): List<String> {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<List<String>>() {}.type

        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun priceEntityToJson(value: ItemEntity.PriceEntity?): String {
        val gson = GsonBuilder().create()
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToPriceEntity(value: String): ItemEntity.PriceEntity? {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<ItemEntity.PriceEntity?>() {}.type

        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun feedbackEntityToJson(value: ItemEntity.FeedbackEntity?): String {
        val gson = GsonBuilder().create()
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToFeedbackEntity(value: String): ItemEntity.FeedbackEntity? {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<ItemEntity.FeedbackEntity?>() {}.type

        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun listInfoEntityToJson(value: List<ItemEntity.InfoEntity>): String {
        val gson = GsonBuilder().create()
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToListInfoEntity(value: String): List<ItemEntity.InfoEntity> {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<List<ItemEntity.InfoEntity>>() {}.type

        return gson.fromJson(value, type)
    }
}