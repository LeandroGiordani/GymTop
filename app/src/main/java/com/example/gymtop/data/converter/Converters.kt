package com.example.gymtop.data.converter

import androidx.room.TypeConverter
import com.example.gymtop.domain.model.SetTypeValue

class Converters {
    @TypeConverter
    fun fromSetTypeValue(value: SetTypeValue): String = value.name

    @TypeConverter
    fun toSetTypeValue(value: String): SetTypeValue = enumValueOf(value)
}
