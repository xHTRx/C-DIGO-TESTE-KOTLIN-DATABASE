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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val dao = db.filmesDAO() // Obtenha o DAO


        setContent {
            BancoOsorioTheme {
                // Instancia o ViewModel, passando o DAO
                val viewModel: FilmesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            // VERIFICAÇÃO DE TIPO MELHORADA
                            if (modelClass.isAssignableFrom(FilmesViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                return FilmesViewModel(dao) as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                )

                // Pega a lista de filmes do StateFlow
                val listaFilmes by viewModel.filmesState.collectAsState()

                FilmesScreen(
                    filmes = listaFilmes,
                    onAdicionarClick = viewModel::adicionarFilme // Passa a função de adição
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BancoOsorioTheme {
        Greeting("Android")
    }
}

@Composable
fun FilmesScreen(
    filmes: List<Filmes>,
    onAdicionarClick: (String, String) -> Unit
) {
    Scaffold(
        topBar = { Text("Lista de Filmes") },
        floatingActionButton = {
            // Exemplo simples: insere um novo filme ao clicar no FAB
            Button(onClick = {
                onAdicionarClick("Novo Filme ${filmes.size + 1}", "Descrição teste")
            }) {
                Text("Adicionar Filme")
            }
        }
    ) { paddingValues ->
        // Usa LazyColumn para lidar com listas grandes de forma eficiente
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(filmes) { filme ->
                // Item de lista para cada filme
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = filme.nome, style = MaterialTheme.typography.titleMedium)
                    Text(text = filme.desc, style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
            if (filmes.isEmpty()) {
                item {
                    Text(
                        "Nenhum filme encontrado. Adicione um!",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}