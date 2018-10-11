package com.bantolomeus.service

import com.bantolomeus.controller.ChallengeController
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.util.DIVISOR_FOR_DAY
import com.bantolomeus.util.dateFormat
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeServiceIT {

    private val challengeRepository = ChallengeRepository(ObjectMapper())
    private val challengeService = ChallengeService(challengeRepository)
    private val challengeController = ChallengeController(challengeService)

    @Test
    fun updateChallenge() {
        val fileName = "testChallenge.json"
        val challengeDTO = ChallengeDTO(pagesPerDay = 10, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 0,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 0, 1).time))
        val today = Date().time
        val daysDifference = (today - dateFormat.parse(challengeDTO.dateStarted).time) / DIVISOR_FOR_DAY

        challengeRepository.saveOrUpdateChallengeData(challengeDTO, fileName = fileName)

        challengeService.saveOrUpdateChallenge(fileName = fileName)
        val updatedChallenge = challengeService.getData(fileName)

        assertEquals("01/01/2018", updatedChallenge.dateStarted)
        assertEquals(challengeDTO.pagesPerDay, updatedChallenge.pagesPerDay)
        assertEquals(challengeDTO.startPagesAheadOfPlan, updatedChallenge.startPagesAheadOfPlan)
        assertEquals(challengeDTO.pagesSinceStart, updatedChallenge.pagesSinceStart)
        assertEquals(challengeDTO.pagesEverRead, updatedChallenge.pagesEverRead)
        assertEquals(-challengeDTO.pagesPerDay.times(daysDifference), updatedChallenge.pagesAheadOfPlan)
    }

    @Test
    fun getChallenge() {
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
    }
}
