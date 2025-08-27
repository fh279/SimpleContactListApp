package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    // Такое решение сомнительно.
    private val randomColor: Float
        get() = Random.nextFloat()
    private val viewModel = MyViewModel(
        stringResourcesProvider = StringResourcesProvider(this))

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleContactListTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                ) {/* innerPadding ->*/
                    MainScreen()
                    // Почему если оформить кнопку в отдельную Compose функцию, то она появляется не под списком, а в самом верху? Можно ли добиться такого что бы элементы экрана были в отдельных композициях, но располагались в порядке вызова функций?
                    // AddContactButton()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        // 1. Преобразует StateFlow (тип из корутин) в State (тип из compose)
        // 2. Здесь происходит подписка на StateFlow. То есть мы можем следить за измеенениями нашего state.
        val state = viewModel.state.collectAsStateWithLifecycle()
        ContactsList(state)
    }

    @Composable
    fun ContactsList(state: State<ListState>) {
        // А как обрабатывать смену состояния? Типа, экран перевернул?
        // не обязательно юзать mutableStateListOf. попробуй metableStateOf а внутри лист строк.

        /*val listState = remember { ListState() }*/
        // из за этого (эти двое расположено внутри активити) у нас затирается состояние. Так не надо. Нужно придумать как создать вьюмодель и listState так что бы и состояние хранилось, и все остальное работало.
        // val listState = remember { ListState(ListState.default()) } - теперь эта строка не нужна, потому что состояние теперь мы храним во вьюмодели (переменная state).


        Column(
            modifier = Modifier.fillMaxSize().imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                contentPadding = PaddingValues(all = 0.dp),
                modifier = Modifier.height(350.dp)

            ) {
                if (state.value.listItems.isNotEmpty()) {
                    items(state.value.listItems,
                    ) { item ->
                        val (color1, color2) = remember {
                            Pair(
                                Color(
                                    red = randomColor,
                                    green = randomColor,
                                    blue = randomColor
                                ),
                                Color(
                                    red = randomColor,
                                    green = randomColor,
                                    blue = randomColor
                                )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Absolute.Left,
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp)
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                color1,
                                                color2
                                            )
                                        )
                                    )
                            ) {
                                Text(
                                    text = item.getOrNull(0).toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(alignment = Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.width(50.dp))
                            Column(modifier = Modifier.width(200.dp)) {
                                Text(text = item)
                                // Text(text = item) - тут дальше будет не item (который name, а номер телефона и это уже будет не item, а в качестве item надо будет передать объект, одним из пропертей которого является item, а другими - фамилия и номер телефона.
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = {
                                    viewModel.removeItemFromList(item)
                                },
                                modifier = Modifier,
                                shape = MaterialTheme.shapes.extraSmall,
                                contentPadding = PaddingValues(
                                    start = 2.dp,
                                    top = 2.dp,
                                    end = 2.dp,
                                    bottom = 2.dp
                                )
                            ) {
                                Text(stringResource(R.string.delete_button_text))
                            }
                        }
                    }
                }
                // Такой вариант - очень плохой, кмк. Как это обойти? Проблема с композабл скоупом.
                // Вопрос: можно ли менять параметры модифаера композ функции на ходу, вне модифаера? Например, хочу что б в if был lazy column в рамке, а в else - без рамки.
                else {
                    // Items использован что бы задать composable скоуп и иметь возможность добавить изображение emptyState'а.
                    items(arrayListOf("")) {
                        Column {
                            Image(painter = painterResource(id = R.drawable.emptystate2),
                                contentDescription = "EmptyState",
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = stringResource(R.string.empty_state_text),
                                modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                        }
                    }
                }
            }

            Column {
                // Интересно чего это метод onChangeNameValue не работает когда написан универсально, но работает когда написано 3 отдельных метода. по любому есть решение.
                // А еще надо текст ошибки разтиражировать с 1 до 3 (на 3 поля вместо 1) и вставлять когда и где надо.
                OutlinedTextField(
                    value = state.value.name,
                    onValueChange = { name -> viewModel.onChangeTextFieldValue(name)/*onChangeNameValue(name)*/ },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_name_field_label)) },
                    supportingText = {
                        // Вся эта конструкция через let мне не нравится...
                        state.value.errorText?.let {
                            viewModel.showErrorText(it)?.let {
                                Text(
                                    text = it,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                )

                /*OutlinedTextField(
                    value = listState.surName.value,
                    onValueChange = { surName -> viewModel.onChangeSurnameValue(surName) },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_surname_field_label)) },
                    supportingText = {
                        // Вся эта конструкция через let мне не нравится...
                        viewModel.showErrorText(listState.errorText.value)?.let {
                            Text(
                                text = it,
                                color = Color.Red
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = listState.number.value,
                    onValueChange = { number -> viewModel.onChangeNumberValue(number) },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_number_field_label)) },
                    supportingText = {
                        // Вся эта конструкция через let мне не нравится...
                        viewModel.showErrorText(listState.errorText.value)?.let {
                            Text(
                                text = it,
                                color = Color.Red
                            )
                        }
                    },
                )*/

                Button(
                    modifier = Modifier.padding(top = 0.dp),
                    onClick = {
                        viewModel.addItemToList(
                            emptyStateStringResource = R.string.emty_name_field_error
                        )
                    }
                ) { Text(stringResource(R.string.add_contact_button_text)) }

            }





        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview(showBackground = true)
    @Composable
    fun ContactList() {
        Scaffold {
            SimpleContactListTheme {
                MainScreen()
            }
        }
    }
}
