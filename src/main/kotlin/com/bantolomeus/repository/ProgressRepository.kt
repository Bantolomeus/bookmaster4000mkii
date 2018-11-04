package com.bantolomeus.repository

import com.bantolomeus.dto.ProgressFileDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.io.File

const val PROGRESS_FILE = "progress.json"

@Repository
class ProgressRepository(private val progressFile: String = PROGRESS_FILE) {
    private val objectMapper = ObjectMapper()

    fun saveProgress(progress: ProgressFileDTO): ProgressFileDTO {
        objectMapper.writeValue(File(progressFile), progress)
        return progress
    }

    fun getProgress(): ProgressFileDTO {
        return try {
            objectMapper.readValue(File(progressFile), ProgressFileDTO::class.java)
        } catch (e: Exception) {
            ProgressFileDTO()
        }
    }
}
