package com.sukses.devterm.view.searchresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.view.home.HomeViewModel
import com.sukses.devterm.view.home.SearchResultState
import com.sukses.devterm.view.home.TermCard

@Composable
fun SearchResult(homeViewModel: HomeViewModel?) {
    val searchResultState = homeViewModel?.searchResultState ?: SearchResultState()

    LaunchedEffect(key1 = homeViewModel?.loadSearch()) {
        homeViewModel?.loadSearch()
    }

    Surface {
        Column {
            when (searchResultState.searchTerms) {
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
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(searchResultState.searchTerms.data ?: emptyList()) {term ->
                            TermCard(name = term.title, category = term.category, description = term.description)
                        }
                    }
                }
                else -> {
                    Text(text = "Realest")
                }
            }
        }
    }
}