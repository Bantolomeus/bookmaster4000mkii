package com.bantolomeus.controller

import com.bantolomeus.dto.*
import com.bantolomeus.service.BookService
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class BookController(private val bookService: BookService) {

    @PostMapping
    fun createBook(@Validated @RequestBody book: BookDTO): BookDTO {
        return bookService.createBook(book)
    }

    @PutMapping(path = ["{bookName}"])
    fun updateBook(@Validated @RequestBody bookUpdate: BookUpdateInputDTO, @PathVariable("bookName") bookName: String):
            BookUpdatesFileDTO {
        return bookService.updateBook(bookUpdate, bookName)
    }

    @GetMapping(path = ["{bookName}"])
    fun getBookWithUpdates(@PathVariable("bookName") bookName: String): BookGetDTO {
        return bookService.getBookWithUpdates(bookName)
    }

    @GetMapping()
    fun getAllBooks(): List<BookDTO> {
        return bookService.getAllBooks()
    }

    @GetMapping(path = ["/sortBookUpdates"])
    fun sortBookUpdates(): BookUpdatesFileDTO {
        return bookService.sortBookUpdates()
    }
}
