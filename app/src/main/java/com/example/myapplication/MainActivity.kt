package com.example.myapplication

// Imports de Compose... (Estão corretos e completos)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
// Imports do professor para o formulário
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
// Log e Coroutines
import android.util.Log
import androidx.compose.foundation.clickable // Import necessário para ícones clicáveis
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold {
                TelaCadastroFilmesSimplificada(Modifier.padding(it))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaCadastroFilmesPreview() {
    // Para o Preview, você não pode chamar TelaCadastroFilmesSimplificada diretamente,
    // pois ela usa LocalContext para o banco de dados.
    // Vamos criar uma Composable separada que contenha a estrutura básica.

    // A solução mais simples é criar uma cópia da estrutura UI,
    // mas chamando um estado simulado.

    // SIMULAÇÃO DO ESTADO INICIAL
    // Como a TelaCadastroFilmesSimplificada tem acesso ao banco de dados no seu corpo,
    // o Preview vai falhar.

    // SOLUÇÃO: Recriar a estrutura da UI sem a lógica de acesso a dados.

    MaterialTheme {
        Scaffold { paddingValues ->
            // Chamamos a função principal, mas note que o acesso ao banco de dados
            // dentro dela pode causar falhas no Preview se o AS não conseguir
            // ignorar as chamadas a LocalContext/Room.

            // Na maioria dos casos, o Android Studio consegue rodar o Preview
            // ignorando as funções que falhariam (como o LaunchedEffect do DB).

            // Se falhar, use esta versão SIMPLIFICADA (que o professor costuma usar):

            Column(Modifier.padding(paddingValues)) {
                // Simula a UI do formulário (que é a parte visual principal)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color.LightGray, RectangleShape),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        TextField(value = "Filme de Teste", onValueChange = {}, label = { Text("Nome do Filme") })
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = "Descrição Teste", onValueChange = {}, label = { Text("Descricao") })
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {}) { Text("Adicionar Filme") }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simula a lista com alguns itens de teste
                LazyColumn {
                    item { UmFilme(

                        Filmes(1, "Teste A", "Desc 1"),
                        displayIndex = 1, // <--- ADICIONADO: Int para índice
                        {}, {}) }
                    item { UmFilme(
                        Filmes(2, "Teste B", "Desc 2"),
                        displayIndex = 2, // <--- ADICIONADO: Int para índice
                        {}, {}) }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UmFilmePreview() {
    MaterialTheme {
        UmFilme(
            filme = Filmes(id = 1, nome = "O Expresso Polar", desc = "Aventura"),
            onEditClick = {}, // Passa lambdas vazias
            onDeleteClick = {},
            displayIndex = 1,
        )
    }
}

// -------------------------------------------------------------------------
// FUNÇÕES SUSPEND DE CRUD
// -------------------------------------------------------------------------

suspend fun deletarFilme(filme: Filmes, filmesDao: FilmesDAO) {
    try {
        filmesDao.deletar(filme)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Msg: ${e.message}")
    }
}

suspend fun atualizarFilme(filme: Filmes, filmesDao: FilmesDAO) {
    try {
        filmesDao.atualizar(filme)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Msg: ${e.message}")
    }
}

suspend fun inserirFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
    try {
        filmesDao.inserir(Filmes(nome = nome, desc = desc))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun buscarFilmes(filmesDao: FilmesDAO): List<Filmes> {
    return try {
        filmesDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

// -------------------------------------------------------------------------
// COMPOSABLE UM FILME (COM CALLBACKS)
// -------------------------------------------------------------------------

// -------------------------------------------------------------------------
// COMPOSABLE UM FILME (COM CALLBACKS)
// -------------------------------------------------------------------------

@Composable
fun UmFilme(
    filme: Filmes, // Objeto Filmes completo
    displayIndex: Int, // <--- NOVO PARÂMETRO: O índice sequencial para exibição
    onEditClick: (Filmes) -> Unit, // Callback para Edição
    onDeleteClick: (Filmes) -> Unit // Callback para Exclusão
){
    Card(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$displayIndex", style = MaterialTheme.typography.bodyMedium)

            Column(
                // Usa um weight para que o texto ocupe o espaço restante
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text(text = filme.nome, style = MaterialTheme.typography.titleLarge)
                Text(text = filme.desc, style = MaterialTheme.typography.titleSmall)
            }

            // ÍCONE DE EDITAR: CLICÁVEL
            Icon(
                Icons.Default.Edit,
                "Editar",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onEditClick(filme) } // Chama o callback
            )
            // ÍCONE DE EXCLUIR: CLICÁVEL
            Icon(
                Icons.Default.Close,
                "Excluir",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onDeleteClick(filme) } // Chama o callback
            )
        }
    }
}

// -------------------------------------------------------------------------
// COMPOSABLE TELA CADASTRO FILMES
// -------------------------------------------------------------------------

@Composable
fun TelaCadastroFilmesSimplificada(modifier: Modifier = Modifier) {
    // ESTADOS
    var filmes by remember { mutableStateOf<List<Filmes>>(emptyList()) }
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var filmeEditando by remember { mutableStateOf<Filmes?>(null) } // Estado de edição

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val filmesDao = db.filmesDAO()

    // Lógica para recarregar a lista
    fun recarregarFilmes() {
        CoroutineScope(Dispatchers.Main).launch {
            filmes = buscarFilmes(filmesDao)
        }
    }

    // EFEITO: Carrega os dados na inicialização
    LaunchedEffect(Unit) {
        recarregarFilmes()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // CARD DO FORMULÁRIO DE CADASTRO
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.LightGray, RectangleShape),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(Modifier.padding(16.dp)) {

                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Filme") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descricao") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ÚNICO BOTÃO DE ADIÇÃO/EDIÇÃO (CORRIGIDO)
                Button(
                    onClick = {
                        if (nome.isNotBlank() && descricao.isNotBlank()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (filmeEditando == null) {
                                    // MODO ADICIONAR
                                    inserirFilme(nome, descricao, filmesDao)
                                } else {
                                    // MODO EDITAR
                                    val filmeAtualizado =
                                        filmeEditando!!.copy(nome = nome, desc = descricao)
                                    atualizarFilme(filmeAtualizado, filmesDao)
                                    filmeEditando = null // Sai do modo de edição
                                }
                                // Limpa os campos e recarrega após a operação
                                nome = ""
                                descricao = ""
                                recarregarFilmes()
                            }
                        }
                    }
                ) {
                    // MUDA O TEXTO DO BOTÃO
                    Text(if (filmeEditando == null) "Adicionar Filme" else "Salvar Edição")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // LISTA DE FILMES
        LazyColumn(
            modifier = Modifier.fillMaxSize() // Ocupa o restante da tela
        ) {
            // ALTERAÇÃO AQUI: Usar itemsIndexed para obter a posição na lista
            itemsIndexed(filmes) { index, filme ->
                // A 'posição' que você quer exibir é (index + 1)

                UmFilme(
                    filme = filme,
                    // Passamos o índice de exibição (index + 1)
                    displayIndex = index + 1, // <--- NOVO PARÂMETRO
                    onEditClick = { itemAEditar ->
                        // Lógica de Edição: Preenche o formulário
                        nome = itemAEditar.nome
                        descricao = itemAEditar.desc
                        filmeEditando = itemAEditar
                    },
                    onDeleteClick = { itemADeletar ->
                        // Lógica de Exclusão
                        CoroutineScope(Dispatchers.IO).launch {
                            deletarFilme(itemADeletar, filmesDao)
                            recarregarFilmes() // Atualiza a lista
                        }
                    }
                )
            }
        }
    }
}