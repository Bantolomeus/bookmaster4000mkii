package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.divisorForDay
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(): ChallengeDTO {
        return challengeRepository.getChallenge()
    }

    fun saveChallenge(pages: Long?) {
        val challenge = challengeRepository.getChallenge()
        val currentTime = Date().time
        val startTime = dateFormat.parse(challenge.dateStarted).time
        val daysSinceStartNegated = -((currentTime - startTime) / divisorForDay)
        challenge.pagesAheadOfPlan = daysSinceStartNegated
                .times(challenge.pagesPerDay)
                .plus(challenge.startPagesAheadOfPlan)
                .plus(pages!!)
                .plus(challenge.pagesSinceStart)
        challenge.pagesEverRead = challenge.pagesEverRead.plus(pages)
        challenge.pagesSinceStart = challenge.pagesSinceStart.plus(pages)
        challengeRepository.saveOrUpdateChallengeData(challenge)
    }
}