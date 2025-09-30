package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmesDAO{

    // ***** MUDANÇA AQUI: Adicionar 'suspend' *****
    @Insert
    suspend fun inserir(filme: Filmes)

    @Query("SELECT * FROM filmes")
    fun buscarTodos(): Flow<List<Filmes>>
}