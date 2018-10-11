package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.util.dateFormat
import com.bantolomeus.util.DIVISOR_FOR_DAY
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(fileName: String = "challenge.json"): ChallengeDTO {
        return challengeRepository.getChallenge(fileName)
    }

    fun saveOrUpdateChallenge(pages: Long = 0, fileName: String = "challenge.json"): ChallengeDTO {
        val challenge = challengeRepository.getChallenge(fileName = fileName)
        val currentTime = Date().time
        val startTime = dateFormat.parse(challenge.dateStarted).time
        val daysSinceStartNegated = -((currentTime - startTime) / DIVISOR_FOR_DAY)
        challenge.pagesAheadOfPlan = daysSinceStartNegated
                .times(challenge.pagesPerDay)
                .plus(challenge.startPagesAheadOfPlan)
                .plus(pages)
                .plus(challenge.pagesSinceStart)
        challenge.pagesEverRead = challenge.pagesEverRead.plus(pages)
        challenge.pagesSinceStart = challenge.pagesSinceStart.plus(pages)
        return challengeRepository.saveOrUpdateChallengeData(challengeDTO = challenge, fileName = fileName)
    }

    fun updateChallenge(challengeDTO: ChallengeDTO, challengeFile: String = "challenge.json"): ChallengeDTO {
        return challengeRepository.saveOrUpdateChallengeData(challengeDTO = challengeDTO, fileName = challengeFile)
    }
}
