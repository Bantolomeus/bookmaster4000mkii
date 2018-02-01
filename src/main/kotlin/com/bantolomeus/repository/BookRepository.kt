package com.bantolomeus.repository

import com.bantolomeus.dto.BookFileDTO
import com.bantolomeus.dto.BookUpdateDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class BookRepository(private val objectMapper: ObjectMapper) {

    fun saveBook(books: BookFileDTO?) {
        objectMapper.writeValue(File("books.json"), books)
    }

    fun saveBookUpdate(bookUpdate: BookUpdateDTO) {
        objectMapper.writeValue(File("booksUpates.json"), bookUpdate)
    }

    fun getBooks(): BookFileDTO? {
        try {
            return objectMapper.readValue(File("books.json"), BookFileDTO::class.java)
        } catch (e: Exception) {
            return BookFileDTO()
        }
    }
}