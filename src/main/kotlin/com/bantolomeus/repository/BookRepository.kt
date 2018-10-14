package com.bantolomeus.repository

import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class BookRepository(private val objectMapper: ObjectMapper) {

    fun saveBook(books: BooksFileDTO?) {
        objectMapper.writeValue(File("books.json"), books)
    }

    fun saveBookUpdate(bookUpdate: BooksUpdatesFileDTO): BooksUpdatesFileDTO {
        objectMapper.writeValue(File("booksUpdates.json"), bookUpdate)
        return BooksUpdatesFileDTO(bookUpdate.booksUpdate.asSequence().sortedBy { it.date }.toMutableList())
    }

    fun getBooks(): BooksFileDTO {
        return try {
            objectMapper.readValue(File("books.json"), BooksFileDTO::class.java)
        } catch (e: Exception) {
            BooksFileDTO()
        }
    }

    fun getBooksUpdates(): BooksUpdatesFileDTO {
        return try {
            BooksUpdatesFileDTO(
                    objectMapper
                    .readValue(File("booksUpdates.json"), BooksUpdatesFileDTO::class.java)
                    .booksUpdate
                    .asSequence()
                    .sortedBy { it.date }
                    .toMutableList())
        } catch (e: Exception) {
            BooksUpdatesFileDTO()
        }
    }
}
