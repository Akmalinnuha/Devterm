@file:OptIn(ExperimentalMaterial3Api::class)

package com.sukses.devterm.view.editterm

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
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
//            AnimatedVisibility(visible = isFormsNotBlank) {
//                FloatingActionButton(
//                    onClick = {
//                        editTermViewModel?.updateTerm(termId)
//                    }
//                ) {
//                    Icon(imageVector = icon, contentDescription = null)
//                }
//            }
            FloatingActionButton(
                onClick = {
                    if (isFormsNotBlank) editTermViewModel?.updateTerm(termId)
                    else Toast.makeText(context, "Pls fill all the forms", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(imageVector = icon, contentDescription = null)
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
            val categories = arrayOf(
                "Software Engineering", "Web", "Mobile Development",
                "Artificial Intelligence", "Cyber Security", "Cloud Computing",
                "Software", "Programming", "Database/DB Management",
                "Computing", "IT Infrastructure"
            )
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = editTermUiState.category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        placeholder = { Text(text = "Category") }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    editTermViewModel?.onCategoryChange(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
//            OutlinedTextField(
//                value = editTermUiState.category,
//                onValueChange = {
//                    editTermViewModel?.onCategoryChange(it)
//                },
//                singleLine = true,
//                label = { Text(text = "Category") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
            OutlinedTextField(
                value = editTermUiState.desc,
                onValueChange = { editTermViewModel?.onDescChange(it) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 78.dp)
            )
        }
    }
}