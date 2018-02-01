package com.bantolomeus.translator

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import com.bantolomeus.util.dateFormat
import java.util.*



fun BookDTO.toBookUpdateDTO(): BookUpdateDTO = BookUpdateDTO(name, pagesRead!!, dateFormat.format(Date()))