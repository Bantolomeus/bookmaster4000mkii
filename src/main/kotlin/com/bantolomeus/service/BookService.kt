package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.date.dateFormat
import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.repository.BookUpdatesRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository,
                  private val bookUpdatesRepository: BookUpdatesRepository,
                  private val challengeService: ChallengeService) {

    fun createBook(bookDTO: BookDTO){
        val bookDTOChanged = saveBook(bookDTO)
        saveBookUpdate(bookDTOChanged)
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO, bookName: String): BooksUpdatesFileDTO {
        var response = BooksUpdatesFileDTO()
        val books = bookRepository.getBooks()
        var oldPage = 5000L
        val foundBook = books.books.filter { it.name == bookName }

        if (foundBook != emptyList<BooksFileDTO>()) {
            foundBook.let {
                oldPage = it[0].currentPage
                it[0].currentPage = bookUpdate.currentPage
                if (it[0].currentPage == it[0].pagesTotal) {
                    it[0].readTime = (Date().time - dateFormat.parse(it[0].dateStarted).time) / DIVISOR_FOR_DAY
                }
            }
        }

        if (oldPage < bookUpdate.currentPage && foundBook != emptyList<BooksFileDTO>()
                && bookUpdate.currentPage <= foundBook[0].pagesTotal) {
            bookRepository.saveBook(books)
            val booksUpdates = bookUpdatesRepository.getBooksUpdates()
            val currentDate = dateFormat.format(Date())
            val foundBookUpdate = booksUpdates.booksUpdate.filter { it.date == currentDate && it.name == bookName }
                    .getOrElse(0) { _ -> BookUpdateOutputDTO()}

            val pagesRead = bookUpdate.currentPage.minus(oldPage)
            if (pagesRead > 0 && foundBookUpdate.date == "") {
                booksUpdates.booksUpdate.add(BookUpdateOutputDTO(
                        name = bookName,
                        pagesRead = pagesRead,
                        date = currentDate)
                )
                response = bookUpdatesRepository.saveBookUpdate(booksUpdates)
                challengeService.saveOrUpdateChallenge(pagesRead)
            } else if (foundBookUpdate.date != "") {
                val oldBookUpdates = booksUpdates.booksUpdate.filter {
                    it.date != foundBookUpdate.date || it.name != bookName
                }
                response = bookUpdatesRepository.saveBookUpdate(BooksUpdatesFileDTO((oldBookUpdates + BookUpdateOutputDTO(
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
        var book = BookDTO()
        bookRepository.getBooks().books.forEach { if (it.name == bookName) book = it }
        val bookUpdates = bookUpdatesRepository.getBooksUpdates().booksUpdate
                .asSequence()
                .filter { it.name == bookName }
                .map { mapOf(it.date to it.pagesRead)}
                .toList()
        return BookGetDTO(book, bookUpdates)
    }

    fun getAllBookNames(): List<String> {
        return bookRepository.getBooks().books.asSequence().map { it.name }.sorted().toList()
    }

    fun sortBookUpdates(): BooksUpdatesFileDTO {
        return bookUpdatesRepository.sortBookUpdates()
    }

    private fun saveBook(bookDTO: BookDTO): BookDTO {
        val books = bookRepository.getBooks()
        var toSave = true
        books.books.forEach { if (it.name == bookDTO.name) toSave = false}
        if (toSave) {
            bookDTO.dateStarted = dateFormat.format(Date())
            books.books.add(bookDTO)
            bookRepository.saveBook(books)
        }
        return bookDTO
    }

    private fun saveBookUpdate(bookDTO: BookDTO) {
        if (bookDTO.currentPage > 0) {
            val booksUpdates = bookUpdatesRepository.getBooksUpdates()
            booksUpdates.booksUpdate.add(bookDTO.toBookUpdateDTO())
            bookUpdatesRepository.saveBookUpdate(booksUpdates)
            challengeService.saveOrUpdateChallenge(bookDTO.currentPage)
        }
    }

}
