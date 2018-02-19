package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.divisorDay
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(): ChallengeDTO {
        return challengeRepository.getChallenge()
    }

    fun saveChallenge(pages: Long?) {
        val challenge = challengeRepository.getChallenge()
        challenge.pagesAheadOfPlan = (-((Date().time - dateFormat.parse(challenge.dateStarted).time)/ divisorDay))
                .times(challenge.pagesPerDay)
                .plus(challenge.startPagesAheadOfPlan)
                .plus(pages!!)
                .plus(challenge.pagesSinceStart)
        challenge.pagesEverRead = challenge.pagesEverRead.plus(pages)
        challenge.pagesSinceStart = challenge.pagesSinceStart.plus(pages)
        challengeRepository.saveOrUpdateChallengeData(challenge)
    }
}