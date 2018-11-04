package com.bantolomeus.controller

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.date.dateFormat
import com.bantolomeus.service.ChallengeService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeControllerIT {

    private lateinit var challengeFile: String

    @Test
    fun updateChallenge() {
        val challengeRepository = ChallengeRepository(challengeFile)
        val challengeService = ChallengeService(challengeRepository)
        val challengeController = ChallengeController(challengeService)

        val dtoBeforeUpdate = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))

        val dtoAfterUpdate = ChallengeDTO(pagesPerDay = 30,
                dateStarted = dateFormat.format(GregorianCalendar(2011, 6, 30).time))

        challengeRepository.saveOrUpdateChallengeData(dtoBeforeUpdate)
        val challengeAfterSave = challengeRepository.getChallenge()
        assertEquals(challengeAfterSave.pagesPerDay, dtoBeforeUpdate.pagesPerDay)
        assertEquals(challengeAfterSave.dateStarted, dtoBeforeUpdate.dateStarted)

        val updatedDTO = challengeController.updateChallenge(dtoAfterUpdate)

        assertEquals("30/07/2011", updatedDTO.dateStarted)
        assertEquals(dtoAfterUpdate.pagesPerDay, updatedDTO.pagesPerDay)
    }

    @Test
    fun getChallenge() {
        val challengeRepository = ChallengeRepository(challengeFile)
        val challengeService = ChallengeService(challengeRepository)
        val challengeController = ChallengeController(challengeService)

        val challengeDTO = ChallengeDTO(pagesPerDay = 15,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))

        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val savedChallenge = challengeController.getChallengeData()

        assertEquals("22/05/2018", savedChallenge.dateStarted)
        assertEquals(challengeDTO.pagesPerDay, savedChallenge.pagesPerDay)
    }

    @Before
    fun createFile() {
        challengeFile = "testChallenge.json"
    }

    @After
    fun removeFiles() {
        File(challengeFile).deleteRecursively()
    }
}
