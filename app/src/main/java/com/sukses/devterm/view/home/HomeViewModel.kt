package com.sukses.devterm.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.repository.StorageRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    var searchResultState by mutableStateOf(SearchResultState())
        private set

    var searchText by mutableStateOf("")
        private set

    fun loadTerms(){
        getAllTerms()
    }

    fun loadSearch() {
        getTermName()
    }

    fun onSearchChange(search: String) {
        searchText = search
    }

    fun getSearch():String {
        return searchText
    }

    private fun getAllTerms() = viewModelScope.launch {
        repository.getAllTerm().collect {
            homeUiState = homeUiState.copy(termsList = it)
        }
    }

    private fun getTermName() = viewModelScope.launch {
        repository.getTermByName(searchText).collect {
            searchResultState = searchResultState.copy(searchTerms = it)
        }
    }

    fun signOut() = repository.signOut()
}

data class HomeUiState(
    val termsList: Resources<List<Terms>> = Resources.Loading()
)

data class SearchResultState(
    val searchTerms: Resources<List<Terms>> = Resources.Loading()
)