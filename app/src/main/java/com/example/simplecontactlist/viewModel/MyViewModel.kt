package com.example.simplecontactlist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplecontactlist.ListItem
import com.example.simplecontactlist.ListState
import com.example.simplecontactlist.StringResourcesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import kotlin.collections.plus

class MyViewModel(private val stringResourcesProvider: StringResourcesProvider) : ViewModel() {
    private val _state: MutableStateFlow<ListState> = MutableStateFlow(ListState())
    val state: StateFlow<ListState> = _state.asStateFlow()

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
                    errorText = null
                )
            }
        }
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
