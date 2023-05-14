package com.filozilla.models

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "payment")
@SuppressLint("KotlinNullnessAnnotation")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NotNull
    var id: Int,
    @ColumnInfo(name = "i_id")
    @NotNull
    var iId: Int,
    @ColumnInfo(name = "fullname")
    @NotNull
    var fullName: String,
    @ColumnInfo(name = "number")
    @NotNull
    var number: String,
    @ColumnInfo(name = "expirydate")
    @NotNull
    var expiryDate: String,
    @ColumnInfo("securitycode")
    @NotNull
    var securityCode: Int
)