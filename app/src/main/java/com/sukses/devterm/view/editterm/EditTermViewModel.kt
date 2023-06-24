package com.sukses.devterm.view.editterm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.StorageRepository

class EditTermViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var editTermUiState by mutableStateOf(EditTermUiState())
        private set

    fun onTitleChange(title: String) {
        editTermUiState = editTermUiState.copy(title = title)
    }

    fun onCategoryChange(category: String) {
        editTermUiState = editTermUiState.copy(category = category)
    }

    fun onDescChange(desc: String) {
        editTermUiState = editTermUiState.copy(desc = desc)
    }

    fun updateTerm(
        termId: String
    ){
        repository.updateTerm(
            title = editTermUiState.title,
            category = editTermUiState.category,
            desc = editTermUiState.desc,
            termId = termId
        ){
            editTermUiState = editTermUiState.copy(updateNoteStatus = it)
        }
    }

    private fun setEditFields(term: Terms){
        editTermUiState = editTermUiState.copy(
            title = term.title,
            category = term.category,
            desc = term.description
        )

    }

    fun getNote(termId:String){
        repository.getTerm(
            termId = termId,
            onError = {},
        ){
            editTermUiState = editTermUiState.copy(selectedTerm = it)
            editTermUiState.selectedTerm?.let { it1 -> setEditFields(it1) }
        }
    }

    fun resetState(){
        editTermUiState = EditTermUiState()
    }
}

data class EditTermUiState(
    val title: String = "",
    val category: String = "",
    val desc: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedTerm: Terms? = null,
)