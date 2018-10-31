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
    fun createBook(@Valid @RequestBody book: BookDTO): BookDTO {
        return bookService.createBook(book)
    }

    @PutMapping(path = ["{bookName}"])
    fun updateBook(@Valid @RequestBody bookUpdate: BookUpdateInputDTO, @PathVariable("bookName") bookName: String):
            BookUpdatesFileDTO {
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

    @GetMapping(path = ["{bookName}/pagesLeft"])
    fun pagesLeft(@PathVariable("bookName") bookName: String): Long {
        return bookService.pagesLeft(bookName)
    }

    @GetMapping(path = ["/sortBookUpdates"])
    fun sortBookUpdates(): BookUpdatesFileDTO {
        return bookService.sortBookUpdates()
    }
}
