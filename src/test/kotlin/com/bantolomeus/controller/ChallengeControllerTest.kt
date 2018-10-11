package com.bantolomeus.controller

import com.bantolomeus.service.ChallengeService
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ChallengeControllerTest {

    @Mock
    lateinit var challengeService: ChallengeService

    @InjectMocks
    private lateinit var challengeController: ChallengeController

    @Test
    fun getChallengeData() {
        challengeController.getChallengeData()
        verify(challengeService).saveOrUpdateChallenge()
    }
}
