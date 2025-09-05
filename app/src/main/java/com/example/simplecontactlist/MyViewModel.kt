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
    // smth: Resources - прикольная штука для получения ресурсов
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

    private val _state: MutableStateFlow<ListState> = MutableStateFlow(ListState())
    /** Это состояние, на которое мы будем подписываться внутри нашей Activity
    (а именно - нашей composable функции).*/
    val state: StateFlow<ListState> = _state.asStateFlow()

    // Название метода - караул.
    fun onChangeNameFieldValue(text: String) {
        _state.update { currentState ->
            currentState.copy(
                name = text,
                errorText = null
            )
        }
    }

    fun onChangeSurnameValue(text: String) {
        _state.update { currentState ->
            currentState.copy(
                surName = text,
                errorText = null
            )
        }
    }

    fun onChangeNumberValue(text: String) {
        _state.update { currentState ->
            currentState.copy(
                number = text,
                errorText = null
            )
        }
    }

    // Название метода - караул.
    fun addItemToList(emptyStateStringResource: String) {
        if (isFieldsNotEmpty()) {
            _state.update { currentState ->
                currentState.copy(
                    listItems = currentState.listItems + ListItem(
                        name = capitalizeFirstLetterInString(currentState.name),
                        surname = capitalizeFirstLetterInString(currentState.surName),
                        number = currentState.number,
                        id = UUID.randomUUID()
                    ),
                    name = "",
                    surName = "",
                    number = "",
                    /*isNameEmpty = true,
                    isNumberEmpty = true,
                    isSurNameEmpty = true,*/
                    errorText = null
                )
            }
        } // Ниже какая-то двойная обработка условий. Я и так проверяю на пустоту все строки отдельно, а тут еще и дополнительно проверка всех троих сразу.
        else {
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

    private fun isFieldsNotEmpty(): Boolean {
        return _state.value.run { name.isNotEmpty() && surName.isNotEmpty() && number.isNotEmpty() }
    }

    private fun isAtLeastOneFieldEmpty(): Boolean = _state.value.run { name.isEmpty() || surName.isEmpty() || number.isEmpty() }

    fun showErrorText() {
        _state.update { it.copy(errorText = getLocalizedString(R.string.emty_name_field_error)) }
    }

    fun clearErrorText() {
        _state.update { it.copy(errorText = null) }
    }

    /** Сам метод (как и stringResourcesProvider) здесь существуют исключительно в учебных целях.
     *  Можно обойтись без него и получать строки в compose-функциях через метод stringResource()
     * */
    private fun getLocalizedString(resource: Int): String {
        return stringResourcesProvider.getString(resource)
    }

    private fun capitalizeFirstLetterInString(string: String): String {
        return string.mapIndexed { index, char  ->
            when (index) {
                0 -> char.uppercase()
                else -> char
            }
        }.joinToString("")
    }
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
