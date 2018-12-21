package com.bantolomeus.repository

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.dto.BookUpdatesFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Repository
import java.io.File
import java.util.*

const val BOOK_UPDATES_FILE = "bookUpdates.json"

@Repository
class BookUpdatesRepository(private val bookUpdatesFile: String = BOOK_UPDATES_FILE) {
    private val objectMapper = ObjectMapper()

    init {
        if (!File(bookUpdatesFile).exists()) {
            objectMapper.writeValue(File(bookUpdatesFile), BookUpdatesFileDTO())
        }
    }

    fun saveBookUpdate(bookUpdate: BookUpdatesFileDTO): BookUpdatesFileDTO {
        objectMapper.writeValue(File(bookUpdatesFile), bookUpdate)
        return bookUpdate
    }

    fun getBookUpdates(): BookUpdatesFileDTO? {
        return try {
            objectMapper.readValue(File(bookUpdatesFile), BookUpdatesFileDTO::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun sortBookUpdates(): BookUpdatesFileDTO {
        return try {
            val bookUpdates = BookUpdatesFileDTO(objectMapper.readValue(File(bookUpdatesFile), BookUpdatesFileDTO::class.java)
                    .bookUpdates
                    .asSequence()
                    .sortedByDescending { it.date }.toMutableList())
            objectMapper.writeValue(File(bookUpdatesFile), bookUpdates)
            bookUpdates
        } catch (exception: Exception) {
            BookUpdatesFileDTO()
        }
    }

    fun getUpdateFromToday(bookName: String): BookUpdateOutputDTO? {
        return getBookUpdates()?.bookUpdates?.firstOrNull { it.date == dateFormat.format(Date()) && it.name == bookName }
    }
}
