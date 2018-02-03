package com.bantolomeus.dto

data class BookDTO(
        val name: String = "",
        val author: String = "",
        val pagesTotal: Long = 0,
        var currentPage: Long? = null,
        var dateStarted: String? = null,
        var readTime: Long? = null
)

data class BooksFileDTO(
        val books: MutableList<BookDTO> = emptyList<BookDTO>().toMutableList()
)

data class BookUpdateInputDTO(
        val name: String = "",
        val currentPage: Long? = null,
        val date: String? = ""
)

data class BookUpdateOutputDTO(
        val name: String = "",
        val pagesRead: Long? = null,
        val date: String? = ""
)

data class BooksUpdatesFileDTO(
        val booksUpdate: MutableList<BookUpdateOutputDTO> = emptyList<BookUpdateOutputDTO>().toMutableList()
)

data class BookGetDTO(
        val bookDTO: BookDTO? = null,
        val updates: List<Map<String?, Long?>>? = null
)