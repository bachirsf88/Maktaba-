package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * Repository for managing book data
 * This follows the Repository pattern to abstract data sources
 */
class BookRepository {

    /**
     * TODO for Students (TP1 - Exercise 1):
     * Complete the book information for each book in the list below.
     * Add the following information for each book:
     * - isbn: Use a valid ISBN-13 format (e.g., "978-3-16-148410-0")
     * - nbPages: Add the actual number of pages
     *
     * Example:
     * Book(
     *     isbn = "978-0-13-468599-1",
     *     title = "Clean Code",
     *     nbPages = 464
     * )
     */
    private val booksList = listOf(
        Book(
            isbn = "978-0-13-235088-4",
            title = "Clean Code",
            nbPages = 464
        ),

        Book(
            isbn = "978-0-13-595705-9",
            title = "The Pragmatic Programmer",
            nbPages = 352
        ),

        Book(
            isbn = "978-0-13-475771-1",
            title = "Refactoring: Improving the Design of Existing Code",
            nbPages = 448
        ),

        Book(
            isbn = "978-0-201-63361-0",
            title = "Design Patterns: Elements of Reusable Object-Oriented Software",
            nbPages = 416
        ),

        Book(
            isbn = "978-0-262-03384-8",
            title = "Introduction to Algorithms",
            nbPages = 1312
        ),


        // --- ------------------
        Book(
            isbn = "978-0-13-468599-1",
            title = "Effective Java",
            nbPages = 416
        ),

        Book(
            isbn = "978-1-61729-960-5",
            title = "Kotlin in Action, Second Edition",
            nbPages = 560
        ),

        Book(
            isbn = "978-0-13-525756-2",
            title = "Android Programming: The Big Nerd Ranch Guide",
            nbPages = 624
        ),

        Book(
            isbn = "978-0-13-449416-6",
            title = "Clean Architecture",
            nbPages = 432
        ),

        Book(
            isbn = "978-0-13-110362-7",
            title = "The C Programming Language",
            nbPages = 272
        )
    )

    /**
     * TODO for Students (TP1 - Exercise 2):
     * Add 5 more books to the list above.
     * Choose books related to Computer Science, Programming, or any topic you like.
     * Remember to include complete information (ISBN, title, nbPages).
     *
     * Tip: You can find ISBN numbers for books on:
     * - Google Books
     * - Amazon
     * - GoodReads
     */

    /**
     * Get all books from the repository
     * @return List of all books
     */
    fun getAllBooks(): List<Book> {
        return booksList
    }

    /**
     * Get a book by ISBN
     * @param isbn The ISBN of the book to find
     * @return The book if found, null otherwise
     */
    fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }
}
