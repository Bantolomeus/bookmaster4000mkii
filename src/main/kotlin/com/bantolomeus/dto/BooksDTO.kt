package com.bantolomeus.dto

import java.util.*

data class BookDTO(
        val name: String,
        val author: String,
        val pagesTotal: Int,
        val pagesRead: Int
)

data class BookUpdateDTO(
        val name: String,
        val pagesRead: Int
)

data class BookDataDTO(
        val name: String,
        val author: String,
        val pagesTotal: Int,
        val pagesCurrent: Int,
        val updates: List<Map<Date, Int>>
)