package com.sukses.devterm.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.repository.StorageRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    private val user: FirebaseUser?
        get() = repository.user()

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val userId: String
        get() = repository.getUserId()

    fun loadTerms(){
        if (hasUser){
            if (userId.isNotBlank()){
                getTermNotes(userId)
            }
        } else {
            homeUiState = homeUiState.copy(termsList = Resources.Error(
                throwable = Throwable(message = "User is not Login")
            ))
        }
    }

    private fun getTermNotes(userId : String) = viewModelScope.launch {
        repository.getUserTerms(userId).collect {
            homeUiState = homeUiState.copy(termsList = it)
        }
    }

    fun signOut() = repository.signOut()
}

data class HomeUiState(
    val termsList: Resources<List<Terms>> = Resources.Loading()
)