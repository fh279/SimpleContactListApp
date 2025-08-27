package com.example.simplecontactlist

/*class ListState2(
    val listItems: MutableList<String>  = mutableListOf(), // mutableStateListOf<String>()
    // private set - что значит эта строка?
    val name: String = emptyString(), // mutableStateOf("")
    val surName: String = emptyString(), //  mutableStateOf("")
    val number: String = emptyString(), //  mutableStateOf("")
    val errorText: String*//* = emptyString()*//*, // mutableStateOf("")

) {
    companion object {
        fun default(): ListState {
            return ListState(
            listItems = mutableListOf(), // mutableStateListOf<String>()
            name = emptyString(), // mutableStateOf("")
            surName = emptyString(), //  mutableStateOf("")
            number = emptyString(), //  mutableStateOf("")
            errorText =  emptyString(), // mutableStateOf("")
            )
        }
    }*/


    // Преследует ощущение, что оборачивание в mutableState должно происходить не здесь.


    /*fun copy(
        listItems: MutableList<String> = this.listItems,
        name: String = this.name,
        surName: String = this.surName,
        number: String = this.number,
        errorText: String = this.errorText
    ): ListState {
        return ListState(
            listItems = listItems,
            name = name,
            surName = surName,
            number = number,
            errorText = errorText
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListState

        if (listItems != other.listItems) return false
        if (name != other.name) return false
        if (surName != other.surName) return false
        if (number != other.number) return false
        if (errorText != other.errorText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = listItems.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + surName.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + errorText.hashCode()
        return result
    }

    override fun toString(): String {
        return "ListState(listItems=$listItems, name='$name', surName='$surName', number='$number', errorText='$errorText')"
    }*/
// }

// ОДин из способов описывать состояния - data class.



data class ListState(
    val listItems: List<String>  = listOf(),
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
