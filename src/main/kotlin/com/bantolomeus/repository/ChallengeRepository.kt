package com.bantolomeus.repository

import com.bantolomeus.dto.ChallengeDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class ChallengeRepository(private val objectMapper: ObjectMapper) {

    fun saveOrUpdateChallengeData(challengeDTO: ChallengeDTO?, fileName: String = "challenge.json"): ChallengeDTO {
        objectMapper.writeValue(File(fileName), challengeDTO)
        return objectMapper.readValue(File(fileName), ChallengeDTO::class.java)
    }

    fun getChallenge(fileName: String = "challenge.json"): ChallengeDTO {
        return try {
            objectMapper.readValue(File(fileName), ChallengeDTO::class.java)
        } catch (e: Exception) {
            ChallengeDTO()
        }
    }
}
