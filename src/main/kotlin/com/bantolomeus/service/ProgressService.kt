package com.bantolomeus.service

import com.bantolomeus.date.DIVISOR_FOR_DAY
import com.bantolomeus.dto.ProgressFileDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.repository.ProgressRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProgressService(private val progressRepository: ProgressRepository,
                      private val challengeRepository: ChallengeRepository) {

    fun saveProgress(pages: Long): ProgressFileDTO {
        val progress = progressRepository.getProgress()
        progress.pagesEverRead = progress.pagesEverRead.plus(pages)
        progress.pagesReadInCurrentChallenge = progress.pagesReadInCurrentChallenge.plus(pages)
        return progressRepository.saveProgress(progress)
    }

    fun calculateReadingState(): Long {
        val daysSinceStart = ((Date().time - challengeRepository.getStartTime()) / DIVISOR_FOR_DAY)
        val pagesPerDay = challengeRepository.getChallenge().pagesPerDay
        val pagesSinceStart = progressRepository.getProgress().pagesReadInCurrentChallenge
        return (-daysSinceStart).times(pagesPerDay).plus(pagesSinceStart)
    }
}
