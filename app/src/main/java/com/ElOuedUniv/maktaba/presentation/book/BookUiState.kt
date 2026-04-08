package com.ElOuedUniv.maktaba.presentation.book

import com.ElOuedUniv.maktaba.data.model.Book

data class BookUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val isAddingBook: Boolean = false
)
