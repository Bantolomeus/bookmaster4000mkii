package com.bantolomeus.controller

import com.bantolomeus.service.ProgressService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/progress")
class ProgressController(private val progressService: ProgressService) {

    @GetMapping()
    fun getReadingStatus(): Long {
        return progressService.calculateReadingState()
    }
}
