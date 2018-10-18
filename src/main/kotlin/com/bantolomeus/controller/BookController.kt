package com.bantolomeus.controller

import com.bantolomeus.dto.*
import com.bantolomeus.service.BookService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/books", produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class BookController(private val bookService: BookService) {

    @PostMapping
    fun createBook(@Valid @RequestBody book: BookDTO) {
        bookService.createBook(book)
    }

    @PutMapping(path = ["{bookName}"])
    fun updateBook(@Valid @RequestBody bookUpdate: BookUpdateInputDTO, @PathVariable("bookName") bookName: String):
            BooksUpdatesFileDTO {
        return bookService.updateBook(bookUpdate, bookName)
    }

    @GetMapping(path = ["{bookName}"])
    fun getBookWithUpdates(@PathVariable("bookName") bookName: String): BookGetDTO {
        return bookService.getBookWithUpdates(bookName)
    }

    @GetMapping()
    fun getAllBookNames(): List<String> {
        return bookService.getAllBookNames()
    }

    @GetMapping(path = ["/sortBookUpdates"])
    fun sortBookUpdates(): BooksUpdatesFileDTO {
        return bookService.sortBookUpdates()
    }
}
