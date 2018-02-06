package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import org.springframework.stereotype.Service

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(): ChallengeDTO? {
        return challengeRepository.getChallenge()
    }

    fun savePagesEverRead(pages: Long?) {
        val challenge = challengeRepository.getChallenge()
        challenge?.pagesEverRead = challenge?.pagesEverRead!!.plus(pages!!)
        challengeRepository.saveChallengeData(challenge)
    }

}