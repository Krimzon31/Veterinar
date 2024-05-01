package com.example.pet_pawtrol.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veterinars")

data class Veterinars(
    @PrimaryKey(autoGenerate = true)
    val ID: Int? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phNumber")
    val phNumber: String,

    @ColumnInfo(name = "comment")
    val comment: String,

    @ColumnInfo(name = "specialization")
    val specialization: String,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "urlProfile")
    val urlProfile: String
    )
