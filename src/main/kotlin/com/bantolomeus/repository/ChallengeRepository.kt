package com.bantolomeus.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository

@Repository
class ChallengeRepository(private val objectMapper: ObjectMapper) {

}