package com.bantolomeus.repository

import com.bantolomeus.dto.BookUpdatesFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val BOOK_UPDATES_FILE = "bookUpdates.json"

@Repository
class BookUpdatesRepository(private val bookUpdatesFile: String = BOOK_UPDATES_FILE) {
    private val objectMapper = ObjectMapper()

    fun saveBookUpdate(bookUpdate: BookUpdatesFileDTO): BookUpdatesFileDTO {
        objectMapper.writeValue(File(bookUpdatesFile), bookUpdate)
        return BookUpdatesFileDTO(
                bookUpdate
                        .booksUpdate
                        .asSequence()
                        .sortedBy { it.date }.toMutableList())
    }

    fun getBooksUpdates(): BookUpdatesFileDTO {
        return try {
            BookUpdatesFileDTO(
                    objectMapper
                            .readValue(File(bookUpdatesFile), BookUpdatesFileDTO::class.java)
                            .booksUpdate
                            .asSequence()
                            .sortedBy { it.date }.toMutableList())
        } catch (e: Exception) {
            BookUpdatesFileDTO()
        }
    }

    fun sortBookUpdates(): BookUpdatesFileDTO {
        return try {
            val bookUpdates = BookUpdatesFileDTO(objectMapper.readValue(File(bookUpdatesFile), BookUpdatesFileDTO::class.java)
                    .booksUpdate
                    .asSequence()
                    .sortedBy { it.date }.toMutableList())
            objectMapper.writeValue(File(bookUpdatesFile), bookUpdates)
            bookUpdates
        } catch (exception: Exception) {
            BookUpdatesFileDTO()
        }
    }
}
