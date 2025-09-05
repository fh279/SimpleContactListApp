package com.example.simplecontactlist

data class ListState(
    val listItems: List<ListItem>  = listOf(),
    val name: String = "",
    val surName: String = "",
    val number: String = "",
    val errorText: String? = null
) {
}
