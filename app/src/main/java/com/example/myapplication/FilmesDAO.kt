package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete // Adicionar este import
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update // Adicionar este import

@Dao
interface FilmesDAO{

    @Insert
    suspend fun inserir(filme: Filmes)

    @Query("SELECT * FROM filmes")
    suspend fun buscarTodos(): List<Filmes>

    @Delete
    suspend fun deletar(filmes: Filmes) // <--- Adicionar esta função

    @Update
    suspend fun atualizar(filmes: Filmes) // <--- Adicionar esta função
}