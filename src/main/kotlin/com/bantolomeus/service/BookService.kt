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
        var otherBooks = bookRepository.getBooks().filter { it.name != bookName }
        var oldPage = Long.MAX_VALUE
        val foundBook = bookRepository.getBookByName(bookName)

        if (foundBook?.name == bookName) {
            foundBook.let {
                oldPage = it.currentPage
                it.currentPage = bookUpdate.currentPage
                if (it.currentPage == it.pagesTotal) {
                    it.readTime = ((Date().time - dateFormat.parse(it.dateStarted).time) / DIVISOR_FOR_DAY) + 1
                }
            }
            otherBooks = mutableListOf(foundBook) + otherBooks
        }

        if (oldPage < bookUpdate.currentPage && foundBook != null
                && bookUpdate.currentPage <= foundBook.pagesTotal) {
            bookRepository.saveBooks(otherBooks)
            val bookUpdates = bookUpdatesRepository.getBookUpdates()
            val currentDate = dateFormat.format(Date())
            val bookUpdateFromToday = bookUpdates?.bookUpdates?.filter { it.date == currentDate && it.name == bookName }
                    ?.getOrElse(0) { _ -> BookUpdateOutputDTO()}
            val pagesRead = bookUpdate.currentPage.minus(oldPage)

            if (pagesRead > 0 && bookUpdateFromToday == null) {
                return saveBookUpdate(bookUpdate, bookName, pagesRead, currentDate)
            } else if (pagesRead > 0 && bookUpdateFromToday != null) {
                return editUpdateFromToday(bookUpdates, bookName, pagesRead, currentDate, bookUpdateFromToday, bookUpdate, oldPage)
            }
        }
        return BookUpdatesFileDTO()
    }

    fun getBookWithUpdates(bookName: String): BookGetDTO {
        val book = bookRepository.getBookByName(bookName)
        val bookUpdates = bookUpdatesRepository.getBookUpdates()?.bookUpdates
                ?.filter { it.name == bookName }
                ?.map { ProgressUpdateDTO(it.date, it.pagesRead)}
        return BookGetDTO(book ?: BookDTO(), bookUpdates ?: emptyList())
    }

    fun getAllBooks(): MutableList<BookDTO> {
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

    private fun saveBookUpdate(bookUpdate: BookUpdateInputDTO, bookName: String, pagesRead: Long, currentDate: String): BookUpdatesFileDTO {
        val bookUpdateToSave = BookUpdatesFileDTO(mutableListOf(BookUpdateOutputDTO(
                name = bookName,
                pagesRead = pagesRead,
                date = bookUpdate.date ?: currentDate)
        ))
        progressService.saveProgress(pagesRead)
        return bookUpdatesRepository.saveBookUpdate(bookUpdateToSave)
    }

    private fun editUpdateFromToday(bookUpdates: BookUpdatesFileDTO?, bookName: String, pagesRead: Long,
                                    currentDate: String, bookUpdateFromToday: BookUpdateOutputDTO,
                                    bookUpdate: BookUpdateInputDTO, oldPage: Long): BookUpdatesFileDTO {
        val oldBookUpdates = bookUpdates?.bookUpdates?.filter {
            it.date != bookUpdateFromToday.date || it.name != bookName
        }?.toMutableList()
        oldBookUpdates?.add(0, BookUpdateOutputDTO(
                name = bookName,
                pagesRead = bookUpdate.currentPage.minus(oldPage).plus(bookUpdateFromToday.pagesRead),
                date = currentDate)
        )
        progressService.saveProgress(pagesRead)
        return bookUpdatesRepository.saveBookUpdate(BookUpdatesFileDTO(oldBookUpdates ?: mutableListOf()))
    }
}
