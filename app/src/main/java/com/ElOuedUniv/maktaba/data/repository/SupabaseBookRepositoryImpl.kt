package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabaseBookRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BookRepository {

    override fun getAllBooks(): Flow<List<Book>> = flow {
        val books = supabaseClient.postgrest["books"]
            .select()
            .decodeList<Book>()
        emit(books)
    }

    override suspend fun getBookByIsbn(isbn: String): Book? {
        return supabaseClient.postgrest["books"]
            .select {
                filter {
                    eq("isbn", isbn)
                }
            }
            .decodeSingleOrNull<Book>()
    }

    override suspend fun addBook(book: Book) {
        supabaseClient.postgrest["books"].insert(book)
    }
}
