package com.sukses.devterm.view.searchresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.sukses.devterm.model.Terms
import com.sukses.devterm.view.home.DetailState
import com.sukses.devterm.view.home.HomeViewModel

@Composable
fun DetailScreen(
    homeViewModel: HomeViewModel?,
    termID : String
) {
    val detailState = homeViewModel?.detailState ?: DetailState()

    val isTermIdNotBlank = termID.isNotBlank()
    LaunchedEffect(key1 = Unit) {
        if (isTermIdNotBlank) {
            homeViewModel?.getTerm(termID)
        }
    }

    val myDetail : Terms = detailState.detailTerms ?: Terms("12","s","s","s", Timestamp(21,22),"12")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = myDetail.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Category : ${myDetail.category}", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = myDetail.description, style = MaterialTheme.typography.bodyMedium)
    }
}