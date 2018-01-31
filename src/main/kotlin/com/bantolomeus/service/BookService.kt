package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import org.springframework.stereotype.Service

@Service
class BookService {

    fun createBook(bookDTO: BookDTO): Any {
        
        return "fuck"
    }

    fun updateBook(bookUpdate: BookUpdateDTO) {

    }

    fun getBook(allBooks: Boolean, bookName: String) {

    }
}