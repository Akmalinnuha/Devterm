package com.sukses.devterm.view.myterm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources

@Composable
fun MyTermScreen(
    onTermClick: (id:String) -> Unit,
    myTermViewModel: MyTermViewModel?
) {
    val myTermUiState = myTermViewModel?.myTermUiState ?: MyTermUiState()

    var openDialog by remember {
        mutableStateOf(false)
    }
    var selectedTerm: Terms? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = myTermViewModel?.loadMyTerms()) {
        myTermViewModel?.loadMyTerms()
    }
    
    Surface() {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.padding(12.dp)) {
                Text(text = "My Terms")
            }
            when (myTermUiState.myTermsList) {
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(6.dp)
                    ) {
                        items(myTermUiState.myTermsList.data ?: emptyList()) {term ->
                            TermItem(
                                Term = term,
                                onLongClick = {
                                    openDialog = true
                                    selectedTerm = term
                                }
                            ) {
                                onTermClick.invoke(term.documentId)
                            }
                        }
                    }
                    AnimatedVisibility(visible = openDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialog = false
                            },
                            title = { Text(text = "Delete a term?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedTerm?.documentId?.let {termID ->
                                            myTermViewModel?.deleteTerm(termID)
                                        }
                                        openDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    ),
                                ) {
                                    Text(text = "Delete")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { openDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                        )
                    }
                }
                else -> {
                    Text(
                        text = myTermUiState
                            .myTermsList.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TermItem(
    Term: Terms,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
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
                    text = Term.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(text = "~${Term.category}", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = Term.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}