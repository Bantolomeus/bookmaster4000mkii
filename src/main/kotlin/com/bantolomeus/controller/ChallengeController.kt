package com.bantolomeus.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/challenge", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class ChallengeController(private val challengeService: ChallengeService) {

    @GetMapping()
    fun getChallengeData(): Any? {
        return challengeService.getData()
    }
}