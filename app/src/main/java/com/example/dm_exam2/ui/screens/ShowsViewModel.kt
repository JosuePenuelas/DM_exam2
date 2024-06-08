package com.example.dm_exam2.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dm_exam2.ShowsApplication
import com.example.dm_exam2.data.ShowsRepository
import com.example.dm_exam2.data.database.FavoriteShow
import com.example.dm_exam2.model.Show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class UIState(
    val shows: List<Show> = emptyList(),
    val selectedShow: Show? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoritesShow: List<FavoriteShow> = emptyList(),
)

class ShowsViewModel(
    private val showsRepository: ShowsRepository
) : ViewModel() {

    val uiState = mutableStateOf(UIState())

    //api
    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val shows = showsRepository.getShows()
                uiState.value = uiState.value.copy(shows = shows)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getShowById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val show = showsRepository.getShowById(id)
                uiState.value = uiState.value.copy(selectedShow = show)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getShowsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val shows = showsRepository.getShowsByName(name)
                uiState.value = uiState.value.copy(shows = shows)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    //database
    fun getAllFavoriteShows(){
        viewModelScope.launch((Dispatchers.IO)) {
            val allFavorites = showsRepository.getAllFavoriteShows()
            uiState.value = uiState.value.copy(favoritesShow = allFavorites)
        }
    }

    fun addFavoriteShow(favoriteShow: FavoriteShow) {
        viewModelScope.launch((Dispatchers.IO)) {
            showsRepository.addFavoriteShow(favoriteShow)
            val allFavorites = showsRepository.getAllFavoriteShows()
            Log.d("TAG", "Todos los favoritos: $allFavorites")
        }
    }

    fun deleteFavoriteShow(favoriteShow: FavoriteShow){
        viewModelScope.launch(Dispatchers.IO) {
            showsRepository.removeFavoriteShow(favoriteShow)
            getAllFavoriteShows()
        }
    }

    fun isFavoriteShow(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch((Dispatchers.IO)) {
            val result = showsRepository.isFavoriteShow(id)
            onResult(result)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                                ShowsApplication)
                val showsRepository = application.container.showsRepository
                ShowsViewModel(showsRepository = showsRepository)
            }
        }
    }
}
