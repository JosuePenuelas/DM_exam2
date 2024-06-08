package com.example.dm_exam2.ui.screens

import android.text.Html
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dm_exam2.data.database.FavoriteShow
import com.example.dm_exam2.model.Show

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailScreen(
    navController: NavController,
    id: Int,
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
) {
    val state by viewModel.uiState

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var isFavorite by remember { mutableStateOf(false) }
    var icon by remember { mutableStateOf(Icons.Filled.FavoriteBorder) }

    LaunchedEffect(Unit) {
        viewModel.getShowById(id)
        viewModel.isFavoriteShow(id) { result ->
            isFavorite = result
            icon = if (result) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        }
    }

    if (state.isLoading) {
        Text(text = "Cargando...")
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
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val favoriteShow = state.selectedShow?.toFavoriteShow()
                            if (favoriteShow != null) {
                                if (isFavorite) {
                                    viewModel.deleteFavoriteShow(favoriteShow)
                                } else {
                                    viewModel.addFavoriteShow(favoriteShow)
                                }
                                isFavorite = !isFavorite
                                icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                            }
                        }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Favorite add/remove"
                            )
                        }
                    }
                )
            },
        )
        { paddingValues ->
            state.selectedShow?.let { ShowDetailScreenBody(it, paddingValues) }
        }
    }
}


@Composable
fun ShowDetailScreenBody(show: Show, paddingValues: PaddingValues) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()) // Agrega scroll vertical
        ) {
            AsyncImage(
                model = show.image?.original,
                contentDescription = "imagen del programa ${show.name}",
                modifier = Modifier
                    .size(500.dp),  // Aumenta la altura de la imagen
                contentScale = ContentScale.Crop
            )

            //mostrar puntuaje
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)  // Reduce el espacio superior
            ) {

                if (show.rating?.average != null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(40.dp)
                            .clip(AlertDialogDefaults.shape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = show.rating?.average.toString(),
                            fontSize = 20.sp,  // Reduce el tamaño de la fuente
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)  // Añade un poco de padding alrededor del texto
                        )
                    }
                }

                //mostrar titulo
                Column() {
                    Text(
                        text = show.name.toString(),
                        fontSize = 40.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar generos
                Column() {
                    ShowGenresDetailScreen(show.genres)
                }

                //mostrar Fecha de inicio
                Column {
                    Text(
                        text = AnnotatedString.Builder(
                            "premiered: ${show.premiered}"
                        )
                            .apply {
                                addStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold),
                                    start = 0,
                                    end = 10
                                )
                            }
                            .toAnnotatedString(),
                        fontSize = 15.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar region
                Column() {
                    Text(
                        text = AnnotatedString.Builder(
                            "Country: ${show.network?.country?.name}, " +
                                    "${show.network?.country?.code}"
                        )
                            .apply {
                                addStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold),
                                    start = 0,
                                    end = 8
                                )
                            }
                            .toAnnotatedString(),
                        fontSize = 15.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar lenguaje
                Column {
                    Text(
                        text = AnnotatedString.Builder(
                            "language: ${show.language}"
                        )
                            .apply {
                                addStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold),
                                    start = 0,
                                    end = 9
                                )
                            }
                            .toAnnotatedString(),
                        fontSize = 15.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar resumen
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(AlertDialogDefaults.shape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                ) {
                    Text(
                        text = buildAnnotatedString { append(Html.fromHtml(show.summary)) },
                        fontSize = 15.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)  // Añade un poco de padding alrededor del texto
                    )
                }
            }
        }
    }
}

@Composable
fun ShowGenresDetailScreen(genres: List<String?>?) {
    if (genres != null) {
        Text(
            text = AnnotatedString.Builder("genres: ${genres.joinToString(separator = ", ")}")
                .apply {
                    addStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold),
                        start = 0,
                        end = 7
                    )
                }
                .toAnnotatedString(),
            fontSize = 15.sp,  // Reduce el tamaño de la fuente
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}