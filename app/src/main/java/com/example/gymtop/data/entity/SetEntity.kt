package com.example.gymtop.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.gymtop.domain.model.SetTypeValue

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: Long,
    val setNumber: Int,      // Order: 1, 2, 3, 4...
    val type: SetTypeValue,         // "REPS" or "DURATION"
    val reps: Int?,
    val duration: Long?,
    val weight: Int?
)
