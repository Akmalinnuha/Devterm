package com.sukses.devterm.view.addterm

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isFormsNotBlank) addTermViewModel?.addTerm()
                    else Toast.makeText(context, "Please fill all the forms", Toast.LENGTH_SHORT).show()
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
                        value = addTermUiState.category,
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
                                    addTermViewModel?.onCategoryChange(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            OutlinedTextField(
                value = addTermUiState.desc,
                onValueChange = { addTermViewModel?.onDescChange(it) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 78.dp)
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropdownCategories() {
//    val context = LocalContext.current
//    val categories = arrayOf(
//        "Software Engineering", "Web", "Mobile Development",
//        "Artificial Intelligence", "Cyber Security", "Cloud Computing",
//        "Software", "Programming", "Database/DB Management",
//        "Computing", "IT Infrastructure"
//    )
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(categories[0]) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = {
//                expanded = !expanded
//            }
//        ) {
//            OutlinedTextField(
//                value = selectedText,
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth()
//            )
//
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                categories.forEach { item ->
//                    DropdownMenuItem(
//                        text = { Text(text = item) },
//                        onClick = {
//                            selectedText = item
//                            expanded = false
//                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
//                        }
//                    )
//                }
//            }
//        }
//    }
//}