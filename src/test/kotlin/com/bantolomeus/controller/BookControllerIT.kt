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

    @Test
    fun createBookWithCurrentPage() {
        val today = dateFormat.format(Date())
        val fileNameChallenge = "testChallenge.json"
        val challengeRepository = ChallengeRepository(fileNameChallenge)
        val challengeService = ChallengeService(challengeRepository)

        val fileNameBook = "testBooks.json"
        val bookRepository = BookRepository(fileNameBook)
        val fileNameBookUpdates = "testBookUpdates.json"
        val bookUpdateRepository = BookUpdatesRepository(fileNameBookUpdates)
        val bookService = BookService(bookRepository, bookUpdateRepository, challengeService)
        val bookController = BookController(bookService)

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
}
