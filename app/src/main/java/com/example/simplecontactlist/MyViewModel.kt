package com.example.simplecontactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MyViewModel(
    // Либо вернуть использование StringResourcesProvider (больше - в опытных целях, чем по необходимости, ибо можно обойти необходимость в этой штуке и использовать композный stringResource. Либо убрать и вернуть получение вьюмодели через by viewModels().
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

    // С этим что делать?
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
    fun addItemToList(emptyStateStringResource: String) {
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
                    errorText = emptyStateStringResource
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
    /*fun showErrorText(
        textInOTF: String,
    ): String? {
        return if (textInOTF.isNotEmpty()) {
            _state.value.errorText
        } else null
    }*/
    // это надо будет раскомментить когда разрулим со строками.
    /*fun showErrorText() {
        _state.update { it.copy(errorText = getLocalizedString(R.string.emty_name_field_error)) }
    }*/

    // Следует ли подобные методы оформлять именно так, и если нет, то как? + вопрос - нейминг норм?
    /*private fun getLocalizedString(resource: Int): String {
        return stringResourcesProvider.getString(resource)
    }*/
}

@Suppress("UNCHECKED_CAST")
/**
 * MyViewModelFactory это фабрика для создания экземпляров MyViewModel.
 * Другими словами, мы делегируем создание экземпляров MyViewModel на эту фабрику (MyViewModelFactory).
 * По-этому, количество аргументов у MyViewModelFactory и MyViewModel должно совпадать.
 * Для создания MyViewModel потребуется передавать эту фабрику в ViewModelProvider. (см. MainActivity).
 */
class MyViewModelFactory(val provider: StringResourcesProvider): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(provider) as T
        } else {
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}

