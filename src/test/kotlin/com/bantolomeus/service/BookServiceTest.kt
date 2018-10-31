package com.bantolomeus.service

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.*
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
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

    private val bookDTOCaptor = argumentCaptor<BookDTO>()

    private val booksFileDTOCaptor = argumentCaptor<BooksFileDTO>()

    private val bookUpdateFileDTOCaptor = argumentCaptor<BookUpdatesFileDTO>()

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

        val booksFileDTO = BooksFileDTO(mutableListOf(bookDTO1, bookDTO3))
        given(bookRepository.getBooks()).willReturn(booksFileDTO)
        given(bookRepository.saveBookIfItNotExists(bookDTO2)).willReturn(bookDTO2)
        booksService.createBook(bookDTO2)

        verify(bookRepository).saveBookIfItNotExists(bookDTOCaptor.capture())
        assertEquals(bookDTO2, bookDTOCaptor.firstValue)
    }

    @Test
    fun getBookWithBookName() {
        val bookDTO = BookDTO(name = "Anger management deluxe")
        val bookUpdates = BookUpdatesFileDTO()

        given(bookRepository.getBookByName(any())).willReturn(bookDTO)
        given(bookUpdatesRepository.getBookUpdates()).willReturn(bookUpdates)
        val response = booksService.getBookWithUpdates(bookDTO.name)

        verify(bookRepository).getBookByName(bookDTO.name)
        compareBookDTOWithBookGetDTO(bookDTO, response)
    }

    @Test
    fun getEmptyBook() {
        val bookDTO = BookDTO()
        val bookUpdates = BookUpdatesFileDTO()

        given(bookRepository.getBookByName(any())).willReturn(bookDTO)
        given(bookUpdatesRepository.getBookUpdates()).willReturn(bookUpdates)
        val response = booksService.getBookWithUpdates(bookDTO.name)

        verify(bookRepository).getBookByName(bookDTO.name)
        compareBookDTOWithBookGetDTO(bookDTO, response)
    }

    @Test
    fun updateBook() {
        val bookDTO = BookDTO(name = "Wohlfahrtsverlust durch Steuern", author = "Your Mother", pagesTotal = 521)
        val bookDTOUpdated = BookDTO(
                name = "Wohlfahrtsverlust durch Steuern",
                author = "Your Mother",
                pagesTotal = 521,
                currentPage = 12)
        val bookUpdate = BookUpdateInputDTO(12)
        val booksFile = BooksFileDTO(mutableListOf(bookDTO))
        val bookUpdates = BookUpdatesFileDTO(
                mutableListOf(
                        BookUpdateOutputDTO(
                            bookDTO.name,
                            bookUpdate.currentPage,
                            dateFormat.format(Date())
                        )
                )
        )

        given(bookRepository.getBooks()).willReturn(booksFile)
        given(bookRepository.getBookByName(any())).willReturn(bookDTO)
        given(bookUpdatesRepository.saveBookUpdate(bookUpdates)).willReturn(bookUpdates)
        given(bookUpdatesRepository.getBookUpdates()).willReturn(BookUpdatesFileDTO())

        booksService.updateBook(bookUpdate, bookDTO.name)

        verify(bookRepository).saveBooks(booksFileDTOCaptor.capture())
        verify(bookUpdatesRepository).saveBookUpdate(bookUpdateFileDTOCaptor.capture())
        assertEquals(bookUpdates, bookUpdateFileDTOCaptor.firstValue)
        assertEquals(bookDTOUpdated, booksFileDTOCaptor.firstValue.books[0])
    }

    @Test
    fun pagesLeft() {
        val bookDTO = BookDTO(name = "The stormy sea", pagesTotal = 890, currentPage = 100)

        given(bookRepository.getBookByName(any())).willReturn(bookDTO)
        val response = booksService.pagesLeft(bookDTO.name)

        assertEquals(790, response)
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
