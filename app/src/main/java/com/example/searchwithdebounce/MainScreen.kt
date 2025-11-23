package com.example.searchwithdebounce

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(
        factory = MainViewModel.provideFactory(Repository())
    )
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val query by viewModel.searchQuery.collectAsState()

        val results by viewModel.searchResults.collectAsState()

        Text("Search for cities")
        // Input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            value = query,
            onValueChange = viewModel::onSearchQueryChanged,
        )

        // show result list
        LazyColumn {
            items(results.size) { index ->
                Text(text = results[index].city)
            }
        }
    }
}