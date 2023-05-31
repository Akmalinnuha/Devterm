@file:OptIn(ExperimentalMaterial3Api::class)

package com.sukses.devterm.view.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.navigation.HomeRoutes
import com.sukses.devterm.repository.Resources

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel?,
    navToLoginPage: () -> Unit
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val aka: List<Terms>
    val exampleTerms = listOf<Terms>(
        Terms("123","HTML", "Web", "The Description", Timestamp(30, 23), "fur"),
        Terms("123","API","Programming", "The Description", Timestamp(21, 12), "fur"),
        Terms("123","CPU","Hardware", "The Description", Timestamp(34, 45), "fur"),
        Terms("123","XSS", "Cyber Security", "The Description", Timestamp(39,65), "fur")
    )

    LaunchedEffect(key1 = homeViewModel?.loadTerms()) {
        homeViewModel?.loadTerms()
    }
    aka = when (homeUiState.termsList) {
        is Resources.Success -> {
            homeUiState.termsList.data ?: emptyList()
        }
        else -> {
            exampleTerms
        }
    }
    Surface {
        Column() {
            Row {
                Column(modifier = Modifier.padding(16.dp)) {
                    SimpleTextField()
                }
                val context = LocalContext.current
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Alignment.TopEnd
                ) {
                    IconButton(
                        modifier = Modifier.padding(top = 5.dp),
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            Modifier.size(30.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("My terms") },
                            onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
                        )
                        DropdownMenuItem(
                            text = { Text("Add term") },
                            onClick = { navController.navigate(route = HomeRoutes.AddTerm.name) }
                        )
                        DropdownMenuItem(
                            text = { Text("View Categories") },
                            onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
                        )
                        DropdownMenuItem(
                            text = { Text("Profile page") },
                            onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
                        )
                        DropdownMenuItem(
                            text = { Text("About us") },
                            onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
                        )
                        DropdownMenuItem(
                            text = { Text("Sign Out") },
                            onClick = {
                                homeViewModel?.signOut()
                                navToLoginPage.invoke()
                            }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(6.dp)
            ) {
                items (aka) {term ->
                    TermCard(
                        name = term.title,
                        category = term.category,
                        description = term.description
                    )
                }
            }
        }
    }
}

@Composable
fun TermCard(name: String, category: String, description: String) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(155.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(text = "~$category", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun SimpleTextField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = { Text(text = "Keyword") },
        placeholder = { Text(text = "HTML") }
    )
}

@Composable
fun Demo_DropDownMenu() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        Alignment.TopEnd
    ) {
        IconButton(
            modifier = Modifier.padding(top = 5.dp),
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                Modifier.size(30.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("My terms") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("Add term") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("View Categories") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("Profile page") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("About us") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}

//            when (homeUiState.termsList) {
//                is Resources.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .wrapContentSize(align = Alignment.Center)
//                    )
//                }
//                is Resources.Success -> {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentPadding = PaddingValues(16.dp)
//                    ) {
//                        items(homeUiState.termsList.data ?: emptyList()) {term ->
//                            TermCard(name = term.title, category = term.category, description = term.description)
//                        }
//                    }
//                }
//                else -> {
//                    Text(text = "Bitches ain't got cash")
//                }
//            }