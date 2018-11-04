package com.bantolomeus.controller

import com.bantolomeus.dto.ChallengeDTO
import com.bantolomeus.service.ChallengeService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/challenge", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class ChallengeController(private val challengeService: ChallengeService) {


    @GetMapping()
    fun getChallengeData(): ChallengeDTO {
        return challengeService.getChallenge()
    }

    @PostMapping()
    fun updateChallenge(@Valid @RequestBody challenge: ChallengeDTO): ChallengeDTO {
        return challengeService.saveChallenge(challenge)
    }
}
