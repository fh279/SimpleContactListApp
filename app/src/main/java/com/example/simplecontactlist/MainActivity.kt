package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets.Type.ime
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val vmFactory = MyViewModelFactory(StringResourcesProvider(this))
    private lateinit var viewModel: MyViewModel

    @OptIn(ExperimentalLayoutApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(
            owner = this,
            factory = vmFactory
        ).get(MyViewModel::class.java)
        setContent {
            SimpleContactListTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imeNestedScroll()
                ) { innerPadding -> MainScreen(Modifier
                    .fillMaxSize()
                    .padding()) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainScreen(
        modifier: Modifier = Modifier
    ) {

        val state: State<ListState> = viewModel.state.collectAsStateWithLifecycle()
        val listState: ListState = state.value
        ContactsList(modifier, listState)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun ContactsList(modifier: Modifier = Modifier, state: ListState) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = ime().dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
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
                if (state.listItems.isNotEmpty()) {
                    items(state.listItems,
                        key = { it.id }
                    ) { item ->
                        val color = remember {
                            Color(
                                red = Random.nextFloat(),
                                green = Random.nextFloat(),
                                blue = Random.nextFloat()
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Absolute.Left,
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp, top = 10.dp)
                                .fillMaxWidth()
                                .background(color = Color.White, shape = RoundedCornerShape(5.dp)),
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color = color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.name.getOrNull(0).toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.width(250.dp)) {
                                Text(text = "${item.name} ${item.surname}")
                                Text(text = item.number)
                            }
                            Button(
                                onClick = { viewModel.removeItemFromList(item) },
                                modifier = Modifier,
                                shape = MaterialTheme.shapes.extraSmall,
                                contentPadding = PaddingValues(
                                    start = 2.dp,
                                    top = 2.dp,
                                    end = 2.dp,
                                    bottom = 2.dp
                                )
                            ) { Text(stringResource(R.string.delete_button_text)) }
                        }
                    }
                }
                else {
                    items(arrayListOf("")) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(all = 50.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.emptystate2),
                                contentDescription = "EmptyState",
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(CircleShape),
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
                    .animateContentSize()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
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
                    text = "Add new contact",
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.name,
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
                        if (state.name.isEmpty()) {
                            Text(
                                text = state.errorText ?: "",
                                color = Color.Red
                            )
                        }
                    },
                    maxLines = 1
                )

                OutlinedTextField(
                    value = state.surName,
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
                        if (state.surName.isEmpty()) {
                            Text(
                                text = state.errorText ?: "",
                                color = Color.Red
                            )
                        }
                    },
                    maxLines = 1
                )

                OutlinedTextField(
                    value = state.number,
                    onValueChange = { number -> viewModel.onChangeNumberValue(number)
                        viewModel.clearErrorText()
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Add_contact_number_field_label)) },
                    supportingText = {
                        if (state.number.isEmpty()) {
                            Text(
                                text = state.errorText ?: "",
                                color = Color.Red
                            )
                        }
                    },
                    maxLines = 1
                )

                val text = stringResource(R.string.emty_name_field_error)
                Button(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                    onClick = { viewModel.addItemToList(emptyStateStringResource = text) }
                ) { Text(stringResource(R.string.add_contact_button_text)) }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview(showBackground = true)
    @Composable
    fun ContactList() {
        SimpleContactListTheme {
            MainScreen()
        }
    }
}
