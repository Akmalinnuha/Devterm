@file:OptIn(ExperimentalMaterial3Api::class)

package com.sukses.devterm.view.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.view.home.HomeUiState
import com.sukses.devterm.view.home.HomeViewModel

@Composable
fun SearchResult(
    homeViewModel: HomeViewModel?,
    onItemClick: (Any?) -> Unit
) {
    val searchResultState = homeViewModel?.homeUiState ?: HomeUiState()
    val searched : String? = homeViewModel?.searchText
    val resultSearch : List<Terms> = searchResultState.termsList.data?.filter { terms: Terms ->
        searched?.let { terms.title.contains(it, ignoreCase = true) } == true
    } ?: emptyList()

    Surface {
        Column {
            if (resultSearch == emptyList<Terms>()) {
                if (homeViewModel != null) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Hasil pencarian \"${homeViewModel.getSearch()}\" tidak ditemukan")
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(resultSearch) {term ->
                    TermSr(name = term.title, category = term.category, description = term.description) {
                        onItemClick.invoke(term.documentId)
                    }
                }
            }
        }
    }
}

@Composable
fun TermSr(name: String, category: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick.invoke() },
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
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

//when (searchResultState.termsList) {
//    is Resources.Loading -> {
//        CircularProgressIndicator(
//            modifier = Modifier
//                .fillMaxSize()
//                .wrapContentSize(align = Alignment.Center)
//        )
//    }
//    is Resources.Success -> {
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            contentPadding = PaddingValues(16.dp)
//        ) {
//            items(searchResultState.termsList.data ?: emptyList()) {term ->
//                if (searched?.let { term.title.contains(it, ignoreCase = true) } == true) {
//                    TermCard(name = term.title, category = term.category, description = term.description)
//                }
//            }
//        }
//    }
//    else -> {
//        Text(text = "Realest")
//    }
//}