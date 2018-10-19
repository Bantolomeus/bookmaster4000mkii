package com.bantolomeus.repository

import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val BOOK_UPDATES_FILE = "bookUpdates.json"

@Repository
class BookUpdatesRepository(private val bookUpdatesFile: String = BOOK_UPDATES_FILE) {
    private val objectMapper = ObjectMapper()

    fun saveBookUpdate(bookUpdate: BooksUpdatesFileDTO): BooksUpdatesFileDTO {
        objectMapper.writeValue(File(bookUpdatesFile), bookUpdate)
        return BooksUpdatesFileDTO(
                bookUpdate
                        .booksUpdate
                        .asSequence()
                        .sortedBy { it.date }.toMutableList())
    }

    fun getBooksUpdates(): BooksUpdatesFileDTO {
        return try {
            BooksUpdatesFileDTO(
                    objectMapper
                            .readValue(File(bookUpdatesFile), BooksUpdatesFileDTO::class.java)
                            .booksUpdate
                            .asSequence()
                            .sortedBy { it.date }.toMutableList())
        } catch (e: Exception) {
            BooksUpdatesFileDTO()
        }
    }

    fun sortBookUpdates(): BooksUpdatesFileDTO {
        return try {
            val bookUpdates = BooksUpdatesFileDTO(objectMapper.readValue(File(bookUpdatesFile), BooksUpdatesFileDTO::class.java)
                    .booksUpdate
                    .asSequence()
                    .sortedBy { it.date }.toMutableList())
            objectMapper.writeValue(File(bookUpdatesFile), bookUpdates)
            bookUpdates
        } catch (exception: Exception) {
            BooksUpdatesFileDTO()
        }
    }
}
