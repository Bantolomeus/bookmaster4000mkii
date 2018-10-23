package com.bantolomeus.controller

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.*
import com.bantolomeus.service.BookService
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

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
        val date = dateFormat.format(Date())
        val bookName = "this is a wonderful book"
        val bookUpdate = BookUpdateInputDTO(
                currentPage = 21
        )
        val returnValue = BookUpdatesFileDTO(mutableListOf(BookUpdateOutputDTO(
                name = bookName,
                pagesRead = bookUpdate.currentPage,
                date = date)))

        whenever(bookService.updateBook(bookUpdate, bookName)).thenReturn(returnValue)

        val response = bookController.updateBook(bookUpdate, bookName = bookName)

        verify(bookService).updateBook(bookUpdate, bookName)
        assertEquals(bookName, response.bookUpdates[0].name)
        assertEquals(bookUpdate.currentPage, response.bookUpdates[0].pagesRead)
        assertEquals(date, response.bookUpdates[0].date)
    }

    @Test
    fun getBookWithUpdates() {
        val bookName = "House"
        val date = dateFormat.format(Date())
        val bookDto = BookDTO(
                name = bookName,
                author = "bla",
                pagesTotal = 312,
                currentPage = 12,
                dateStarted = date,
                readTime = 0)
        val bookUpdate = listOf(ProgressUpdateDTO(date, 12L))
        val returnValue = BookGetDTO(bookDto, bookUpdate)

        whenever(bookService.getBookWithUpdates(bookName)).thenReturn(returnValue)

        val response = bookController.getBookWithUpdates(bookName)

        verify(bookService).getBookWithUpdates(bookName)
        assertEquals(bookName, response.book.name)
        assertEquals(bookDto.author, response.book.author)
        assertEquals(bookDto.pagesTotal, response.book.pagesTotal)
        assertEquals(bookDto.currentPage, response.book.currentPage)
        assertEquals(bookDto.dateStarted, response.book.dateStarted)
        assertEquals(bookDto.readTime, response.book.readTime)
        assertEquals(bookUpdate, response.updates)
    }

    @Test
    fun getAllBookNames() {
        bookController.getAllBookNames()
        verify(bookService).getAllBookNames()
    }
}
