package com.bantolomeus.repository

import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val BOOK_FILE = "book.json"
const val BOOK_UPDATES_FILE = "booksUpdates.json"

@Repository
class BookRepository {
    private val objectMapper = ObjectMapper()

    fun saveBook(books: BooksFileDTO?) {
        objectMapper.writeValue(File(BOOK_FILE), books)
    }

    fun saveBookUpdate(bookUpdate: BooksUpdatesFileDTO): BooksUpdatesFileDTO {
        objectMapper.writeValue(File(BOOK_UPDATES_FILE), bookUpdate)
        return BooksUpdatesFileDTO(
                bookUpdate
                        .booksUpdate
                        .asSequence()
                        .sortedBy { it.date }.toMutableList())
    }

    fun getBooks(): BooksFileDTO {
        return try {
            objectMapper.readValue(File(BOOK_FILE), BooksFileDTO::class.java)
        } catch (e: Exception) {
            BooksFileDTO()
        }
    }

    fun getBooksUpdates(): BooksUpdatesFileDTO {
        return try {
            BooksUpdatesFileDTO(
                    objectMapper
                            .readValue(File(BOOK_UPDATES_FILE), BooksUpdatesFileDTO::class.java)
                            .booksUpdate
                            .asSequence()
                            .sortedBy { it.date }.toMutableList())
        } catch (e: Exception) {
            BooksUpdatesFileDTO()
        }
    }

    fun sortBookUpdates(): BooksUpdatesFileDTO {
        return try {
            val bookUpdates = BooksUpdatesFileDTO(objectMapper.readValue(File(BOOK_UPDATES_FILE), BooksUpdatesFileDTO::class.java)
                    .booksUpdate
                    .asSequence()
                    .sortedBy { it.date }.toMutableList())
            objectMapper.writeValue(File(BOOK_UPDATES_FILE), bookUpdates)
            bookUpdates
        } catch (exception: Exception) {
            BooksUpdatesFileDTO()
        }
    }
}
