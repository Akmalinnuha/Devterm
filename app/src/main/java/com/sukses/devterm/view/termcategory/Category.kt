package com.sukses.devterm.view.termcategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.repository.Resources
import com.sukses.devterm.view.searchresult.TermSr

@Composable
fun CategoryScreen(
    onItemClick: (Any)-> Unit,
    CategoryName : String,
    categoryViewModel: CategoryViewModel?,
) {
    val categoryUiState = categoryViewModel?.categoryUiState ?: CategoryUiState()

    val isCategoryNameNotBlank = CategoryName.isNotBlank()
    LaunchedEffect(key1 = Unit) {
        if (isCategoryNameNotBlank) categoryViewModel?.loadCategories(CategoryName)
    }

    val resultCategory : List<Terms> = categoryUiState.termsCategoryList.data ?: emptyList()

    if (resultCategory == emptyList<Terms>()) {
        if (categoryViewModel != null) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(text = "Istilah tidak ditemukan di kategori ini")
            }
        }
    }

    when (categoryUiState.termsCategoryList) {
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
                contentPadding = PaddingValues(8.dp)
            ) {
                items(categoryUiState.termsCategoryList.data ?: emptyList()) { cat ->
                    TermSr(name = cat.title, category = cat.category, description = cat.description) {
                        onItemClick.invoke(cat.documentId)
                    }
                }
            }
        }
        else -> {
            Text(text = "Cannot found what you are looking")
        }
    }
}