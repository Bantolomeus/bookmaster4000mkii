package com.bantolomeus.controller

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookUpdateDTO
import com.bantolomeus.service.BookService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/books", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class BookController(private val bookService: BookService) {

    @PostMapping
    fun createBook(@Valid @RequestBody book: BookDTO): Any {
        bookService.createBook(book)
        return "saved"
    }

    @PutMapping(path = ["update"])
    fun updateBook(@Valid @RequestBody bookUpdate: BookUpdateDTO): Any {
        bookService.updateBook(bookUpdate)
        return "updated"
    }

    @GetMapping()
    fun getBook(@RequestParam(value = "allBooks", required = false) allBooks: Boolean,
                @RequestParam(value = "bookName", required = false) bookName: String): Any? {
        return bookService.getBook(allBooks, bookName)
    }
}