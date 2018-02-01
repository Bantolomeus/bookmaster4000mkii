package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
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
        if (bookDTO.pagesRead != null)  bookRepository.saveBookUpdate(booksUpdates)
    }

    fun updateBook(bookUpdate: BookUpdateDTO) {

    }

    fun getBook(allBooks: Boolean, bookName: String) {

    }
}