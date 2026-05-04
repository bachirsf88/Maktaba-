package com.ElOuedUniv.maktaba.presentation.book.add

import android.net.Uri

data class AddBookUiState(
    val title: String = "",
    val isbn: String = "",
    val nbPages: String = "",
    val titleError: String? = null,
    val isbnError: String? = null,
    val pagesError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val selectedImageUri: Uri? = null
)
