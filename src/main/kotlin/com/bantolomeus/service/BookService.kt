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
        if (savedBookDTO.currentPage > 0L) {
            saveBookUpdate(savedBookDTO)
        }
        return bookDTO
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO, bookName: String): BookUpdatesFileDTO {
        var response = BookUpdatesFileDTO()
        var books = bookRepository.getBooks().books.filter { it.name != bookName }
        var oldPage = Long.MAX_VALUE
        val foundBook = bookRepository.getBookByName(bookName)

        if (foundBook.name == bookName) {
            foundBook.let {
                oldPage = it.currentPage
                it.currentPage = bookUpdate.currentPage
                if (it.currentPage >= it.pagesTotal) {
                    it.readTime = (Date().time - dateFormat.parse(it.dateStarted).time) / DIVISOR_FOR_DAY
                }
            }
            books = mutableListOf(foundBook) + books.toMutableList()
        }

        if (oldPage < bookUpdate.currentPage && foundBook.name == bookName
                && bookUpdate.currentPage <= foundBook.pagesTotal) {
            bookRepository.saveBooks(BooksFileDTO(books.toMutableList()))
            val bookUpdates = bookUpdatesRepository.getBookUpdates()
            val currentDate = dateFormat.format(Date())
            val foundBookUpdate = bookUpdates.bookUpdates.filter { it.date == currentDate && it.name == bookName }
                    .getOrElse(0) { _ -> BookUpdateOutputDTO()}

            val pagesRead = bookUpdate.currentPage.minus(oldPage)
            if (pagesRead > 0 && foundBookUpdate.date.isEmpty()) {
                bookUpdates.bookUpdates.add(0, BookUpdateOutputDTO(
                        name = bookName,
                        pagesRead = pagesRead,
                        date = currentDate)
                )
                response = bookUpdatesRepository.saveBookUpdate(bookUpdates)
                progressService.saveProgress(pagesRead)
            } else if (foundBookUpdate.date.isNotEmpty()) {
                val oldBookUpdates = bookUpdates.bookUpdates.filter {
                    it.date != foundBookUpdate.date || it.name != bookName
                }.toMutableList()
                oldBookUpdates.add(0, BookUpdateOutputDTO(
                        name = bookName,
                        pagesRead = bookUpdate.currentPage.minus(oldPage).plus(foundBookUpdate.pagesRead),
                        date = currentDate)
                )
                response = bookUpdatesRepository.saveBookUpdate(BookUpdatesFileDTO(oldBookUpdates))
                progressService.saveProgress(pagesRead)
            }
        }
        return response
    }

    fun getBookWithUpdates(bookName: String): BookGetDTO {
        val book = bookRepository.getBookByName(bookName)
        val bookUpdates = bookUpdatesRepository.getBookUpdates().bookUpdates
                .filter { it.name == bookName }
                .map { ProgressUpdateDTO(it.date, it.pagesRead)}
        return BookGetDTO(book, bookUpdates)
    }

    fun getAllBookNames(): List<String> {
        return bookRepository.getBooks().books.map { it.name }.sorted()
    }

    fun sortBookUpdates(): BookUpdatesFileDTO {
        return bookUpdatesRepository.sortBookUpdates()
    }

    private fun saveBookUpdate(bookDTO: BookDTO) {
        if (bookDTO.currentPage > 0) {
            val bookUpdates = bookUpdatesRepository.getBookUpdates()
            bookUpdates.bookUpdates.add(0, bookDTO.toBookUpdateDTO())
            bookUpdatesRepository.saveBookUpdate(bookUpdates)
            progressService.saveProgress(bookDTO.currentPage)
        }
    }
}
