package com.bantolomeus.dto

import com.bantolomeus.date.dateFormat
import java.util.*

data class BookDTO(
        val name: String = "",
        val author: String = "",
        val pagesTotal: Long = 0,
        var currentPage: Long = 0,
        var dateStarted: String = "",
        var readTime: Long = 0
)

data class BookUpdateInputDTO(
        val currentPage: Long = 0,
        val date: String = dateFormat.format(Date())
)

data class BookUpdateOutputDTO(
        val name: String = "",
        val pagesRead: Long = 0,
        val date: String = ""
)

data class BookUpdatesFileDTO(
        val bookUpdates: List<BookUpdateOutputDTO> = emptyList()
)

data class BookGetDTO(
        val book: BookDTO = BookDTO(),
        val updates: List<ProgressUpdateDTO> = listOf(ProgressUpdateDTO())
)
