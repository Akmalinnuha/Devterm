package com.sukses.devterm.view.termcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.repository.StorageRepository
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

//    var termCategory by mutableStateOf("")
//        private set

    fun loadCategories(termCategory : String) {
        getCategories(name = termCategory)
    }
    private fun getCategories(name : String) = viewModelScope.launch {
        repository.getTermByCategory(name).collect {
            categoryUiState = categoryUiState.copy(termsCategoryList = it)
        }
    }
}

data class CategoryUiState(
    val termsCategoryList: Resources<List<Terms>> = Resources.Loading()
)