package com.bantolomeus.service

import com.bantolomeus.controller.ChallengeController
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.date.dateFormat
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeServiceIT {

    @Test
    fun updateChallenge() {

        val fileName = "testChallenge.json"
        val challengeRepository = ChallengeRepository(fileName)
        val challengeService = ChallengeService(challengeRepository)

        val challengeDTO = ChallengeDTO(pagesPerDay = 10, pagesAheadOfPlan = 0,
                startPagesAheadOfPlan = 0, pagesSinceStart = 0, pagesEverRead = 0,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 0, 1).time))
        val today = Date().time
        val daysDifference = (today - dateFormat.parse(challengeDTO.dateStarted).time) / DIVISOR_FOR_DAY

        challengeRepository.saveOrUpdateChallengeData(challengeDTO)

        challengeService.saveOrUpdateChallenge()
        val updatedChallenge = challengeService.getData()

        assertEquals("01/01/2018", updatedChallenge.dateStarted)
        assertEquals(challengeDTO.pagesPerDay, updatedChallenge.pagesPerDay)
        assertEquals(challengeDTO.startPagesAheadOfPlan, updatedChallenge.startPagesAheadOfPlan)
        assertEquals(challengeDTO.pagesSinceStart, updatedChallenge.pagesSinceStart)
        assertEquals(challengeDTO.pagesEverRead, updatedChallenge.pagesEverRead)
        assertEquals(-challengeDTO.pagesPerDay.times(daysDifference), updatedChallenge.pagesAheadOfPlan)

        File(fileName).deleteRecursively()
    }
}
