package com.bantolomeus.dto

data class ChallengeDTO(
        val pagesPerDay: Long = 0,
        var pagesSurplus: Long = 0,
        val startPagesSurplus: Long = 0,
        var pagesSinceStart: Long = 0,
        var pagesEverRead: Long = 0,
        val dateStarted: String? = ""
)