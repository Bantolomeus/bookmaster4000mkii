package com.bantolomeus.controller

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.*
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.repository.ProgressRepository
import com.bantolomeus.service.BookService
import com.bantolomeus.service.ProgressService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
class BookControllerIT {

    private lateinit var today: String
    private lateinit var fileNameChallenge: String
    private lateinit var challengeRepository: ChallengeRepository

    private lateinit var fileNameBookUpdates: String
    private lateinit var bookUpdateRepository: BookUpdatesRepository

    private lateinit var fileNameBook: String
    private lateinit var bookRepository: BookRepository
    private lateinit var bookService: BookService
    private lateinit var bookController: BookController

    private lateinit var fileNameProgress: String
    private lateinit var progressRepository: ProgressRepository
    private lateinit var progressService: ProgressService

    @Before
    fun setUpClasses() {
        today = dateFormat.format(Date())
        fileNameChallenge = "testChallenge.json"
        challengeRepository = ChallengeRepository(fileNameChallenge)

        fileNameProgress = "testProgress.json"
        progressRepository = ProgressRepository(fileNameProgress)
        progressService = ProgressService(progressRepository, challengeRepository)

        fileNameBook = "testBooks.json"
        bookRepository = BookRepository(fileNameBook)
        fileNameBookUpdates = "testBookUpdates.json"
        bookUpdateRepository = BookUpdatesRepository(fileNameBookUpdates)
        bookService = BookService(bookRepository, bookUpdateRepository, progressService)
        bookController = BookController(bookService)
    }

    @Test
    fun createBookWithCurrentPage() {
        val bookDTO = BookDTO(name = "testBook", author = "Jim Carry", pagesTotal = 314, currentPage = 12)
        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        val progressDTO = ProgressFileDTO(pagesReadInCurrentChallenge = 0, pagesEverRead = 1000)
        val bookUpdate = BookUpdateOutputDTO(name = "testBook", pagesRead = 12, date = today)

        progressRepository.saveProgress(progressDTO)

        assertTrue { challengeRepository.getChallenge().dateStarted.isEmpty() }
        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val readingStatusBefore = progressService.calculateReadingState()
        val bookResponse = bookController.createBook(bookDTO)
        val readingStatusAfter = progressService.calculateReadingState()
        val bookUpdatesUpdated = bookUpdateRepository.getBookUpdates()

        assertNotEquals(readingStatusBefore, readingStatusAfter)
        assertEquals(readingStatusAfter, (readingStatusBefore + bookResponse.currentPage))
        assertTrue { bookUpdatesUpdated.bookUpdates.contains(bookUpdate) }
    }

    // TODO: order data with functions
    @Test
    fun createAndUpdateBookWithUpdateAndBookUpdatesAreSortedByDateDESC() {

        val progressDTO = ProgressFileDTO(pagesReadInCurrentChallenge = 0, pagesEverRead = 1000)
        progressRepository.saveProgress(progressDTO)

        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val readingStatusInit = progressService.calculateReadingState()

        val bookUpdates = BookUpdatesFileDTO(mutableListOf(
                BookUpdateOutputDTO("Bugs Bunny", 152, "13/06/2018"),
                BookUpdateOutputDTO("Fire", 2, "13/06/2018"),
                BookUpdateOutputDTO("Ants", 4, "13/06/2018"),
                BookUpdateOutputDTO("House in the woods", 32, "04/10/2017"),
                BookUpdateOutputDTO("Zap", 41, "01/01/2001")))
        bookUpdateRepository.saveBookUpdate(bookUpdates)

        val bookDTO = BookDTO(name = "testBook", author = "Jim Carry", pagesTotal = 314, currentPage = 12)
        val bookResponse = bookController.createBook(bookDTO)
        val readingStatusAfterBookCreation = progressService.calculateReadingState()
        val bookUpdatesUpdated = bookUpdateRepository.getBookUpdates()

        assertNotEquals(readingStatusInit, readingStatusAfterBookCreation)
        assertEquals(readingStatusAfterBookCreation, (readingStatusInit + bookResponse.currentPage))
        assertEquals(bookUpdatesUpdated.bookUpdates[0].name, bookDTO.name)
        assertEquals(bookUpdatesUpdated.bookUpdates[0].pagesRead, bookDTO.currentPage)
        assertEquals(bookUpdatesUpdated.bookUpdates[1].name, bookUpdates.bookUpdates[0].name)
        assertEquals(bookUpdatesUpdated.bookUpdates[2].name, bookUpdates.bookUpdates[1].name)
        assertEquals(bookUpdatesUpdated.bookUpdates[3].name, bookUpdates.bookUpdates[2].name)
        assertEquals(bookUpdatesUpdated.bookUpdates[4].name, bookUpdates.bookUpdates[3].name)
        assertEquals(bookUpdatesUpdated.bookUpdates[5].name, bookUpdates.bookUpdates[4].name)

        val bookUpdateDTO = BookUpdateInputDTO(currentPage = 27)
        val updateResponse = bookController.updateBook(bookUpdateDTO, bookDTO.name)
        val readingStatusAfterBookUpdate = progressService.calculateReadingState()

        assertEquals((readingStatusInit + bookUpdateDTO.currentPage), readingStatusAfterBookUpdate)
        assertEquals(bookDTO.name, updateResponse.bookUpdates[0].name)
        assertEquals(updateResponse.bookUpdates[0].pagesRead, bookUpdateDTO.currentPage)
        assertEquals(updateResponse.bookUpdates[1].name, bookUpdates.bookUpdates[0].name)
        assertEquals(updateResponse.bookUpdates[2].name, bookUpdates.bookUpdates[1].name)
        assertEquals(updateResponse.bookUpdates[3].name, bookUpdates.bookUpdates[2].name)
        assertEquals(updateResponse.bookUpdates[4].name, bookUpdates.bookUpdates[3].name)
        assertEquals(updateResponse.bookUpdates[5].name, bookUpdates.bookUpdates[4].name)
    }

    @Test
    fun updateBookAndBookUpdatesAreSortedByDateDESC() {
        val bookDTO = BookDTO(name = "Zap", author = "Jim Carry", pagesTotal = 314, currentPage = 41, dateStarted = today)
        val bookDTOAfterUpdate = BookDTO(name = "Zap", author = "Jim Carry", pagesTotal = 314, currentPage = 61, dateStarted = today)
        val bookUpdateDTO = BookUpdateInputDTO(61)
        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        val progressDTO = ProgressFileDTO(pagesReadInCurrentChallenge = 0, pagesEverRead = 1000)
        val bookUpdates = BookUpdatesFileDTO(mutableListOf(
                BookUpdateOutputDTO("Bugs Bunny", 152, today),
                BookUpdateOutputDTO("Fire", 2, "13/06/2018"),
                BookUpdateOutputDTO("Ants", 4, "13/06/2018"),
                BookUpdateOutputDTO("House in the woods", 32, "04/10/2017"),
                BookUpdateOutputDTO("Zap", 41, "01/01/2001")))

        val progressFileBefore = progressRepository.saveProgress(progressDTO)
        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val readingStatusBefore = progressService.calculateReadingState()
        bookUpdateRepository.saveBookUpdate(bookUpdates)
        bookRepository.saveBookIfItNotExists(bookDTO)
        val bookUpdateResponse = bookController.updateBook(bookUpdateDTO, "Zap")
        val booksFile = bookRepository.getBooks()
        val readingStatusAfter = progressService.calculateReadingState()
        val progressFileAfter = progressRepository.getProgress()

        assertNotEquals(readingStatusBefore, readingStatusAfter)
        assertEquals(readingStatusAfter, (readingStatusBefore + bookUpdateResponse.bookUpdates[0].pagesRead))
        assertEquals(progressFileAfter.pagesEverRead, (progressFileBefore.pagesEverRead + 20))
        assertEquals(booksFile[0], bookDTOAfterUpdate)
        assertEquals(bookUpdateResponse.bookUpdates[0].name, "Zap")
        assertEquals(bookUpdateResponse.bookUpdates[0].date, today)
        assertEquals(bookUpdateResponse.bookUpdates[0].pagesRead, 20)
        assertEquals(bookUpdateResponse.bookUpdates[1].name, bookUpdates.bookUpdates[0].name)
        assertEquals(bookUpdateResponse.bookUpdates[2].name, bookUpdates.bookUpdates[1].name)
        assertEquals(bookUpdateResponse.bookUpdates[3].name, bookUpdates.bookUpdates[2].name)
        assertEquals(bookUpdateResponse.bookUpdates[4].name, bookUpdates.bookUpdates[3].name)
        assertEquals(bookUpdateResponse.bookUpdates[5].name, bookUpdates.bookUpdates[4].name)
    }

    @Test
    fun getAllBooks() {
        val books = mutableListOf(
                BookDTO(name = "Bugs Bunny", author = "ich", pagesTotal = 152),
                BookDTO(name = "Fire", author = "er", pagesTotal =  2),
                BookDTO(name = "Ants", author = "sie", pagesTotal =  4),
                BookDTO(name = "House in the woods", author = "es", pagesTotal = 32),
                BookDTO(name = "Zap", author = "du", pagesTotal = 41))

        bookRepository.saveBooks(books)

        val response = bookController.getAllBooks()

        assertEquals(books, response)
    }

    @After
    fun removeFiles() {
        File(fileNameBook).deleteRecursively()
        File(fileNameBookUpdates).deleteRecursively()
        File(fileNameChallenge).deleteRecursively()
        File(fileNameProgress).deleteRecursively()
    }
}
