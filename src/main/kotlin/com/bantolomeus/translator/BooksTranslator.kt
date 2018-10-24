package com.bantolomeus.translator

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.date.dateFormat
import com.bantolomeus.dto.ProgressUpdateDTO
import java.util.*

fun BookDTO.toBookUpdateDTO(): BookUpdateOutputDTO = BookUpdateOutputDTO(name, currentPage, dateFormat.format(Date()))

fun BookDTO.toProgressUpdateDTO(): ProgressUpdateDTO = ProgressUpdateDTO(dateFormat.format(Date()), currentPage)
