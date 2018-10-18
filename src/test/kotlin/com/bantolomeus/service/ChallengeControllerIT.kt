package com.bantolomeus.service

import com.bantolomeus.controller.ChallengeController
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.date.dateFormat
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeControllerIT {

    private val challengeRepository = ChallengeRepository()
    private val challengeService = ChallengeService(challengeRepository)
    private val challengeController = ChallengeController(challengeService)

    @Test
    fun updateChallenge() {
        val dtoBeforeUpdate = ChallengeDTO(pagesPerDay = 15, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 0,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 4, 22).time))

        val dtoAfterUpdate = ChallengeDTO(pagesPerDay = 30, pagesAheadOfPlan = 3,
                startPagesAheadOfPlan = 524, pagesSinceStart = 442424, pagesEverRead = 4444222,
                dateStarted = dateFormat.format(GregorianCalendar(2011, 6, 30).time))

        challengeRepository.saveOrUpdateChallengeData(dtoBeforeUpdate)

        val updatedDTO = challengeController.updateChallenge(dtoAfterUpdate)

        assertEquals("30/07/2011", updatedDTO.dateStarted)
        assertEquals(dtoAfterUpdate.pagesPerDay, updatedDTO.pagesPerDay)
        assertEquals(dtoAfterUpdate.startPagesAheadOfPlan, updatedDTO.startPagesAheadOfPlan)
        assertEquals(dtoAfterUpdate.pagesSinceStart, updatedDTO.pagesSinceStart)
        assertEquals(dtoAfterUpdate.pagesEverRead, updatedDTO.pagesEverRead)
        assertEquals(dtoAfterUpdate.pagesAheadOfPlan, updatedDTO.pagesAheadOfPlan)
    }
}
