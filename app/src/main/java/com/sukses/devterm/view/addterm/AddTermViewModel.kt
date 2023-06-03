package com.sukses.devterm.view.addterm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.StorageRepository

class AddTermViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var addTermUiState by mutableStateOf(AddTermUiState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun onTitleChange(title: String) {
        addTermUiState = addTermUiState.copy(title = title)
    }

    fun onCategoryChange(category: String) {
        addTermUiState = addTermUiState.copy(category = category)
    }

    fun onDescChange(desc: String) {
        addTermUiState = addTermUiState.copy(desc = desc)
    }

    fun addTerm() {
        if (hasUser){
            repository.addTerm(
                userId = user!!.uid,
                title = addTermUiState.title,
                category = addTermUiState.category,
                description = addTermUiState.desc,
                timestamp = Timestamp.now()
            ){
                addTermUiState = addTermUiState.copy(termAddedStatus = it)
            }
        }
    }

    fun resetTermAddedStatus(){
        addTermUiState = addTermUiState.copy(
            termAddedStatus = false,
        )
    }

    fun resetState(){
        addTermUiState = AddTermUiState()
    }
}

data class AddTermUiState(
    val title: String = "",
    val desc: String = "",
    val category: String = "",
    val termAddedStatus: Boolean = false,
    val selectedNote: Terms? = null,
)