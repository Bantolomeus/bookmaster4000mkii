package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.divisorDay
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

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
        if (bookDTO.currentPage != null)  bookRepository.saveBookUpdate(booksUpdates)
    }

    fun updateBook(bookUpdate: BookUpdateInputDTO) {
        val books = bookRepository.getBooks()
        val booksUpdates = bookRepository.getBooksUpdates()
        var oldPage: Long? = 0
        books?.books?.forEach {
            if (it.name == bookUpdate.name) {
                it.currentPage?.let { oldPage = it }
                it.currentPage = bookUpdate.currentPage
                if (it.currentPage == it.pagesTotal) it.readTime = (Date().time - dateFormat.parse(it.dateStarted).time) / divisorDay
            }
        }
        bookRepository.saveBook(books)

        val bookUpdateToSave = BookUpdateOutputDTO(name = bookUpdate.name,
                                                   pagesRead = bookUpdate.currentPage?.minus(oldPage!!),
                                                   date = dateFormat.format(Date()))
        if (bookUpdateToSave.pagesRead!! > 0) {
            booksUpdates?.booksUpdate?.add(bookUpdateToSave)
            bookRepository.saveBookUpdate(booksUpdates)
        }
    }

    fun getBook(allBooks: Boolean, bookName: String): Any? {
        if (bookName.isNotEmpty()) {
            var book = BookDTO()
            val books = bookRepository.getBooks()
            books?.books?.forEach { if (it.name == bookName) book = it }


            val booksUpdates = bookRepository.getBooksUpdates()
            val bookUpdates = booksUpdates?.booksUpdate?.map { it }?.filter { it.name == bookName }?.map { mapOf(it.date to it.pagesRead)}

            return BookGetDTO(book, bookUpdates)
        }
        //TODO: handle allBooks parameter
        return BookGetDTO()
    }
}