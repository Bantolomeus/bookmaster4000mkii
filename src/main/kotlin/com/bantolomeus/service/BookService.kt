package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.dto.BooksUpdatesFileDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.divisorForDay
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository,
                  private val challengeService: ChallengeService) {

    fun createBook(bookDTO: BookDTO){
        val books = bookRepository.getBooks()
        val booksUpdates = bookRepository.getBooksUpdates()
        var toSave = true
        books?.books?.forEach { if (it.name == bookDTO.name) toSave = false}
        if (toSave) {
            bookDTO.dateStarted = dateFormat.format(Date())
            books?.books?.add(bookDTO)
            booksUpdates?.booksUpdate?.add(bookDTO.toBookUpdateDTO())
        }
        bookRepository.saveBook(books)
        if (bookDTO.currentPage != null) {
            bookRepository.saveBookUpdate(booksUpdates)
            challengeService.saveChallenge(bookDTO.currentPage)
        }
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO) {
        val books = bookRepository.getBooks()
        val booksUpdates = bookRepository.getBooksUpdates()
        var oldPage: Long = 0
        books?.books?.forEach {
            if (it.name == bookUpdate.name) {
                it.currentPage?.let { oldPage = it }
                if (oldPage < bookUpdate.currentPage!!) { it.currentPage = bookUpdate.currentPage }
                if (it.currentPage == it.pagesTotal) { it.readTime = (Date().time - dateFormat.parse(it.dateStarted).time) / divisorForDay
                }
            }
        }
        bookRepository.saveBook(books)
        bookRepository.saveBookUpdate(booksUpdates)

        if (oldPage < bookUpdate.currentPage!!) {

            var foundBookUpdate = BookUpdateOutputDTO()
            booksUpdates?.booksUpdate?.forEach {
                if (it.date == dateFormat.format(Date()) && it.name == bookUpdate.name) {
                    foundBookUpdate = it
                }
            }

            val bookUpdateToSave = BookUpdateOutputDTO(name = bookUpdate.name,
                    pagesRead = bookUpdate.currentPage.minus(oldPage),
                    date = dateFormat.format(Date()))
            if (bookUpdateToSave.pagesRead!! > 0 && foundBookUpdate.date == "") {
                booksUpdates?.booksUpdate?.add(bookUpdateToSave)
                bookRepository.saveBookUpdate(booksUpdates)
                challengeService.saveChallenge(bookUpdateToSave.pagesRead)
            } else if (foundBookUpdate.date != "") {
                val readPages = bookUpdateToSave.pagesRead
                bookUpdateToSave.apply { this.pagesRead = bookUpdate.currentPage.minus(oldPage).plus(foundBookUpdate.pagesRead!!) }
                val filteredBookUpdates = booksUpdates?.booksUpdate?.filter { it.date != foundBookUpdate.date || it.name != bookUpdate.name }
                (filteredBookUpdates as ArrayList).add(bookUpdateToSave)
                bookRepository.saveBookUpdate(BooksUpdatesFileDTO(filteredBookUpdates.toMutableList()))
                challengeService.saveChallenge(readPages)
            }
        }
    }

    fun getBook(bookName: String?): Any? {
        return if (bookName != null) {
            var book = BookDTO()
            val books = bookRepository.getBooks()
            books?.books?.forEach { if (it.name == bookName) book = it }

            val booksUpdates = bookRepository.getBooksUpdates()
            val bookUpdates = booksUpdates?.booksUpdate?.map { it }?.filter { it.name == bookName }?.map { mapOf(it.date to it.pagesRead)}

            BookGetDTO(book, bookUpdates)
        } else {
            bookRepository.getBooks()?.books?.map { it.name }?.sorted()
        }
    }
}