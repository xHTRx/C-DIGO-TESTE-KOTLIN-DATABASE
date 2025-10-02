package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FilmesDAO{

    @Insert
    suspend fun inserir(filme: Filmes)

    @Query("SELECT * FROM filmes")
    suspend fun buscarTodos(): List<Filmes>
}