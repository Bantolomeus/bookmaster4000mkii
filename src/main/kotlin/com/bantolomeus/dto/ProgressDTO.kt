package com.bantolomeus.dto

data class ProgressUpdateDTO(val date: String = "",
                             val pagesRead: Long = 0)

data class ProgressFileDTO(var pagesSinceStart: Long = 0,
                           var pagesEverRead: Long = 0)
