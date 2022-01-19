package com.bantolomeus.controller

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.service.ChallengeService
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/challenge", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class ChallengeController(private val challengeService: ChallengeService) {

    @GetMapping()
    fun getChallengeData(): ChallengeDTO {
        return challengeService.getChallenge()
    }

    @PostMapping()
    fun updateChallenge(@Validated @RequestBody challenge: ChallengeDTO): ChallengeDTO {
        return challengeService.saveChallenge(challenge)
    }
}
