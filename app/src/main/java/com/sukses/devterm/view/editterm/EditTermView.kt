@file:OptIn(ExperimentalMaterial3Api::class)

package com.sukses.devterm.view.editterm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sukses.devterm.view.addterm.AddTermUiState
import kotlinx.coroutines.launch

@Composable
fun EditTermScreen(
    editTermViewModel: EditTermViewModel?,
    termId: String
) {
    val editTermUiState = editTermViewModel?.editTermUiState ?: EditTermUiState()

    val isFormsNotBlank = editTermUiState.title.isNotBlank() &&
            editTermUiState.desc.isNotBlank() && editTermUiState.desc.isNotBlank()
    val isTermIdNotBlank = termId.isNotBlank()
    val icon = Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if (isTermIdNotBlank) {
            editTermViewModel?.getNote(termId)
        } else {
            editTermViewModel?.resetState()
        }
    }

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(
                    onClick = {
                        editTermViewModel?.updateTerm(termId)
                    }
                ) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LaunchedEffect(key1 = editTermUiState.updateNoteStatus) {
                if (editTermUiState.updateNoteStatus) {
                    scope.launch {
                        snackbarHostState
                            .showSnackbar("Note updated successfully")
                        editTermViewModel?.resetState()
                    }
                }
            }

            OutlinedTextField(
                value = editTermUiState.title,
                onValueChange = {
                    editTermViewModel?.onTitleChange(it)
                },
                singleLine = true,
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = editTermUiState.category,
                onValueChange = {
                    editTermViewModel?.onCategoryChange(it)
                },
                singleLine = true,
                label = { Text(text = "Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = editTermUiState.desc,
                onValueChange = { editTermViewModel?.onDescChange(it) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}