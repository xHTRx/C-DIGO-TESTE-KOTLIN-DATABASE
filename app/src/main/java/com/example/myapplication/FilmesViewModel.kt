package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FilmesViewModel(private val dao: FilmesDAO) : ViewModel() {

    // StateFlow é um tipo de Flow que armazena o estado e é ideal para Compose
    val filmesState: StateFlow<List<Filmes>> = dao.buscarTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Inicia a coleta enquanto a UI está visível
            initialValue = emptyList()
        )

    fun adicionarFilme(nome: String, descricao: String) {
        viewModelScope.launch {
            val novoFilme = Filmes(nome = nome, desc = descricao)
            dao.inserir(novoFilme)
        }
    }
}