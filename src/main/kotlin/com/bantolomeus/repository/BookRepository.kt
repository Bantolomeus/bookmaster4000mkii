package com.bantolomeus.repository

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Repository
import java.io.File
import java.util.*

const val BOOK_FILE = "books.json"

// TODO: Save books also as array. Can no longer use objectMapper, maybe stream instead.
// TODO: Create file if it does not exist? Would be great. Extra ticket?!
@Repository
class BookRepository(private val bookFile: String = BOOK_FILE) {
    private val objectMapper = jacksonObjectMapper()

    fun saveBooks(books: List<BookDTO>): List<BookDTO> {
        objectMapper.writeValue(File(bookFile), books)
        return books
    }

    fun saveBookIfItNotExists(bookDTO: BookDTO): BookDTO {
        val book = getBookByName(bookDTO.name)
        if (book == null) {
            val books = getBooks()
            bookDTO.dateStarted = dateFormat.format(Date())
            books.add(bookDTO)
            objectMapper.writeValue(File(bookFile), books)
            return bookDTO
        }

        return book
    }

    fun getBooks(): MutableList<BookDTO> {
        return try {
            objectMapper.readValue<List<BookDTO>>(File(bookFile)).toMutableList()
        } catch (exception: Exception) {
            println("ERROR[getBooks]: The books file does not exists. \n Stacktrace: \n $exception")
            emptyList<BookDTO>().toMutableList()
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
}
