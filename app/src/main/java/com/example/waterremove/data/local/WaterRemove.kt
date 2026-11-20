package com.example.waterremove.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_remove")
data class WaterRemove(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
)