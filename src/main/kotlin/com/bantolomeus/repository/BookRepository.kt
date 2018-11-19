package com.bantolomeus.repository

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BooksFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File
import java.util.*

const val BOOK_FILE = "books.json"

@Repository
class BookRepository(private val bookFile: String = BOOK_FILE) {
    private val objectMapper = ObjectMapper()

    fun saveBooks(books: BooksFileDTO): BooksFileDTO {
        objectMapper.writeValue(File(bookFile), books)
        return books
    }

    fun saveBookIfItNotExists(bookDTO: BookDTO): BookDTO {
        return if (getBookByName(bookDTO.name).name != bookDTO.name) {
            val books = getBooks()
            bookDTO.dateStarted = dateFormat.format(Date())
            books.add(bookDTO)
            objectMapper.writeValue(File(bookFile), books)
            return bookDTO
        } else BookDTO()
    }

    fun getBooks(): MutableList<BookDTO> {
        return try {
            objectMapper.readValue(File(bookFile), BooksFileDTO::class.java).books
        } catch (exception: Exception) {
            println("ERROR: The books file does not exists. /n Stacktrace: $exception")
            emptyList<BookDTO>().toMutableList()
        }
    }

    fun getBookByName(bookName: String): BookDTO {
        return try {
            objectMapper.readValue(File(bookFile), BooksFileDTO::class.java)
                    .books.first { it.name == bookName }
        } catch (e: Exception) {
            println("ERROR: The books file does not exists")
            BookDTO()
        }
    }
}
