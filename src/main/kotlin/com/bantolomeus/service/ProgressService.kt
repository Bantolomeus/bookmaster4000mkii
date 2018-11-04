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
        progress.pagesSinceStart = progress.pagesSinceStart.plus(pages)
        return progressRepository.saveProgress(progress)
    }

    fun calculateReadingState(): Long {
        val daysSinceStartNegated = -((Date().time - challengeRepository.getStartTime()) / DIVISOR_FOR_DAY)
        return daysSinceStartNegated.times(challengeRepository.getChallenge().pagesPerDay)
                                    .plus(progressRepository.getProgress().pagesSinceStart)
    }
}
