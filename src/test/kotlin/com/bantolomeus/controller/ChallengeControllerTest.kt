package com.bantolomeus.controller

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.service.ChallengeService
import com.nhaarman.mockito_kotlin.given
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeControllerTest {

    @Mock
    lateinit var challengeService: ChallengeService

    @InjectMocks
    private lateinit var challengeController: ChallengeController

    @Test
    fun getChallengeData() {
        val challengeDTO = ChallengeDTO(
                pagesPerDay = 50,
                pagesAheadOfPlan = 1500,
                startPagesAheadOfPlan = 0,
                pagesSinceStart = 0,
                pagesEverRead = 429993,
                dateStarted = "05/02/2000"
        )

        given(challengeService.getData()).willReturn(challengeDTO)
        val response = challengeController.getChallengeData()

        assertEquals(challengeDTO, response)
    }
}
