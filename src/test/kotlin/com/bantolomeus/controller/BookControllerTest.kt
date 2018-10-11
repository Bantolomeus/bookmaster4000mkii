package com.bantolomeus.controller

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateInputDTO
import com.bantolomeus.service.BookService
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class BookControllerTest {

    @Mock
    lateinit var bookService: BookService

    @InjectMocks
    private lateinit var bookController: BookController

    @Test
    fun createBook() {
        val book = BookDTO(
                name = "this is a wonderful book",
                author = "Mr. White",
                pagesTotal = 294
        )

        bookController.createBook(book)
        verify(bookService).createBook(book)
    }

    @Test
    fun updateBook() {
        val bookName = "this is a wonderful book"
        val bookUpdate = BookUpdateInputDTO(
                currentPage = 21
        )

        bookController.updateBook(bookUpdate, bookName = "this is a wonderful book")
        verify(bookService).updateBook(bookUpdate, bookName)
    }

    @Test
    fun getBookWithUpdates() {
        val bookName = "House"

        bookController.getBookWithUpdates(bookName)
        verify(bookService).getBookWithUpdates(bookName)
    }

    @Test
    fun getAllBookNames() {
        bookController.getAllBookNames()
        verify(bookService).getAllBookNames()
    }
}
