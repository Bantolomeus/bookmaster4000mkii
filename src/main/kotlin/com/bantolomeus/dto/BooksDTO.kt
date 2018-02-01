package com.bantolomeus.dto

data class BookDTO(
        val name: String = "",
        val author: String = "",
        val pagesTotal: Long = 0,
        val pagesRead: Long? = 0,
        val dateStarted: String? = "",
        val readTime: Long? = 0
)

data class BookUpdateDTO(
        val name: String = "",
        val pagesRead: Long = 0,
        val date: String? = ""
)

data class BookDataDTO(
        val bookDTO: BookDTO,
        val updates: List<Map<String, Long>>
)