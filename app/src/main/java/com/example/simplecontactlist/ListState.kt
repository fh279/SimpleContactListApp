package com.example.simplecontactlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class ListState {
    // Преследует ощущение, что оборачивание в mutableState должно происходить не здесь.
    var listItems = mutableStateListOf<String>()
        // private set - что значит эта строка?
    var text = mutableStateOf("")
    var errorText = mutableStateOf("")
}
