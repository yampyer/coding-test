package com.jeanpigomez.codingtest.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Language(
        @field:PrimaryKey
        val name: String,
        val score: Int,
        val endDate: String
)
