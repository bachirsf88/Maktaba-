package com.ElOuedUniv.maktaba.presentation.book.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val supabaseClient: SupabaseClient,
    @param:ApplicationContext private val context: Context
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
            is AddBookUiAction.OnCoverChange -> {
                _uiState.update {
                    it.copy(selectedImageUri = action.uri)
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

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                var imageUrl: String? = null
                val selectedImageUri = currentState.selectedImageUri
                if (selectedImageUri != null) {
                    val coverBytes = readImageBytes(selectedImageUri)
                    val fileName = "${currentState.isbn}_${UUID.randomUUID()}.jpg"
                    supabaseClient.storage["book_covers"].upload(fileName, coverBytes)
                    imageUrl = supabaseClient.storage["book_covers"].publicUrl(fileName)
                }

                val book = Book(
                    isbn = currentState.isbn.trim(),
                    title = currentState.title.trim(),
                    nbPages = currentState.nbPages.toIntOrNull() ?: 0,
                    imageUrl = imageUrl
                )
                
                addBookUseCase(book)
                _uiState.update {
                    it.copy(
                        titleError = null,
                        isbnError = null,
                        pagesError = null,
                        errorMessage = null,
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun readImageBytes(uri: Uri): ByteArray {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: error("Unable to read the selected image.")
    }
}
