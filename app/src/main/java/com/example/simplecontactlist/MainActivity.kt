package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() - зачем вообще эта функция? fullscreen?
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
}

@Composable
fun MainScreen() {
    ContactsList()
}

@Composable
fun ContactsList() {
    // А как обрабатывать смену состояния? Типа, экран перевернул?
    // не обязательно юзать mutableStateListOf. попробуй metableStateOf а внутри лист строк.
    val listItems = remember { mutableStateListOf<String>()}
    var text by remember { mutableStateOf("") }
    // val insets = rememberKeyboardInsets() - что это за дичь из нейросетей?

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            contentPadding = PaddingValues(all = 0.dp),
            modifier = Modifier.height(350.dp)

        ) {
            if (listItems.isNotEmpty()) {
            items(listItems) { item ->
                Row(
                    horizontalArrangement = Arrangement.Absolute.Left,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp).fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Аватар пользователя",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        text = item,
                        modifier = Modifier.width(200.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { listItems.remove(item) },
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
                            text = "Тут еще ничего нет.",
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                    }

                }
            // Вот эта шляпа почему то не работает
            // Row { Text("Ты чо, сыч, что ли?..") }

        }
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 50.dp)
                .width(200.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            label = { Text("Введите имя") }
        )
        Button(
            modifier = Modifier.padding(top = 0.dp),
            onClick = { listItems.add(text) }
        ) { Text(stringResource(R.string.add_contact_button_text)) }
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
