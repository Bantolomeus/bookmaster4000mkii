package com.bantolomeus.service

import com.bantolomeus.controller.ChallengeController
import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.date.dateFormat
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeControllerIT {
    @Test
    fun updateChallenge() {
        val fileName = "testChallenge.json"
        val challengeRepository = ChallengeRepository(fileName)
        val challengeService = ChallengeService(challengeRepository)
        val challengeController = ChallengeController(challengeService)

        val dtoBeforeUpdate = ChallengeDTO(pagesPerDay = 15, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 0,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))

        val dtoAfterUpdate = ChallengeDTO(pagesPerDay = 30, pagesAheadOfPlan = 3,
                startPagesAheadOfPlan = 524, pagesSinceStart = 442424, pagesEverRead = 4444222,
                dateStarted = dateFormat.format(GregorianCalendar(2011, 6, 30).time))

        challengeRepository.saveOrUpdateChallengeData(dtoBeforeUpdate)
        val challengeAfterSave = challengeRepository.getChallenge()
        assertEquals(challengeAfterSave.pagesAheadOfPlan, dtoBeforeUpdate.pagesAheadOfPlan)
        assertEquals(challengeAfterSave.pagesEverRead, dtoBeforeUpdate.pagesEverRead)
        assertEquals(challengeAfterSave.pagesSinceStart, dtoBeforeUpdate.pagesSinceStart)
        assertEquals(challengeAfterSave.pagesPerDay, dtoBeforeUpdate.pagesPerDay)
        assertEquals(challengeAfterSave.startPagesAheadOfPlan, dtoBeforeUpdate.startPagesAheadOfPlan)
        assertEquals(challengeAfterSave.dateStarted, dtoBeforeUpdate.dateStarted)

        val updatedDTO = challengeController.updateChallenge(dtoAfterUpdate)

        assertEquals("30/07/2011", updatedDTO.dateStarted)
        assertEquals(dtoAfterUpdate.pagesPerDay, updatedDTO.pagesPerDay)
        assertEquals(dtoAfterUpdate.startPagesAheadOfPlan, updatedDTO.startPagesAheadOfPlan)
        assertEquals(dtoAfterUpdate.pagesSinceStart, updatedDTO.pagesSinceStart)
        assertEquals(dtoAfterUpdate.pagesEverRead, updatedDTO.pagesEverRead)
        assertEquals(dtoAfterUpdate.pagesAheadOfPlan, updatedDTO.pagesAheadOfPlan)

        File(fileName).deleteRecursively()
    }

    @Test
    fun getChallenge() {

        val fileName = "testChallenge.json"
        val challengeRepository = ChallengeRepository(fileName)
        val challengeService = ChallengeService(challengeRepository)
        val challengeController = ChallengeController(challengeService)

        val challengeDTO = ChallengeDTO(pagesPerDay = 15, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 0,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))

        val today = Date().time
        val daysDifference = (today - dateFormat.parse(challengeDTO.dateStarted).time) / DIVISOR_FOR_DAY

        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        val updatedDTO = challengeController.getChallengeData()

        assertEquals("22/05/2018", updatedDTO.dateStarted)
        assertEquals(challengeDTO.pagesPerDay, updatedDTO.pagesPerDay)
        assertEquals(challengeDTO.startPagesAheadOfPlan, updatedDTO.startPagesAheadOfPlan)
        assertEquals(challengeDTO.pagesSinceStart, updatedDTO.pagesSinceStart)
        assertEquals(challengeDTO.pagesEverRead, updatedDTO.pagesEverRead)
        assertEquals(-challengeDTO.pagesPerDay.times(daysDifference), updatedDTO.pagesAheadOfPlan)

        File(fileName).deleteRecursively()
    }
}
