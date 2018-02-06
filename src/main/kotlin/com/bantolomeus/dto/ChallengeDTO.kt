package com.bantolomeus.dto

data class ChallengeDTO(
        val pagesPerDay: Long = 0,
        val pagesPlus: Long = 0,
        val startPagesPlus: Long = 0,
        val pagesEverRead: Long = 0,
        val dateStarted: String? = ""
)