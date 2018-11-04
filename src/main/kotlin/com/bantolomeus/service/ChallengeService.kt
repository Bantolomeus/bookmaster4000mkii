package com.bantolomeus.service

import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(): ChallengeDTO {
        return challengeRepository.getChallenge()
    }

    // TODO: remove this method after pagesAheadOfPlan is moved elsewhere
    fun saveOrUpdateChallenge(pages: Long = 0): ChallengeDTO {
        val challenge = challengeRepository.getChallenge()
        val startTime = dateFormat.parse(challenge.dateStarted).time
        val daysSinceStartNegated = -((Date().time - startTime) / DIVISOR_FOR_DAY)
        challenge.pagesAheadOfPlan = daysSinceStartNegated
                .times(challenge.pagesPerDay)
                .plus(challenge.startPagesAheadOfPlan)
                .plus(pages)
                .plus(challenge.pagesSinceStart)
        return challengeRepository.saveOrUpdateChallengeData(challengeDTO = challenge)
    }

    // TODO: rename this to saveOrUpdateChallenge
    fun updateChallenge(challengeDTO: ChallengeDTO): ChallengeDTO {
        return challengeRepository.saveOrUpdateChallengeData(challengeDTO = challengeDTO)
    }
}
