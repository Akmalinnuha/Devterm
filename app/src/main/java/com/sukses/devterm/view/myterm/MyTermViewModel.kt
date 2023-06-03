package com.sukses.devterm.view.myterm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.repository.StorageRepository
import kotlinx.coroutines.launch

class MyTermViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var myTermUiState by mutableStateOf(MyTermUiState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val userId: String
        get() = repository.getUserId()

    fun loadMyTerms(){
        if (hasUser){
            if (userId.isNotBlank()){
                getTermNotes(userId)
            }
        } else {
            myTermUiState = myTermUiState.copy(myTermsList = Resources.Error(
                throwable = Throwable(message = "User is not Login")
            ))
        }
    }

    private fun getTermNotes(userId : String) = viewModelScope.launch {
        repository.getUserTerms(userId).collect {
            myTermUiState = myTermUiState.copy(myTermsList = it)
        }
    }

    fun deleteTerm(termId:String) = repository.deleteNote(termId){
        myTermUiState = myTermUiState.copy(myTermsDeletedStatus = it)
    }
}

data class MyTermUiState(
    val myTermsList: Resources<List<Terms>> = Resources.Loading(),
    val myTermsDeletedStatus: Boolean = false,
)