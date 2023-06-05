package com.sukses.devterm.view.addterm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTermScreen(
    addTermViewModel: AddTermViewModel?
) {
    val addTermUiState = addTermViewModel?.addTermUiState ?: AddTermUiState()

    val isFormsNotBlank = addTermUiState.title.isNotBlank() &&
            addTermUiState.desc.isNotBlank() && addTermUiState.desc.isNotBlank()
    val icon = Icons.Default.Check

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(
                    onClick = {
                        addTermViewModel?.addTerm()
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
            LaunchedEffect(key1 = addTermUiState.termAddedStatus) {
                if (addTermUiState.termAddedStatus) {
                    scope.launch {
                        snackbarHostState
                            .showSnackbar("Added Note Successfully")
                        addTermViewModel?.resetState()
                    }
                }
            }

            OutlinedTextField(
                value = addTermUiState.title,
                onValueChange = {
                    addTermViewModel?.onTitleChange(it)
                },
                singleLine = true,
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = addTermUiState.category,
                onValueChange = {
                    addTermViewModel?.onCategoryChange(it)
                },
                singleLine = true,
                label = { Text(text = "Category")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = addTermUiState.desc,
                onValueChange = { addTermViewModel?.onDescChange(it) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}