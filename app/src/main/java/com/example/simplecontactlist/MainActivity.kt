package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            SimpleContactListTheme {
                Scaffold(modifier = Modifier.fillMaxSize() ) {/* innerPadding ->*/
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
    // Тут явно должны быть не строки. Либо эти строки должны как-то по-другому применяться.
    val listItems = remember { mutableStateListOf("Text1", "Text2", "Text3") }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            contentPadding = PaddingValues(all = 0.dp),
            modifier = Modifier.border(
                width = 1.dp,
                brush = Brush.sweepGradient(
                    Pair(1f, Color.Black),
                    Pair(2f, Color.Black)
                ),
                shape = RoundedCornerShape(8.dp)
            )

        ) {
            items(listItems) { item ->
                if (listItems.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 50.dp, end = 50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Аватар пользователя",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = item,
                        modifier = Modifier.border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                colors = arrayListOf(
                                    Color.Blue,
                                    Color.Black
                                )
                            ),
                            shape = Shapes().extraSmall
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
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
                else {
                        Text("Ты чо, сыч, что ли?..")
                }
            }
        }
        AddContactButton(
            list = listItems,
            name = text)

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(200.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            label = {Text("Введите имя")}

        )
    }
}

// Put OutlinedTextField here if possible.
@Composable
fun UserNameField() {

}

@Composable
fun AddContactButton(
    list: SnapshotStateList<String>,
    name: String) {
    val state = remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.padding(top = 100.dp),
        onClick = {
            state.value = true
            list.add(name)
        }
    ) {
        Text(stringResource(R.string.add_contact_button_text))
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

