package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.BancoOsorioTheme
import androidx.lifecycle.ViewModel // Novo import
import androidx.lifecycle.ViewModelProvider // Novo import
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp

// NOVO CÓDIGO DA MAINACTIVITY SIMPLIFICADA

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Não precisa de ViewModelProvider aqui
            BancoOsorioTheme {
                // Chama a tela principal
                TelaCadastroFilmesSimplificada()
            }
        }
    }
}

// A função externa que insere o filme (como o professor fez)
suspend fun inserirFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
    // ... (corpo da função de inserção do professor) ...
    filmesDao.inserir(Filmes(nome = nome, desc = desc))
}

// A função externa que busca os filmes (como o professor fez)
suspend fun buscarFilmes(filmesDao: FilmesDAO): List<Filmes> {
    return filmesDao.buscarTodos()
}


@Composable
fun TelaCadastroFilmesSimplificada() {
    // 1. ESTADO: Gerencia a lista localmente
    var filmes by remember { mutableStateOf<List<Filmes>>(emptyList()) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val filmesDao = db.filmesDAO()

    // 2. EFEITO: Carrega os dados na inicialização
    LaunchedEffect(Unit) {
        filmes = buscarFilmes(filmesDao)
    }

    // Lógica para recarregar a lista após uma inserção
    fun recarregarFilmes() {
        CoroutineScope(Dispatchers.Main).launch {
            filmes = buscarFilmes(filmesDao)
        }
    }

    Scaffold(
        topBar = { Text("Lista de Filmes") },
        floatingActionButton = {
            Button(onClick = {
                // 3. AÇÃO: Executa a inserção em IO, e RECARREGA a lista (no Main)
                CoroutineScope(Dispatchers.IO).launch {
                    inserirFilme("Novo Filme ${filmes.size + 1}", "Descrição teste", filmesDao)
                    recarregarFilmes() // Chama a recarga para atualizar o estado
                }
            }) {
                Text("Adicionar Filme")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(filmes) { filme ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = filme.nome, style = MaterialTheme.typography.titleMedium)
                    // ...
                }
            }
        }
    }
}