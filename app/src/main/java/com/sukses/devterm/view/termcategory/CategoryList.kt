package com.sukses.devterm.view.termcategory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//@Preview(showSystemUi = true)
@Composable
fun CategoryListScreen(
    onCategoryClick: (Any?) -> Unit,
    categoryViewModel: CategoryViewModel?
) {
    val categoryUiState = categoryViewModel?.categoryUiState ?: CategoryUiState()

    val categories = listOf(
        "Software Engineering", "Web", "Mobile Development",
        "Artificial Intelligence", "Cyber Security", "Cloud Computing",
        "Software", "Programming", "Database/DB Management",
        "Computing", "IT Infrastructure")
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Term Categories")
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            items(categories) {category->
                itemCategory(category) {
                    onCategoryClick.invoke(category)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun itemCategory(
    name:String,
    onClick: ()-> Unit
) {
    Surface(
        modifier = Modifier
            .border(border = BorderStroke(1.dp, Color.Black)),
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailCategory() {
    itemCategory(name = "Reggie") {

    }
}