package com.bantolomeus.service

import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.date.dateFormat
import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.dto.*
import com.bantolomeus.repository.BookUpdatesRepository
import com.bantolomeus.translator.toProgressUpdateDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository,
                  private val bookUpdatesRepository: BookUpdatesRepository,
                  private val challengeService: ChallengeService) {

    fun createBook(bookDTO: BookDTO): BookGetDTO {
        val savedBookDTO = bookRepository.saveBookIfItNotExists(bookDTO)
        return if (savedBookDTO.currentPage != 0L) {
            saveBookUpdate(savedBookDTO)
            BookGetDTO(bookDTO, listOf(bookDTO.toProgressUpdateDTO()))
        } else BookGetDTO(bookDTO)
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO, bookName: String): BookUpdatesFileDTO {
        var response = BookUpdatesFileDTO()
        val books = bookRepository.getBooks()
        var oldPage = 5000L
        val foundBook = bookRepository.getBookByName(bookName)

        if (foundBook.name == bookName) {
            foundBook.let {
                oldPage = it.currentPage
                it.currentPage = bookUpdate.currentPage
                if (it.currentPage >= it.pagesTotal) {
                    it.readTime = (Date().time - dateFormat.parse(it.dateStarted).time) / DIVISOR_FOR_DAY
                }
            }
        }

        if (oldPage < bookUpdate.currentPage && foundBook.name == bookName
                && bookUpdate.currentPage <= foundBook.pagesTotal) {
            bookRepository.saveBooks(books)
            val bookUpdates = bookUpdatesRepository.getBookUpdates()
            val currentDate = dateFormat.format(Date())
            val foundBookUpdate = bookUpdates.bookUpdates.filter { it.date == currentDate && it.name == bookName }
                    .getOrElse(0) { _ -> BookUpdateOutputDTO()}

            val pagesRead = bookUpdate.currentPage.minus(oldPage)
            if (pagesRead > 0 && foundBookUpdate.date == "") {
                bookUpdates.bookUpdates.add(BookUpdateOutputDTO(
                        name = bookName,
                        pagesRead = pagesRead,
                        date = currentDate)
                )
                response = bookUpdatesRepository.saveBookUpdate(bookUpdates)
                challengeService.saveOrUpdateChallenge(pagesRead)
            } else if (foundBookUpdate.date != "") {
                val oldBookUpdates = bookUpdates.bookUpdates.filter {
                    it.date != foundBookUpdate.date || it.name != bookName
                }
                response = bookUpdatesRepository.saveBookUpdate(BookUpdatesFileDTO((oldBookUpdates + BookUpdateOutputDTO(
                        name = bookName,
                        pagesRead = bookUpdate.currentPage.minus(oldPage).plus(foundBookUpdate.pagesRead),
                        date = currentDate)).toMutableList())
                )
                challengeService.saveOrUpdateChallenge(pagesRead)
            }
        }
        return response
    }

    fun getBookWithUpdates(bookName: String): BookGetDTO {
        val book = bookRepository.getBookByName(bookName)
//        val bookUpdates = bookUpdatesRepository.getBookUpdates().bookUpdates
//                .asSequence()
//                .filter { it.name == bookName }
//                .map { mapOf(it.date to it.pagesRead)}
//                .toList()
        val bookUpdates = bookUpdatesRepository.getBookUpdates().bookUpdates
                .asSequence()
                .filter { it.name == bookName }
                .map { ProgressUpdateDTO(it.date, it.pagesRead)}
                .toList()
        return BookGetDTO(book, bookUpdates)
    }

    fun getAllBookNames(): List<String> {
        return bookRepository.getBooks().books.asSequence().map { it.name }.sorted().toList()
    }

    fun sortBookUpdates(): BookUpdatesFileDTO {
        return bookUpdatesRepository.sortBookUpdates()
    }

    private fun saveBookUpdate(bookDTO: BookDTO) {
        if (bookDTO.currentPage > 0) {
            val bookUpdates = bookUpdatesRepository.getBookUpdates()
            bookUpdates.bookUpdates.add(bookDTO.toBookUpdateDTO())
            bookUpdatesRepository.saveBookUpdate(bookUpdates)
            challengeService.saveOrUpdateChallenge(bookDTO.currentPage)
        }
    }

}
