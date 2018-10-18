package com.bantolomeus.repository

import com.bantolomeus.dto.ChallengeDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val CHALLENGE_FILE = "challenge.json"

@Repository
class ChallengeRepository {
    private val objectMapper = ObjectMapper()

    fun saveOrUpdateChallengeData(challengeDTO: ChallengeDTO?, fileName: String = CHALLENGE_FILE): ChallengeDTO {
        objectMapper.writeValue(File(fileName), challengeDTO)
        return objectMapper.readValue(File(fileName), ChallengeDTO::class.java)
    }

    fun getChallenge(fileName: String = CHALLENGE_FILE): ChallengeDTO {
        return try {
            objectMapper.readValue(File(fileName), ChallengeDTO::class.java)
        } catch (e: Exception) {
            ChallengeDTO()
        }
    }
}
