package com.github.llmaximll.test_em.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.llmaximll.test_em.core.common.models.User

@Entity("user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long? = null,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("last_name") val lastName: String,
    @ColumnInfo("phone_number") val phoneNumber: String
)

fun UserEntity.asModel(): User =
    User(
        localId = this.id,
        name = this.name,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber
    )

fun User.asEntity() =
    UserEntity(
        id = this.localId,
        name = this.name,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber
    )