package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    // Такое решение сомнительно.
    private val randomColor: Float
        get() = Random.nextFloat()
    /** Немного истории по работе над ViewModel.
     * Вот так это было по старому:
     * private val viewModel = MyViewModel(stringResourcesProvider = StringResourcesProvider(this))
     * - не будет работать в случае переворота экрана, потому что это кастомный конструктор и он
     * инстанциируетсся при пересоздании активити и данные затираются.
     * Затем был найден другой вариант:
     * private val viewModel: MyViewModel by viewModels() Поворот экрана заработал, данные не перезатираются.
     * Но ТАК это работает только в случае если во Вьюмодель не надо передавать аргументы.
     * И тогда стал вопрос - как сделать так что б и аргументы во ViewModel можно было передавать,
     * и она не умирала с пересозданием Activity. Тогда я узнал что можно
     * использовать ViewModelProvider.Factory - провайдер, в который надо передать фабрику по созданию
     * вьюмоделей. И тогда можно вне активити создать фабрику, сделать lateinit вьюмодель,
     * а потом вьюмодель проинициализировать в активити. и вот эта конструкция будет хранить состояния не умирая при пересоздании активности.
     *
     * Задача со звёздочкой: написать свой делегат для получения кастомизируемых ViewModel. Когда-нибудь.
     */


    private val vmFactory = MyViewModelFactory(StringResourcesProvider(this))
    private lateinit var viewModel: MyViewModel


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(
            owner = this,
            factory = vmFactory
        ).get(MyViewModel::class.java)
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
        // из за этого (эти двое расположено внутри активити) у нас затирается состояние. Так не надо. Нужно придумать как создать вьюмодель и listState так что бы и состояние хранилось, и все остальное работало.
        // val listState = remember { ListState(ListState.default()) } - теперь эта строка не нужна, потому что состояние теперь мы храним во вьюмодели (переменная state).

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .background(color = Color(red = 192, green = 192 , blue = 192)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                contentPadding = PaddingValues(all = 0.dp),
                modifier = Modifier
                    // .align(Alignment.CenterHorizontally)

                    .fillMaxSize()
                    .padding(
                        start = 10.dp,
                        end = 10.dp
                    )
                    .height(350.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))


            ) {
                if (state.value.listItems.isNotEmpty()) {
                    items(state.value.listItems,
                        key = { it.id }
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
                                .padding(start = 12.dp, end = 12.dp, top = 10.dp)
                                .fillMaxWidth()
                                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(5.dp)
                                )
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
                                    text = item.name.getOrNull(0).toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(alignment = Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.width(50.dp))
                            Column(modifier = Modifier.width(200.dp)) {
                                Text(text = "${item.name} ${item.surname}")
                                Text(text = item.number)
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = {
                                    viewModel.removeItemFromList(item)
                                },
                                modifier = Modifier.padding(end = 10.dp),
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
                        Column(
                            modifier = Modifier.fillMaxSize()/*.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))*/.padding(all = 20.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.emptystate2),
                                contentDescription = "EmptyState",
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(CircleShape)
                                    .align(alignment = Alignment.CenterHorizontally)
                            )
                            Text(
                                text = stringResource(R.string.empty_state_text),
                                modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(all = 20.dp)

            ) {
                Text(
                    text = "Add new context",
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                    )
                // Интересно чего это метод onChangeNameValue не работает когда написан универсально, но работает когда написано 3 отдельных метода. по любому есть решение.
                // А еще надо текст ошибки разтиражировать с 1 до 3 (на 3 поля вместо 1) и вставлять когда и где надо.
                OutlinedTextField(
                    value = state.value.name,
                    onValueChange = {
                        name -> viewModel.onChangeNameFieldValue(name)
                        viewModel.clearErrorText()

                    },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_name_field_label)) },
                    supportingText = {
                        if (state.value.isNameEmpty) {
                            Text(
                                text = state.value.errorText,
                                color = Color.Red
                            )
                        }
                    }
                    /** Весь код ниже - раскомментировать и привести к макету */
                    /*supportingText = {
                        // Вся эта конструкция через let мне не нравится...
                        state.value.errorText.let {
                            viewModel.showErrorText(it)?.let {
                                Text(
                                    text = it,
                                    color = Color.Red
                                )
                            }
                        }
                    }*/
                )

                OutlinedTextField(
                    value = state.value.surName,
                    onValueChange = {
                        surName -> viewModel.onChangeSurnameValue(surName)
                        viewModel.clearErrorText()
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_surname_field_label)) },
                    supportingText = {
                        if (state.value.isSurNameEmpty) {
                            Text(
                                text = state.value.errorText,
                                color = Color.Red
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = state.value.number,
                    onValueChange = { number -> viewModel.onChangeNumberValue(number)
                        viewModel.clearErrorText()
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_number_field_label)) },
                    supportingText = {
                        if (state.value.isNumberEmpty) {
                            Text(
                                text = state.value.errorText,
                                color = Color.Red
                            )
                        }
                    },
                )

                val text = stringResource(R.string.emty_name_field_error)
                Button(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    onClick = { viewModel.addItemToList(emptyStateStringResource = text) }
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
