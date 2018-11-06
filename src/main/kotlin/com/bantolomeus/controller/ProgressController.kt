package com.bantolomeus.controller

import com.bantolomeus.service.ProgressService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/progress")
class ProgressController(private val progressService: ProgressService) {

    @PostMapping(path = ["/calculate"])
    fun getReadingStatus(): Long {
        return progressService.calculateReadingState()
    }
}
