package com.sukses.devterm.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.navigation.HomeRoutes
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.view.searchresult.TermSr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick :(Any) -> Unit,
    navController: NavController,
    homeViewModel: HomeViewModel?,
    navToLoginPage: () -> Unit
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    val textSearch = homeViewModel?.searchText

    val aka: List<Terms>
    val exampleTerms = listOf(
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
        Column {
            Row {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    if (textSearch != null) {
                        OutlinedTextField(
                            value = textSearch,
                            onValueChange = {
                                homeViewModel.onSearchChange(it)
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        navController.navigate(route = HomeRoutes.SearchResult.name)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            label = { Text(text = "Keyword") },
                            shape = RoundedCornerShape(percent = 20),
                            placeholder = { Text(text = "HTML") }
                        )
                    }
                }
//                val context = LocalContext.current
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Alignment.TopEnd
                ) {
                    IconButton(
                        modifier = Modifier.padding(top = 12.dp),
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
                            text = { Text("My Terms") },
                            onClick = { navController.navigate(route = HomeRoutes.MyTerm.name) }
                        )
                        DropdownMenuItem(
                            text = { Text("Add term") },
                            onClick = { navController.navigate(route = HomeRoutes.AddTerm.name) }
                        )
                        DropdownMenuItem(
                            text = { Text("View Categories") },
                            onClick = { navController.navigate(route = HomeRoutes.CategoryList.name) }
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
                items (items = aka.take(10)) {term ->
                    TermSr(
                        name = term.title,
                        category = term.category,
                        description = term.description
                    ) {
                        onItemClick.invoke(term.documentId)
                    }
                }
            }
        }
    }
}

//@Composable
//fun TermCard(name: String, category: String, description: String) {
//    Card(
//        modifier = Modifier
//            .padding(10.dp)
//            .fillMaxWidth()
//            .height(155.dp)
//            .background(color = MaterialTheme.colorScheme.surface),
//        shape = MaterialTheme.shapes.medium,
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 5.dp
//        )
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Column(Modifier.padding(5.dp)) {
//                Text(
//                    text = name,
//                    style = MaterialTheme.typography.titleLarge,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
//                Text(text = "~$category", style = MaterialTheme.typography.labelLarge)
//                Spacer(modifier = Modifier.padding(vertical = 8.dp))
//                Text(
//                    text = description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Justify
//                )
//            }
//        }
//    }
//}