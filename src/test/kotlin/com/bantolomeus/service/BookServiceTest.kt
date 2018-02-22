package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.repository.BookRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class BookServiceTest {

    @Mock
    private lateinit var bookRepository: BookRepository

    @Mock
    lateinit var challengeService: ChallengeService

    @InjectMocks
    private lateinit var booksService: BookService

    @Test
    fun getAllBooks() {

        val bookDTO1 = BookDTO(name = "testName", author = "testAuthor", pagesTotal = 521)
        val bookDTO2 = BookDTO(name = "testName2", author = "testAuthor", pagesTotal = 51)
        val expectedList = listOf("testName", "testName2")
        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO1, bookDTO2))

        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        val allBooks = booksService.getAllBooks()

        assertEquals(expectedList,allBooks)
    }

    @Test
    fun getAllBooksWithoutBooks() {

        val expectedList = listOf<String>()
        val booksFileDTO = BooksFileDTO(mutableListOf())

        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        val allBooks = booksService.getAllBooks()

        assertEquals(expectedList,allBooks)
    }
}
