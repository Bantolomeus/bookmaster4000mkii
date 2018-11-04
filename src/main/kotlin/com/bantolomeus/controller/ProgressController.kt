package com.bantolomeus.controller

import com.bantolomeus.service.ProgressService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/progress", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class ProgressController(private val progressService: ProgressService) {

    @GetMapping()
    fun getChallengeData(): Long {
        return progressService.calculateReadingState()
    }
}
