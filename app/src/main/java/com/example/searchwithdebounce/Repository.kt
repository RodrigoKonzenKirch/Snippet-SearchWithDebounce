package com.example.searchwithdebounce

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository {

    val mockList = listOf(
        Item("Athens", "Description 1"),
        Item("Rome", "Description 2"),
        Item("Paris", "Description 3"),
        Item("London", "Description 4"),
        Item("New York", "Description 5"),
        Item("Tokyo", "Description 6"),
        Item("Berlin", "Description 7"),
        Item("Madrid", "Description 8"),
        Item("Rosales", "Description 9"),
        Item("Barcelona", "Description 10"),
        Item("Lisbon", "Description 11"),
        Item("Prague", "Description 12"),
        Item("Vienna", "Description 13"),
        Item("Amsterdam", "Description 14"),
        Item("Lost Island", "Description 15"),
        Item("Lissabon", "Description 16"),
    )


    fun search(query: String): Flow<List<Item>> {
        return flow {
            //delay(500) // Simulate a network request
            val results = mockList.filter {
                it.city.contains(query, ignoreCase = true)
            }
            emit(results)
        }
    }
}