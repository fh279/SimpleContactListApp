package com.example.simplecontactlist

import androidx.lifecycle.ViewModel

class MyViewModel(
    private val listState: ListState,
    private val stringResourcesProvider: StringResourcesProvider
) : ViewModel() {

    // Название метода - караул.
    fun onChangeTextFieldValue(text: String) {
        listState.text.value = text
        listState.errorText.value = ""
    }

    // Название метода - караул.
    // Метод должен быть Unit как сейчас или следует сделать что бы он возвращал String и его работу куда-то присваивать?
    fun addItemToList(emptyStateStringResource: Int) {
        if (listState.text.value.isNotBlank()) {
            listState.listItems.add(listState.text.value)
            listState.text.value = ""
        } else {
            listState.errorText.value = getLocalizedString(emptyStateStringResource)
        }
    }

    // Название метода некорректно - он не показывает ничего, он просто обрабатывает логику, а показывает то, где он вызывается ( Compose - функция )
    fun showErrorText(
        textInOTF: String,
    ): String? {
        return if (textInOTF.isNotEmpty()) {
            listState.errorText.value
        } else null
    }

    // Следует ли подобные методы оформлять именно так, и если нет, то как? + вопрос - нейминг норм?
    private fun getLocalizedString(resource: Int): String {
        return stringResourcesProvider.getString(resource)
    }
}
