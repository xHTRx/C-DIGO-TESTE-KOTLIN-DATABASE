package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers // Adicione este import
import kotlinx.coroutines.launch

class FilmesViewModel(private val dao: FilmesDAO) : ViewModel() {

    // 1. Remova o filmesState: StateFlow

    suspend fun buscarFilmes(): List<Filmes> {
        // Usa o Dispatchers.IO para a operação de I/O de disco
        return dao.buscarTodos()
    }

    fun adicionarFilme(nome: String, descricao: String) {
        // Lança a coroutine para inserção no escopo IO
        viewModelScope.launch(Dispatchers.IO) {
            val novoFilme = Filmes(nome = nome, desc = descricao)
            dao.inserir(novoFilme)
        }
    }

    // Você pode remover o ViewModel se quiser seguir o padrão EXATO do professor
    // que usou funções suspend externas à UI.
}