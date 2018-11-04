package com.bantolomeus.service

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.repository.ChallengeRepository
import org.springframework.stereotype.Service

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    fun getData(): ChallengeDTO {
        return challengeRepository.getChallenge()
    }

    fun saveChallenge(challengeDTO: ChallengeDTO): ChallengeDTO {
        return challengeRepository.saveOrUpdateChallengeData(challengeDTO = challengeDTO)
    }
}
