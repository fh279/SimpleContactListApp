package com.example.simplecontactlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class ListState {
    // tut nuzhen mutableState?
    var listItems = mutableStateListOf<String>()
    var text = mutableStateOf("")
    var errorText = mutableStateOf("")
}
