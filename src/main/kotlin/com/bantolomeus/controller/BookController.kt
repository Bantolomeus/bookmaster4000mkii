package com.bantolomeus.controller

import com.bantolomeus.dto.BookDTO
import com.bantolomeus.dto.BookGetDTO
import com.bantolomeus.dto.BookUpdateInputDTO
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
    fun createBook(@Valid @RequestBody book: BookDTO) {
        bookService.createBook(book)
    }

    @PutMapping(path = ["update"])
    fun updateBook(@Valid @RequestBody bookUpdate: BookUpdateInputDTO) {
        bookService.updateBook(bookUpdate)
    }

    @GetMapping(path = ["book"])
    fun getBook(@RequestParam(value = "bookName") bookName: String): BookGetDTO {
        return bookService.getBook(bookName)
    }

    @GetMapping()
    fun getAllBooks(): List<String> {
        return bookService.getAllBooks()
    }
}
