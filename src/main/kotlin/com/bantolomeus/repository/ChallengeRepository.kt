package com.bantolomeus.repository

import com.bantolomeus.dto.ChallengeDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class ChallengeRepository(private val objectMapper: ObjectMapper) {

    fun saveChallengeData(challengeDTO: ChallengeDTO?) {
        objectMapper.writeValue(File("challenge.json"), challengeDTO)
    }

    fun getChallenge(): ChallengeDTO? {
        return try {
            objectMapper.readValue(File("challenge.json"), ChallengeDTO::class.java)
        } catch (e: Exception) {
            ChallengeDTO()
        }
    }
}