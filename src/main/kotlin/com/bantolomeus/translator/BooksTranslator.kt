package com.bantolomeus.translator

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateOutputDTO
import com.bantolomeus.util.dateFormat
import java.util.*


fun BookDTO.toBookUpdateDTO(): BookUpdateOutputDTO = BookUpdateOutputDTO(name, currentPage, dateFormat.format(Date()))