package com.example.simplecontactlist

// ОДин из способов описывать состояния - data class.
data class ListState(
    val listItems: List<ListItem>  = listOf(),
    val name: String = emptyString(),
    val surName: String = emptyString(),
    val number: String = emptyString(),
    val errorText: String?
) {
    companion object {
        fun default(): ListState {
            return ListState(
                listItems = mutableListOf(),
                name = emptyString(),
                surName = emptyString(),
                number = emptyString(),
                errorText = emptyString()
            )

        }

        fun emptyString(): String = ""
    }
}
