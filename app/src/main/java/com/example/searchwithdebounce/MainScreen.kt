package com.example.searchwithdebounce

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val query by viewModel.searchQuery.collectAsState()
        val results by viewModel.searchResults.collectAsState()
        val isSearching = query.isNotEmpty()

        Text(
            text = "Search for cities",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 32.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = query,
            onValueChange = viewModel::onSearchQueryChanged,
            label = { Text("City name") },
            singleLine = true
        )

        if (isSearching && results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results.size) { index ->
                    CityItem(item = results[index])
                }
            }
        }
    }
}

@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    item: Item
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Text(
            text = item.city,
            modifier = Modifier.padding(16.dp)
        )
    }
}
