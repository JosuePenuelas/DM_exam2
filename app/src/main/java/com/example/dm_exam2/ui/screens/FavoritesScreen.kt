package com.example.dm_exam2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dm_exam2.data.database.FavoriteShow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    onShowClick: (Int) -> Unit = {},
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
) {
    val state by viewModel.uiState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteShows()
    }

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
                        "Favorites Shows",
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
            )
        },
    ){ paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.favoritesShow.isEmpty()){
                Column(modifier = Modifier .padding(100.dp) .align(Alignment.CenterHorizontally)) {
                    Text(
                        text = "the list of favorites show is empty",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
            }else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        items(state.favoritesShow) { favoriteShow ->
                            FavoritesScreenBody(favoriteShow, onShowClick)
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun FavoritesScreenBody(favoriteShow: FavoriteShow, onShowClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(300.dp)
            .clickable { favoriteShow.id?.let { onShowClick(it) } },
    ) {
        Column {
            AsyncImage(
                model = favoriteShow.image,
                contentDescription = "imagen del programa ${favoriteShow.name}",
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

                if (favoriteShow.rate?.toString() != null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(30.dp)
                            .clip(AlertDialogDefaults.shape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        favoriteShow.rate?.toString()?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,  // Reduce el tamaño de la fuente
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)  // Añade un poco de padding alrededor del texto
                            )
                        }
                    }
                }

                //mostrar titulo
                Column {
                    Text(
                        text = favoriteShow.name.toString(),
                        fontSize = 16.sp,  // Reduce el tamaño de la fuente
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)  // Añade un poco de padding alrededor del texto
                    )
                }

                //mostrar generos
                Column {
                    ShowGenresFavoriteShow(favoriteShow.genres)
                }
            }
        }
    }
}

@Composable
fun ShowGenresFavoriteShow(genres: String?) {
    if (genres != null) {
        Text(
            text = genres,
            fontSize = 8.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}
