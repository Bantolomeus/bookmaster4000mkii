package com.bantolomeus.translator

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

fun BookDTO.toBookUpdateDTO(): BookUpdateDTO = BookUpdateDTO(name, pagesRead!!, dateFormat.parse(dateFormat.format(Date())))