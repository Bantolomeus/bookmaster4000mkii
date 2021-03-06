package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.date.dateFormat
import com.nhaarman.mockito_kotlin.given
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class ChallengeServiceTest {

    @Mock
    private lateinit var challengeRepository: ChallengeRepository

    @InjectMocks
    private lateinit var challengeService: ChallengeService

    @Test
    fun getData() {
        val challengeDTO = ChallengeDTO(pagesPerDay = 20,
                dateStarted = dateFormat.format(GregorianCalendar(2018, 1, 1).time))

        given(challengeRepository.getChallenge()).willReturn(challengeDTO)
        val challengeData = challengeService.getChallenge()

        assertEquals(challengeDTO, challengeData)
    }
}
