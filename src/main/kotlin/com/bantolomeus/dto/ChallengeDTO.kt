package com.bantolomeus.dto

data class ChallengeDTO(
        val pagesPerDay: Long = 0,
        val pagesSurplus: Long = 0,
        val startPagesPlus: Long = 0,
        var pagesEverRead: Long = 0,
        val dateStarted: String? = ""
)