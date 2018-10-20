package com.bantolomeus.service

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdatesFileDTO
import com.bantolomeus.dto.BooksFileDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import com.nhaarman.mockito_kotlin.given
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
        val bookDTO3 = BookDTO()
        val expectedBooks = BooksFileDTO(mutableListOf(bookDTO1, bookDTO3, bookDTO2))

        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO1, bookDTO3))
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
        compareBookDTOWithBookGetDTO(bookDTO, response)
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
        compareBookDTOWithBookGetDTO(bookDTO, response)
    }

    private fun compareBookDTOWithBookGetDTO(expected: BookDTO, actual: BookGetDTO) {
        assertEquals(expected.name, actual.book.name)
        assertEquals(expected.readTime, actual.book.readTime)
        assertEquals(expected.dateStarted, actual.book.dateStarted)
        assertEquals(expected.currentPage, actual.book.currentPage)
        assertEquals(expected.pagesTotal, actual.book.pagesTotal)
        assertEquals(expected.author, actual.book.author)
        assertEquals(emptyList(), actual.updates)
    }
}
