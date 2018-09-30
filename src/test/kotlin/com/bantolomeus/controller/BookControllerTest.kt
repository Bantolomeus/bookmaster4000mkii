package com.bantolomeus.controller

import com.bantolomeus.dto.BookDTO
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

    // TODO:
    @Test
    fun updateBook() {
        val book = BookDTO(
                name = "this is a wonderful book",
                author = "Mr. White",
                pagesTotal = 294
        )

        bookController.createBook(book)
        verify(bookService).createBook(book)
    }

    // TODO:
    @Test
    fun getBookWithUpdates() {
        val book = BookDTO(
                name = "this is a wonderful book",
                author = "Mr. White",
                pagesTotal = 294
        )

        bookController.createBook(book)
        verify(bookService).createBook(book)
    }

    @Test
    fun getAllBookNames() {
        bookController.getAllBookNames()
        verify(bookService).getAllBookNames()
    }
}
