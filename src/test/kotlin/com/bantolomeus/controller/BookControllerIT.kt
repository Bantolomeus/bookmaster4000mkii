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

    @Test
    fun createBookWithCurrentPage() {
        val bookDTO = BookDTO(name = "testBook", author = "Jim Carry", pagesTotal = 314, currentPage = 12)
        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        val progressDTO = ProgressFileDTO(pagesSinceStart = 0, pagesEverRead = 1000)
        val bookUpdate = BookUpdateOutputDTO(name = "testBook", pagesRead = 12, date = today)

        progressRepository.saveProgress(progressDTO)
        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val readingStatusBefore = progressService.calculateReadingState()
        val bookResponse = bookController.createBook(bookDTO)
        val readingStatusAfter = progressService.calculateReadingState()
        val bookUpdatesUpdated = bookUpdateRepository.getBookUpdates()

        assertNotEquals(readingStatusBefore, readingStatusAfter)
        assertEquals(readingStatusAfter, (readingStatusBefore + bookResponse.currentPage))
        assertTrue { bookUpdatesUpdated.bookUpdates.contains(bookUpdate) }
    }

    @Test
    fun createAndUpdateBookWithUpdateAndBookUpdatesAreSortedByDateDESC() {
        val bookDTO = BookDTO(name = "testBook", author = "Jim Carry", pagesTotal = 314, currentPage = 12)
        val bookUpdateDTO = BookUpdateInputDTO(27)
        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        val progressDTO = ProgressFileDTO(pagesSinceStart = 0, pagesEverRead = 1000)
        val bookUpdates = BookUpdatesFileDTO(mutableListOf(
                BookUpdateOutputDTO("Bugs Bunny", 152, "13/06/2018"),
                BookUpdateOutputDTO("Fire", 2, "13/06/2018"),
                BookUpdateOutputDTO("Ants", 4, "13/06/2018"),
                BookUpdateOutputDTO("House in the woods", 32, "04/10/2017"),
                BookUpdateOutputDTO("Zap", 41, "01/01/2001")))

        progressRepository.saveProgress(progressDTO)
        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val readingStatusInit = progressService.calculateReadingState()
        bookUpdateRepository.saveBookUpdate(bookUpdates)
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

        val updateResponse = bookController.updateBook(bookUpdateDTO, bookDTO.name)
        val readingStatusAfterBookUpdate = progressService.calculateReadingState()

        assertEquals(readingStatusAfterBookUpdate, (readingStatusInit + bookUpdateDTO.currentPage))
        assertEquals(updateResponse.bookUpdates[0].name, bookDTO.name)
        assertEquals(updateResponse.bookUpdates[0].pagesRead, bookUpdateDTO.currentPage)
        assertEquals(updateResponse.bookUpdates[1].name, bookUpdates.bookUpdates[0].name)
        assertEquals(updateResponse.bookUpdates[2].name, bookUpdates.bookUpdates[1].name)
        assertEquals(updateResponse.bookUpdates[3].name, bookUpdates.bookUpdates[2].name)
        assertEquals(updateResponse.bookUpdates[4].name, bookUpdates.bookUpdates[3].name)
        assertEquals(updateResponse.bookUpdates[5].name, bookUpdates.bookUpdates[4].name)
    }

//    @Test
//    fun updateBookAndBookUpdatesAreSortedByDateDESC() {
//        val bookDTO = BookDTO(name = "Zap", author = "Jim Carry", pagesTotal = 314, currentPage = 41, dateStarted = today)
//        val bookDTOAfterUpdate = BookDTO(name = "Zap", author = "Jim Carry", pagesTotal = 314, currentPage = 61, dateStarted = today)
//        val bookUpdateDTO = BookUpdateInputDTO(61)
//        val challengeDTO = ChallengeDTO(pagesPerDay = 15, pagesAheadOfPlan = 0,
//                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 1000,
//                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
//        val bookUpdates = BookUpdatesFileDTO(mutableListOf(
//                BookUpdateOutputDTO("Bugs Bunny", 152, today),
//                BookUpdateOutputDTO("Fire", 2, "13/06/2018"),
//                BookUpdateOutputDTO("Ants", 4, "13/06/2018"),
//                BookUpdateOutputDTO("House in the woods", 32, "04/10/2017"),
//                BookUpdateOutputDTO("Zap", 41, "01/01/2001")))
//
//        val challengeInit = challengeRepository.saveOrUpdateChallengeData(challengeDTO)
//        bookUpdateRepository.saveBookUpdate(bookUpdates)
//        bookRepository.saveBookIfItNotExists(bookDTO)
//        val bookResponse = bookController.updateBook(bookUpdateDTO, "Zap")
//        val challengeUpdated = challengeRepository.getChallenge()
//        val booksFile = bookRepository.getBooks()
//
//        assertNotEquals(challengeInit.pagesAheadOfPlan, challengeUpdated.pagesAheadOfPlan)
//        assertEquals(challengeUpdated.pagesEverRead, (challengeInit.pagesEverRead + 20))
//        assertEquals(booksFile.books[0], bookDTOAfterUpdate)
//        assertEquals(bookResponse.bookUpdates[0].name, "Zap")
//        assertEquals(bookResponse.bookUpdates[0].date, today)
//        assertEquals(bookResponse.bookUpdates[0].pagesRead, 20)
//        assertEquals(bookResponse.bookUpdates[1].name, bookUpdates.bookUpdates[0].name)
//        assertEquals(bookResponse.bookUpdates[2].name, bookUpdates.bookUpdates[1].name)
//        assertEquals(bookResponse.bookUpdates[3].name, bookUpdates.bookUpdates[2].name)
//        assertEquals(bookResponse.bookUpdates[4].name, bookUpdates.bookUpdates[3].name)
//        assertEquals(bookResponse.bookUpdates[5].name, bookUpdates.bookUpdates[4].name)
//    }

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

    @After
    fun removeFiles() {
        File(fileNameBook).deleteRecursively()
        File(fileNameBookUpdates).deleteRecursively()
        File(fileNameChallenge).deleteRecursively()
        File(fileNameProgress).deleteRecursively()
    }
}
