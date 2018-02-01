package com.bantolomeus.dto

data class BookDTO(
        val name: String = "",
        val author: String = "",
        val pagesTotal: Long = 0,
        val pagesRead: Long? = null,
        val dateStarted: String? = null,
        val readTime: Long? = null
)

data class BookUpdateDTO(
        val name: String = "",
        val pagesRead: Long = 0,
        val date: String? = ""
)

data class BookFileDTO(
        val books: MutableList<BookDTO> = emptyList<BookDTO>().toMutableList()
)

data class BookDataDTO(
        val bookDTO: BookDTO,
        val updates: List<Map<String, Long>>
)