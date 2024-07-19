package ru.zakablukov.yourmoviebase.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "person", indices = [Index(value = ["externalId"], unique = true)])
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val personId: Int = 0,
    val externalId: Int,
    val photoUrl: String,
    val name: String?,
    val enName: String?,
    val profession: String,
    val enProfession: String
)
