package com.bantolomeus.translator

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.date.dateFormat
import java.util.*

fun BookDTO.toBookUpdateDTO(): BookUpdateOutputDTO = BookUpdateOutputDTO(name, currentPage, dateFormat.format(Date()))
