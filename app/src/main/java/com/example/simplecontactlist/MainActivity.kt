package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.room.util.copy
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
    val listItems = remember { mutableStateListOf("Text1", "Text2", "Text3") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            contentPadding = PaddingValues(all = 10.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Аватар пользователя",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    OutlinedTextField(
                        value = item,
                        onValueChange = {},
                        modifier = Modifier.width(200.dp),
                        label = { Text("Name")}
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier,
                        shape =  MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            top = 2.dp,
                            end = 2.dp,
                            bottom = 2.dp
                        )

                    ) {
                        Text("Delete")
                    }
                }
            }
        }
        AddContactButton()
    }
}

// Put OutlinedTextField here if possible.
@Composable
fun UserNameField() {

}

@Composable
fun AddContactButton() {
    val state = remember { mutableStateOf(false) }
    Button(
        onClick = { state.value = true }
    ) {
        Text("Add Contact")
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

