package com.bantolomeus.service

import com.bantolomeus.dto.ProgressFileDTO
import com.bantolomeus.repository.ChallengeRepository
import com.bantolomeus.repository.ProgressRepository
import org.springframework.stereotype.Service

@Service
class ProgressService(private val progressRepository: ProgressRepository,
                      private val challengeRepository: ChallengeRepository) {

    fun saveProgress(pages: Long): ProgressFileDTO {
        val progress = progressRepository.getProgress()
        progress.pagesEverRead = progress.pagesEverRead.plus(pages)
        progress.pagesSinceStart = progress.pagesSinceStart.plus(pages)
        return progressRepository.saveProgress(progress)
    }

    // TODO: implement this (pagesAheadOfPlan so far)
    fun calculateReadingState(): Long {
        return 0
    }
}
