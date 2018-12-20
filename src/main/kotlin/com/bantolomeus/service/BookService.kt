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
            book.readTime = ((Date().time - dateFormat.parse(book.dateStarted).time) / DIVISOR_FOR_DAY) + 1
        }
        val oldPage = book.currentPage
        book.currentPage = bookUpdate.currentPage
        val pagesRead = bookUpdate.currentPage.minus(oldPage)
        if (pagesRead <= 0 || bookUpdate.currentPage > book.pagesTotal) {
            return BookUpdatesFileDTO()
        }

        bookRepository.updateBook(book)

        // todo '.firstOrNull { it.date == currentDate && it.name == bookName }' is also something that should happen in the repo.
        //      repository.getTodaysUpdatesForBook(foundBook)
        // todo move exception into repo
        //      in this case I don't see any value in running the bookmaster w/o a working BookUpdatesRepository
        val bookUpdates = bookUpdatesRepository.getBookUpdates()
                ?: throw IllegalStateException("BookUpdatesRepository not available but it should be")
        val currentDate = dateFormat.format(Date())
        bookUpdates.bookUpdates.firstOrNull { it.date == currentDate && it.name == bookName }?.let {
            return editUpdateFromToday(bookUpdates, bookName, pagesRead, currentDate, it, bookUpdate, oldPage)
        } ?: return editUpdateFromToday(bookUpdates, bookName, pagesRead, bookUpdate.date ?: currentDate, BookUpdateOutputDTO(), bookUpdate, oldPage)
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
