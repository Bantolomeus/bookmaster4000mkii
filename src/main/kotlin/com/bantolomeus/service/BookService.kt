package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.DIVISOR_FOR_DAY
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository,
                  private val challengeService: ChallengeService) {

    fun createBook(bookDTO: BookDTO){
        val books = bookRepository.getBooks()
        var toSave = true
        books.books.forEach { if (it.name == bookDTO.name) toSave = false}
        if (toSave) {
            bookDTO.dateStarted = dateFormat.format(Date())
            books.books.add(bookDTO)
            bookRepository.saveBook(books)
        }

        if (bookDTO.currentPage > 0) {
        val booksUpdates = bookRepository.getBooksUpdates()
            booksUpdates.booksUpdate.add(bookDTO.toBookUpdateDTO())
            bookRepository.saveBookUpdate(booksUpdates)
            challengeService.saveChallenge(bookDTO.currentPage)
        }
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO) {
        val books = bookRepository.getBooks()
        var oldPage: Long = 0
        var bookFound = false
        books.books.forEach {
            if (it.name == bookUpdate.name) {
                bookFound = true
                it.currentPage.let { oldPage = it }
                it.currentPage = bookUpdate.currentPage
                if (it.currentPage == it.pagesTotal) {
                    it.readTime = (Date().time - dateFormat.parse(it.dateStarted).time) / DIVISOR_FOR_DAY
                }
            }
        }

        if (oldPage < bookUpdate.currentPage && bookFound) {
            bookRepository.saveBook(books)
            val booksUpdates = bookRepository.getBooksUpdates()
            val currentDate = dateFormat.format(Date())
            var foundBookUpdate = BookUpdateOutputDTO()
            booksUpdates.booksUpdate.forEach {
                if (it.date == currentDate && it.name == bookUpdate.name) {
                    foundBookUpdate = it
                }
            }

            val pagesRead = bookUpdate.currentPage.minus(oldPage)
            if (pagesRead > 0 && foundBookUpdate.date == "") {
                booksUpdates.booksUpdate.add(BookUpdateOutputDTO(
                        name = bookUpdate.name,
                        pagesRead = pagesRead,
                        date = currentDate)
                )
                bookRepository.saveBookUpdate(booksUpdates)
                challengeService.saveChallenge(pagesRead)
            } else if (foundBookUpdate.date != "") {
                val oldBookUpdates = booksUpdates.booksUpdate.filter {
                    it.date != foundBookUpdate.date || it.name != bookUpdate.name
                }
                bookRepository.saveBookUpdate(BooksUpdatesFileDTO((oldBookUpdates + BookUpdateOutputDTO(
                        name = bookUpdate.name,
                        pagesRead = bookUpdate.currentPage.minus(oldPage).plus(foundBookUpdate.pagesRead),
                        date = currentDate)).toMutableList())
                )
                challengeService.saveChallenge(pagesRead)
            }
        }
    }

    fun getBook(bookName: String): BookGetDTO {
            var book = BookDTO()
            bookRepository.getBooks().books.forEach { if (it.name == bookName) book = it }
            val bookUpdates = bookRepository.getBooksUpdates().booksUpdate.map { it }
                    .filter { it.name == bookName }
                    .map { mapOf(it.date to it.pagesRead)}
            return BookGetDTO(book, bookUpdates)
    }

    fun getAllBooks(): List<String> {
        return bookRepository.getBooks().books.map { it.name }.sorted()
    }
}
