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
import kotlin.math.exp
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class BookServiceTest {

    @Mock
    lateinit var bookRepository: BookRepository

    @Mock
    lateinit var challengeService: ChallengeService

    @InjectMocks
    lateinit var booksService: BookService

    @Test
    fun getAllBooks() {

        val bookDTO = BookDTO(name = "testName", author = "testAuthor", pagesTotal = 521)
        val expectedList = listOf("testName")
        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO))

        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        val allBooks = booksService.getAllBooks()

        assertEquals(expectedList,allBooks)
    }

}
