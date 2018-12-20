package com.bantolomeus.service

import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.*
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.bantolomeus.translator.toBookUpdateDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository,
                  private val bookUpdatesRepository: BookUpdatesRepository,
                  private val progressService: ProgressService) {

    fun createBook(bookDTO: BookDTO): BookDTO {
        val savedBookDTO = bookRepository.saveBookIfItNotExists(bookDTO)
        if (savedBookDTO.currentPage > 0) {
            saveBookUpdate(savedBookDTO)
        }
        return bookDTO
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO, bookName: String): BookUpdatesFileDTO {
        val book = bookRepository.getBookByName(bookName) ?: return BookUpdatesFileDTO()
        if (book.currentPage == book.pagesTotal) {
            book.readTime = ((Date().time.minus(dateFormat.parse(book.dateStarted).time)) / DIVISOR_FOR_DAY)
        }

        val oldPage = book.currentPage.also { book.currentPage = bookUpdate.currentPage}
        val pagesRead = bookUpdate.currentPage.minus(oldPage)
        if (pagesRead <= 0 || bookUpdate.currentPage > book.pagesTotal) {
            return BookUpdatesFileDTO()
        }
        bookRepository.updateBook(book)

        val currentDate = dateFormat.format(Date())
        val updateFromToday = bookUpdatesRepository.getUpdateFromToday(book.name)
        return if (updateFromToday == null) {
            saveBookUpdate(bookName, pagesRead, bookUpdate.date ?: currentDate, BookUpdateOutputDTO(), bookUpdate, oldPage)
        } else {
            saveBookUpdate(bookName, pagesRead, currentDate, updateFromToday, bookUpdate, oldPage)
        }
    }

    fun getBookWithUpdates(bookName: String): BookGetDTO {
        val book = bookRepository.getBookByName(bookName)
        val bookUpdates = bookUpdatesRepository.getBookUpdates()?.bookUpdates
                ?.filter { it.name == bookName }
                ?.map { ProgressUpdateDTO(it.date, it.pagesRead)}
        return BookGetDTO(book ?: BookDTO(), bookUpdates ?: emptyList())
    }

    fun getAllBooks(): List<BookDTO> {
        return bookRepository.getBooks()
    }

    fun sortBookUpdates(): BookUpdatesFileDTO {
        return bookUpdatesRepository.sortBookUpdates()
    }

    private fun saveBookUpdate(bookDTO: BookDTO) {
        val bookUpdates = bookUpdatesRepository.getBookUpdates()
        bookUpdates?.bookUpdates?.add(0, bookDTO.toBookUpdateDTO())
        bookUpdatesRepository.saveBookUpdate(bookUpdates ?: BookUpdatesFileDTO(mutableListOf(bookDTO.toBookUpdateDTO())))
        progressService.saveProgress(bookDTO.currentPage)
    }

    private fun saveBookUpdate(bookName: String, pagesRead: Long, date: String, bookUpdateFromToday: BookUpdateOutputDTO?,
                               bookUpdate: BookUpdateInputDTO, oldPage: Long): BookUpdatesFileDTO {
        val oldBookUpdates = bookUpdatesRepository.getBookUpdates()?.bookUpdates?.filter {
            it.date != bookUpdateFromToday?.date || it.name != bookName
        }?.toMutableList()
        oldBookUpdates?.add(0, BookUpdateOutputDTO(
                name = bookName,
                pagesRead = bookUpdate.currentPage.minus(oldPage).plus(bookUpdateFromToday!!.pagesRead),
                date = date)
        )
        progressService.saveProgress(pagesRead)
        return bookUpdatesRepository.saveBookUpdate(BookUpdatesFileDTO(oldBookUpdates ?: mutableListOf()))
    }
}
