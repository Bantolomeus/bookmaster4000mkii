package com.bantolomeus.repository

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Repository
import java.io.File
import java.util.*

const val BOOK_FILE = "books.json"

@Repository
class BookRepository(private val bookFile: String = BOOK_FILE) {
    private val objectMapper = jacksonObjectMapper()

    fun saveBooks(books: List<BookDTO>): List<BookDTO> {
        objectMapper.writeValue(File(bookFile), books)
        return books
    }

    fun saveBookIfItNotExists(newBook: BookDTO): BookDTO {
        val existingBook = getBookByName(newBook.name)
        if (existingBook == null) {
            val books = getBooks()
            newBook.dateStarted = dateFormat.format(Date())
            objectMapper.writeValue(File(bookFile), books + newBook)
            return newBook
        }

        return existingBook
    }

    fun getBooks(): List<BookDTO> {
        return try {
            objectMapper.readValue(File(bookFile))
        } catch (exception: Exception) {
            println("ERROR[getBooks]: The books file does not exists. \n Stacktrace: \n $exception")
            emptyList()
        }
    }

    fun getBookByName(bookName: String): BookDTO? {
        return try {
            objectMapper.readValue<List<BookDTO>>(File(bookFile))
                    .firstOrNull { it.name == bookName }
        } catch (exception: Exception) {
            println("ERROR[getBookByName]: The books file does not exists. \n Stacktrace: \n $exception")
            null
        }
    }

    fun getAllBooksExcept(bookName: String): List<BookDTO> {
        return try {
            objectMapper.readValue<List<BookDTO>>(File(bookFile)).toMutableList().filter { it.name != bookName }
        } catch (exception: Exception) {
            println("ERROR[getBooks]: The books file does not exists. \n Stacktrace: \n $exception")
            emptyList()
        }
    }
}
