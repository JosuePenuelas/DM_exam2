package com.example.dm_exam2.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dm_exam2.model.Show


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
    onShowClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val state by viewModel.uiState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getShows()
    }

    if (state.isLoading) {
        Text(
            text = "Cargando...",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(50.dp)
        )
    } else {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "TVMaze shows",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(onClick = onFavoritesClick) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Go to Favorites",
                                )
                                Text(
                                    text = "Favorites",
                                    textAlign = TextAlign.Center,
                                    fontSize = 6.sp,
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SearchBar(
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = {
                        active = false
                        if (text.isNotEmpty()) {
                            viewModel.getShowsByName(text)
                        } else {
                            viewModel.getShows()
                        }
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        if (active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    text = ""
                                    active = false
                                    viewModel.getShows()  // Resetear la lista al limpiar la búsqueda
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Search Icon"
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {}

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        items(state.shows) { show ->
                            ScreenHomeBody(show, onShowClick)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ScreenHomeBody(show: Show, onShowClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(300.dp)
            .clickable { show.id?.let { onShowClick(it) } },
    ) {
        Column {
            AsyncImage(
                model = show.image?.original,
                contentDescription = "imagen del programa ${show.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),  // Aumenta la altura de la imagen
                contentScale = ContentScale.Crop
            )

            //mostrar puntuaje
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)  // Reduce el espacio superior
            ) {

                if (show.rating?.average != null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(30.dp)
                            .clip(shape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = show.rating?.average.toString(),
                            fontSize = 12.sp,  // Reduce el tamaño de la fuente
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)  // Añade un poco de padding alrededor del texto
                        )
                    }
                }

                //mostrar titulo
                Column {
                    Text(
                        text = show.name.toString(),
                        fontSize = 16.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar generos
                Column {
                    ShowGenres(show.genres)
                }
            }
        }
    }
}

@Composable
fun ShowGenres(genres: List<String?>?) {
    if (genres != null) {
        Text(
            text = genres.joinToString(separator = ", "),
            fontSize = 8.sp,  // Reduce el tamaño de la fuente
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)  // Añade un poco de padding alrededor del texto
        )
    }
}
