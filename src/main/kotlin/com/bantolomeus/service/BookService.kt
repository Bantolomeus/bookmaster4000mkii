package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.translator.toBookUpdateDTO
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun createBook(bookDTO: BookDTO){
        val books = bookRepository.getBooks()
        var update = true
        books?.books?.forEach { if (it.name == bookDTO.name) update = false}
        if (update) books?.books?.add(bookDTO)
        bookRepository.saveBook(books)
        if (bookDTO.pagesRead != null) { bookRepository.saveBookUpdate(bookDTO.toBookUpdateDTO())}
    }

    fun updateBook(bookUpdate: BookUpdateDTO) {

    }

    fun getBook(allBooks: Boolean, bookName: String) {

    }
}