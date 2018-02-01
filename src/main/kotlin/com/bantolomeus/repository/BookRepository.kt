package com.bantolomeus.repository

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class BookRepository(private val objectMapper: ObjectMapper) {

    fun saveBook(book: BookDTO) {
        objectMapper.writeValue(File("book.json"), book)
    }

    fun saveBookUpdate(bookUpdate: BookUpdateDTO) {
        objectMapper.writeValue(File("bookUpates.json"), bookUpdate)
    }
}