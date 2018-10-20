package com.bantolomeus.dto

data class BookDTO(
        val name: String = "",
        val author: String = "",
        val pagesTotal: Long = 0,
        var currentPage: Long = 0,
        var dateStarted: String = "",
        var readTime: Long = 0
)

data class BooksFileDTO(
        val books: MutableList<BookDTO> = emptyList<BookDTO>().toMutableList()
)

data class BookUpdateInputDTO(
        val currentPage: Long = 0
)

data class BookUpdateOutputDTO(
        val name: String = "",
        val pagesRead: Long = 0,
        val date: String = ""
)

data class BooksUpdatesFileDTO(
        val booksUpdate: MutableList<BookUpdateOutputDTO> = emptyList<BookUpdateOutputDTO>().toMutableList()
)

data class BookGetDTO(
        val book: BookDTO = BookDTO(),
        val updates: List<Map<String, Long>>
)
