package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import com.bantolomeus.util.dateFormat
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

    fun createBook(bookDTO: BookDTO){
        val books = bookRepository.getBooks()
        val booksUpdates = bookRepository.getBooksUpdates()
        var update = true
        books?.books?.forEach { if (it.name == bookDTO.name) update = false}
        if (update) {
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
            }
        }
        bookRepository.saveBook(books)

        val bookUpdateToSave = BookUpdateOutputDTO(name = bookUpdate.name,
                                                   pagesRead = bookUpdate.currentPage?.minus(oldPage!!),
                                                   date = dateFormat.format(Date()))
        booksUpdates?.booksUpdate?.add(bookUpdateToSave)
        bookRepository.saveBookUpdate(booksUpdates)
    }

    fun getBook(allBooks: Boolean, bookName: String) {

    }
}