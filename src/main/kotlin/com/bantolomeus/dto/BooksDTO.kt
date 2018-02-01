package com.bantolomeus.dto

import java.util.*

data class BookDTO(
        val name: String,
        val author: String,
        val pagesTotal: Int,
        val pagesRead: Int?,
        val dateStarted: Date?,
        val readTime: Int?
)

data class BookUpdateDTO(
        val name: String,
        val pagesRead: Int,
        val date: Date?
)

data class BookDataDTO(
        val bookDTO: BookDTO,
        val updates: List<Map<Date, Int>>
)