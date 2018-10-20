package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdatesFileDTO
import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.willReturn
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class BookServiceTest {

    @Mock
    private lateinit var bookRepository: BookRepository

    @Mock
    private lateinit var bookUpdatesRepository: BookUpdatesRepository

    @Mock
    lateinit var challengeService: ChallengeService

    @InjectMocks
    private lateinit var booksService: BookService

    private val booksFileDTOCaptor = argumentCaptor<BooksFileDTO>()

    @Test
    fun getAllBooks() {

        val bookDTO1 = BookDTO(name = "testName", author = "testAuthor", pagesTotal = 521)
        val bookDTO2 = BookDTO(name = "testName2", author = "testAuthor", pagesTotal = 51)
        val expectedList = listOf("testName", "testName2")
        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO1, bookDTO2))

        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        val allBooks = booksService.getAllBookNames()

        assertEquals(expectedList, allBooks)
    }

    @Test
    fun getAllBooksWithoutBooks() {

        val expectedList = listOf<String>()
        val booksFileDTO = BooksFileDTO(mutableListOf())

        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        val allBooks = booksService.getAllBookNames()

        assertEquals(expectedList, allBooks)
    }

    @Test
    fun createBookWithoutCurrentPage() {
        val bookDTO1 = BookDTO(name = "Wohlfahrtsverlust durch Steuern", author = "Your Mother", pagesTotal = 521)
        val bookDTO2 = BookDTO(name = "Hasenklo ist so fro", author = "Vin Diesel", pagesTotal = 51)
        val expectedBooks = BooksFileDTO(mutableListOf(bookDTO1, bookDTO2))

        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO1))
        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        booksService.createBook(bookDTO2)

        verify(bookRepository).saveBook(booksFileDTOCaptor.capture())
        assertEquals(expectedBooks, booksFileDTOCaptor.firstValue)
    }

    @Test
    fun getBookWithBookName() {
        val bookDTO = BookDTO(name = "Anger management deluxe")
        val bookFileDto = BooksFileDTO(mutableListOf(bookDTO))
        val bookUpdates = BookUpdatesFileDTO()

        given(bookRepository.getBooks()).willReturn(bookFileDto)
        given(bookUpdatesRepository.getBooksUpdates()).willReturn(bookUpdates)
        val response = booksService.getBookWithUpdates(bookDTO.name)

        verify(bookRepository).getBooks()
        assertEquals(bookDTO.name, response.book.name)
        assertEquals(bookDTO.readTime, response.book.readTime)
        assertEquals(bookDTO.dateStarted, response.book.dateStarted)
        assertEquals(bookDTO.currentPage, response.book.currentPage)
        assertEquals(bookDTO.pagesTotal, response.book.pagesTotal)
        assertEquals(bookDTO.author, response.book.author)
        assertEquals(emptyList(), response.updates)
    }

    @Test
    fun getEmptyBook() {
        val bookDTO = BookDTO()
        val bookFileDto = BooksFileDTO(mutableListOf(bookDTO))
        val bookUpdates = BookUpdatesFileDTO()

        given(bookRepository.getBooks()).willReturn(bookFileDto)
        given(bookUpdatesRepository.getBooksUpdates()).willReturn(bookUpdates)
        val response = booksService.getBookWithUpdates(bookDTO.name)

        verify(bookRepository).getBooks()
        assertEquals(bookDTO.name, response.book.name)
        assertEquals(bookDTO.readTime, response.book.readTime)
        assertEquals(bookDTO.dateStarted, response.book.dateStarted)
        assertEquals(bookDTO.currentPage, response.book.currentPage)
        assertEquals(bookDTO.pagesTotal, response.book.pagesTotal)
        assertEquals(bookDTO.author, response.book.author)
        assertEquals(emptyList(), response.updates)
    }
}
