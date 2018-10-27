package com.bantolomeus.controller

import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.BookRepository
import com.bantolomeus.repository.BookUpdatesRepository
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.service.BookService
import com.bantolomeus.service.ChallengeService
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
    private lateinit var challengeService:ChallengeService

    private lateinit var fileNameBook: String
    private lateinit var bookRepository: BookRepository
    private lateinit var fileNameBookUpdates: String
    private lateinit var bookUpdateRepository: BookUpdatesRepository
    private lateinit var bookService: BookService
    private lateinit var bookController: BookController

    @Test
    fun createBookWithCurrentPage() {
        val bookDTO = BookDTO(name = "testBook", author = "Jim Carry", pagesTotal = 314, currentPage = 12)
        val challengeDTO = ChallengeDTO(pagesPerDay = 15, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 1000,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))
        val bookUpdate = BookUpdateOutputDTO(name = "testBook", pagesRead = 12, date = today)

        val challengeInit = challengeRepository.saveOrUpdateChallengeData(challengeDTO)
        val bookResponse = bookController.createBook(bookDTO)
        val challengeUpdated = challengeRepository.getChallenge()
        val bookUpdatesUpdated = bookUpdateRepository.getBookUpdates()

        assertNotEquals(challengeInit.pagesAheadOfPlan, challengeUpdated.pagesAheadOfPlan)
        assertEquals(challengeUpdated.pagesEverRead, (challengeInit.pagesEverRead + bookResponse.currentPage))
        assertTrue { bookUpdatesUpdated.bookUpdates.contains(bookUpdate) }

        File(fileNameBook).deleteRecursively()
        File(fileNameBookUpdates).deleteRecursively()
        File(fileNameChallenge).deleteRecursively()
    }

    @Test
    fun bookUpdatesIsSortedByDateDESC() {

    }

    @Before
    fun setUpClasses() {
        today = dateFormat.format(Date())
        fileNameChallenge = "testChallenge.json"
        challengeRepository = ChallengeRepository(fileNameChallenge)
        challengeService = ChallengeService(challengeRepository)

        fileNameBook = "testBooks.json"
        bookRepository = BookRepository(fileNameBook)
        fileNameBookUpdates = "testBookUpdates.json"
        bookUpdateRepository = BookUpdatesRepository(fileNameBookUpdates)
        bookService = BookService(bookRepository, bookUpdateRepository, challengeService)
        bookController = BookController(bookService)
    }
}
