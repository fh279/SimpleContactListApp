package com.example.simplecontactlist

import java.util.UUID

data class ListItem(
    val name: String,
    val surname: String,
    val number: String,
    val id: UUID = UUID.randomUUID()
) {

}