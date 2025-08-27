package com.example.simplecontactlist

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MyViewModel(

    private val stringResourcesProvider: StringResourcesProvider
) : ViewModel() {

    // Это - изменяемое состояние, которое хранится внутри ViewModel'и. Изменение этого состояния
    // происходит из ВьюМодели.
    /** Есть 3 класса:
     *  - StateFlow,
     *  - SharedFlow,
     *  - Flow
     *  Если они должны быть изменяемыми, в начало имени добавляется Mutable.
     *  Вроде (не точно) эти классы реализуют Observer.
     */

    private val _state: MutableStateFlow<ListState> =  MutableStateFlow(ListState.default())
    /** Это состояние, на которое мы будем подписываться внутри нашей Activity
    (а именно - нашей composable функции).*/
    val state: StateFlow<ListState> =  _state.asStateFlow()

    // Название метода - караул.
    // тут надо было по другому как то. не name.
    fun onChangeTextFieldValue(/*field: MutableState<String>, - захера оно тут?.. */text: String) {
        _state.update { currentState ->
            currentState.copy(
            name = text,
            errorText = ""
            )
        }
    }

    fun onChangeNameValue(text: String) {
        _state.update {
            it.copy(
                name = text,
                errorText = ""
            )
        }
    }

    // пока что закомментил что бы только на имени отточить/
    /*fun onChangeSurnameValue(text: String) {
        _state.surName.value = text
        _state.errorText.value = ""
    }

    fun onChangeNumberValue(text: String) {
        _state.number.value = text
        _state.errorText.value = ""
    }*/

    // Название метода - караул.
    // Метод должен быть Unit как сейчас или следует сделать что бы он возвращал String и его работу куда-то присваивать?
    fun addItemToList(emptyStateStringResource: Int) {
        if (/*isFieldsNotBlank() - это будет надо когда вернешь закомменченные поля. */ _state.value.name.isNotBlank()) {
            _state.update { currentState ->
                currentState.copy(
                    listItems = currentState.listItems + ListItem(
                        name = currentState.name,
                        surname = "",
                        number = "",
                        id = UUID.randomUUID()
                    )  // listOf(currentState.name), // а это точно так делать надо?
                )
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    errorText = getLocalizedString(emptyStateStringResource)
                )
            }
        }
    }

    fun removeItemFromList(itemToRemove: ListItem) {
        _state.update { currentState ->
            currentState.copy(
                listItems = currentState.listItems.filter { it.id != itemToRemove.id }
            )
        }
    }

    // Вот эта штука нам понадобится позже. Не сейчас, когда я работаю только с одним полем.
    /*private fun isFieldsNotBlank(): Boolean {
        return _state.value.run {
            name.isNotBlank() &&
            surName.isNotBlank() &&
            number.isNotBlank()
        }
    }*/

    // Название метода некорректно - он не показывает ничего, он просто обрабатывает логику, а показывает то, где он вызывается ( Compose - функция )
    fun showErrorText(
        textInOTF: String,
    ): String? {
        return if (textInOTF.isNotEmpty()) {
            _state.value.errorText
        } else null
    }

    // Следует ли подобные методы оформлять именно так, и если нет, то как? + вопрос - нейминг норм?
    private fun getLocalizedString(resource: Int): String {
        return stringResourcesProvider.getString(resource)
    }
}
