package com.bantolomeus.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BooksFileDTO
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Repository
import java.io.File
import java.util.*

const val BOOK_FILE = "books.json"

// TODO: Save books also as array. Can no§ longer use objectMapper, maybe stream instead.
// TODO: create file if it does not exist? Would be great.
@Repository
class BookRepository(private val bookFile: String = BOOK_FILE) {
    private val objectMapper = jacksonObjectMapper()

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
            println("ERROR[getBooks]: The books file does not exists. \n Stacktrace: \n $exception")
            emptyList<BookDTO>().toMutableList()
        }
    }

    // TODO: return null in error case
    fun getBookByName(bookName: String): BookDTO {
        return try {
            objectMapper.readValue<List<BookDTO>>(File(bookFile))
                    .first { it.name == bookName }
        } catch (exception: Exception) {
            println("ERROR[getBookByName]: The books file does not exists. \n Stacktrace: \n $exception")
            BookDTO()
        }
    }
}
