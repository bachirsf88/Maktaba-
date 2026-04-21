package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                _uiState.update {
                    it.copy(
                        title = action.title,
                        titleError = null,
                        errorMessage = null
                    )
                }
            }
            is AddBookUiAction.OnIsbnChange -> {
                _uiState.update {
                    it.copy(
                        isbn = action.isbn,
                        isbnError = null,
                        errorMessage = null
                    )
                }
            }
            is AddBookUiAction.OnPagesChange -> {
                _uiState.update {
                    it.copy(
                        nbPages = action.pages,
                        pagesError = null,
                        errorMessage = null
                    )
                }
            }
            AddBookUiAction.OnAddClick -> {
                addBook()
            }
        }
    }

    private fun addBook() {
        val currentState = _uiState.value
        val titleError = if (currentState.title.isBlank()) {
            "Title is required."
        } else {
            null
        }
        val isbnError = if (currentState.isbn.isBlank()) {
            "ISBN is required."
        } else {
            null
        }
        val pagesError = when {
            currentState.nbPages.isBlank() -> null
            currentState.nbPages.toIntOrNull() == null -> "Pages must be a valid number."
            currentState.nbPages.toInt() < 0 -> "Pages cannot be negative."
            else -> null
        }

        if (titleError != null || isbnError != null || pagesError != null) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    isbnError = isbnError,
                    pagesError = pagesError,
                    errorMessage = "Please fix the highlighted fields."
                )
            }
            return
        }

        val book = Book(
            isbn = currentState.isbn.trim(),
            title = currentState.title.trim(),
            nbPages = currentState.nbPages.toIntOrNull() ?: 0
        )
        addBookUseCase(book)
        _uiState.update {
            it.copy(
                titleError = null,
                isbnError = null,
                pagesError = null,
                errorMessage = null,
                isSuccess = true
            )
        }
    }
}
