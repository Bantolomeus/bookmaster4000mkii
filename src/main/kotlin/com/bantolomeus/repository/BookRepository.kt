package com.bantolomeus.repository

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BooksFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val BOOK_FILE = "books.json"

@Repository
class BookRepository(private val bookFile: String = BOOK_FILE) {
    private val objectMapper = ObjectMapper()

    fun saveBook(books: BooksFileDTO?) {
        objectMapper.writeValue(File(bookFile), books)
    }

    fun getBooks(): BooksFileDTO {
        return try {
            objectMapper.readValue(File(bookFile), BooksFileDTO::class.java)
        } catch (e: Exception) {
            BooksFileDTO()
        }
    }

    fun getBookByName(bookName: String): BookDTO {
        return try {
            objectMapper.readValue(File(bookFile), BooksFileDTO::class.java)
                    .books
                    .asSequence()
                    .filter { it.name == bookName }
                    .first()
        } catch (e: Exception) {
            BookDTO()
        }
    }
}
